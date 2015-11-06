#!/usr/bin/python
# -*- coding: utf-8 -*-
"""MD5相关操作"""

from hashlib import md5, sha1
from binascii import crc32
from os.path import exists, isfile

def mkmd5fromdict(dict, key):
    keyss = dict.keys()
    keyss.sort()
    tempStr = "&".join(["%s=%s" % (param, dict[param]) for param in keyss])
    return md5(tempStr + key).hexdigest()

def mkmd5fromstr(str):
    if isinstance(str, unicode):
        str = str.encode('utf-8')
    return md5(str).hexdigest()

def mkmd5fromfile(filename, blockM = 1):#block with (M)
    opened = False
    fobj = None
    if hasattr(filename, "read"):
        fobj = filename
    else:
        if exists(filename) and isfile(filename):
            fobj = open(filename, "rb")
            opened = True
    if fobj:
        blockB = blockM * 1024 * 1024
        try:
            m = md5()
            while True:
                fb = fobj.read(blockB)
                if not fb:
                    break
                m.update(fb)
        finally:
            if opened:
                fobj.close()
        return m.hexdigest()
    else:
        return 0

def mkcrc32fromfile(filename, blockM = 1):#block with (M)
    if exists(filename) and isfile(filename):
        blockB = blockM * 1024 * 1024
        crc = 0
        f = open(filename, "rb")
        while True:
            fb = f.read(blockB)
            if not fb:
                break;
            crc = crc32(fb, crc)
        f.close()
        res = ''
        for i in range(4):
            t= crc & 0xFF
            crc >>= 8
            res = '%02x%s' % (t, res)
        return "0x" + res
    else:
        return 0
    
def mksha1fromfile(filename, blockM = 1):
    if exists(filename) and isfile(filename):
        blockB = blockM * 1024 * 1024
        m = sha1()
        fobj = open(filename, "rb")   
        while True:
            fb = fobj.read(blockB)
            if not fb:
                break
            m.update(fb)
        fobj.close()
        return m.hexdigest()
    else:
        return 0
    
def mksha1fromstr(bytes):
    return sha1(bytes).hexdigest()
    
class AsyncMD5():
    def __init__(self):
        self._m = md5()
        self._writed = False
    
    def write(self, block):
        self._m.update(block)
        self._writed = True
    
    def truncate(self, size = 0):
        self._m = md5()
        self._writed = False
    
    def get_md5(self):
        if self._writed:
            return self._m.hexdigest()
        else: 
            return ""
    
if __name__ == "__main__":
    print mksha1fromstr('mercury123'), len(mksha1fromstr('mercury123'))
    print mkmd5fromstr('mercury123'), len(mkmd5fromstr('mercury123'))
    print mkmd5fromfile('md5mgr.py'), mksha1fromfile('md5mgr.py')
    