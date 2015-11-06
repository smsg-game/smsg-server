#!/usr/bin/python
# -*- coding: utf-8 -*-
"""基于simplejson的json序列化和反序列化

几乎所有的函数都可以包装成为 jsonrpc, 只要他的参数及返回值能够 json 化.
能 json 化的对象包括:
    None, True, False, 整数, 浮点数, 字符串, 
    tuple(会被转化为list), list, dict(字典的key会被转化为字符串), 
        以及这些对象的嵌套形式.
"""

import datetime
import sys

import simplejson
from simplejson.decoder import BACKSLASH, STRINGCHUNK, DEFAULT_ENCODING, errmsg, \
    JSONArray, JSONObject, _CONSTANTS, make_scanner
    


class _Encoder(simplejson.JSONEncoder):
    
    DATE_FORMAT = "%Y-%m-%d" 
    TIME_FORMAT = "%H:%M:%S"
    
    def __init__(self, *args, **kwargs):
        if 'ensure_ascii' not in kwargs:
            kwargs['ensure_ascii'] = False # 避免'ValueError: Invalid \uXXXX\uXXXX surrogate pair: line 1 column 4291 (char 4291)'
        simplejson.JSONEncoder.__init__(self, *args, **kwargs)
        
    def default(self, a):
        if isinstance(a, datetime.datetime):
            return {'__datetime__': (a.year, a.month, a.day, a.hour, 
                                     a.minute, a.second, a.microsecond)}
        elif isinstance(a, datetime.date):
            return {'__date__': (a.year, a.month, a.day)}
        elif isinstance(a, set):
            return {'__set__': list(a)}
        return super(_Encoder, self).default(a)

def _object_hook(dct):
    if '__datetime__' in dct:
        return datetime.datetime(*dct['__datetime__'])
    elif '__date__' in dct:
        return datetime.date(*dct['__date__'])
    elif '__set__' in dct:
        return set(dct['__set__'])
    return dct

def _py_scanstring(s, end, encoding=None, strict=True, _b=BACKSLASH, _m=STRINGCHUNK.match):
    """Scan the string s for a JSON string. End is the index of the
    character in s after the quote that started the JSON string.
    Unescapes all valid JSON string escape sequences and raises ValueError
    on attempt to decode an invalid string. If strict is False then literal
    control characters are allowed in the string.
    
    Returns a tuple of the decoded string and the index of the character in s
    after the end quote.
    
    兼容64bit系统""Invalid \\uXXXX\\uXXXX surrogate pair""问题
    """
    if encoding is None:
        encoding = DEFAULT_ENCODING
    chunks = []
    _append = chunks.append
    begin = end - 1
    while 1:
        chunk = _m(s, end)
        if chunk is None:
            raise ValueError(
                errmsg("Unterminated string starting at", s, begin))
        end = chunk.end()
        content, terminator = chunk.groups()
        # Content is contains zero or more unescaped string characters
        if content:
            if not isinstance(content, unicode):
                content = unicode(content, encoding)
            _append(content)
        # Terminator is the end of string, a literal control character,
        # or a backslash denoting that an escape sequence follows
        if terminator == '"':
            break
        elif terminator != '\\':
            if strict:
                msg = "Invalid control character %r at" % (terminator,)
                #msg = "Invalid control character {0!r} at".format(terminator)
                raise ValueError(errmsg(msg, s, end))
            else:
                _append(terminator)
                continue
        try:
            esc = s[end]
        except IndexError:
            raise ValueError(
                errmsg("Unterminated string starting at", s, begin))
        # If not a unicode escape sequence, must be in the lookup table
        if esc != 'u':
            try:
                char = _b[esc]
            except KeyError:
                msg = "Invalid \\escape: " + repr(esc)
                raise ValueError(errmsg(msg, s, end))
            end += 1
        else:
            # Unicode escape sequence
            esc = s[end + 1:end + 5]
            next_end = end + 5
            if len(esc) != 4:
                msg = "Invalid \\uXXXX escape"
                raise ValueError(errmsg(msg, s, end))
            uni = int(esc, 16)
            # Check for surrogate pair on UCS-4 systems
            if 0xd800 <= uni <= 0xdbff and False:# and sys.maxunicode > 65535: #64位系统会出问题。。。
                msg = "Invalid \\uXXXX\\uXXXX surrogate pair"
                if not s[end + 5:end + 7] == '\\u':
                    raise ValueError(errmsg(msg, s, end))
                esc2 = s[end + 7:end + 11]
                if len(esc2) != 4:
                    raise ValueError(errmsg(msg, s, end))
                uni2 = int(esc2, 16)
                uni = 0x10000 + (((uni - 0xd800) << 10) | (uni2 - 0xdc00))
                next_end += 6
            char = unichr(uni)
            end = next_end
        # Append the unescaped character
        _append(char)
#    print 'it works!'
    return u''.join(chunks), end

class _Decoder(simplejson.JSONDecoder):
    
    def __init__(self, encoding=None, object_hook=_object_hook, parse_float=None,
                 parse_int=None, parse_constant=None, strict=True, memo={}):
        self.encoding = encoding
        self.object_hook = object_hook
        self.parse_float = parse_float or float
        self.parse_int = parse_int or int
        self.parse_constant = parse_constant or _CONSTANTS.__getitem__
        self.strict = strict
        self.parse_object = JSONObject
        self.parse_array = JSONArray
        self.parse_string = _py_scanstring
        self.object_pairs_hook = None
        self.memo = memo
        self.scan_once = make_scanner(self)

JSONEncoder = _Encoder
JSONDecoder = _Decoder

def dumps(o, ensure_ascii=True):
    """返回JSON序列化字符串(unicode)
       
    支持Datetime格式，但不能反序列化"""
    return simplejson.dumps(o, ensure_ascii=ensure_ascii, cls=_Encoder)

def loads(s):
    """JSON反序列化"""
    return simplejson.loads(s, cls=_Decoder)

def dump(o, fp):
    return simplejson.dump(o, fp, cls=_Encoder)

def load(fp):
    return simplejson.load(fp, cls=_Decoder)

        
if __name__ == '__main__':
    l = [1, 0,
         None,
         True, False, 
         'str', u'unicode', '晕aaa', u'继a续晕', 
         {'1':1, '2':'2', u'3':u'3'},
         datetime.datetime.now(),
         ]
    print l
    data = dumps(l)
#    assert isinstance(data, unicode)
    print `data`
    print loads(data)
#    assert loads(data) == l
    print sys.maxunicode > 65535
    f = open('json_test.dat', 'rb')
    try:
        dd = JSONDecoder().decode(f.read())
        
        for t in [dd]:
            print t['title']
            print t['description']
            print '-' * 60
    finally:
        f.close()