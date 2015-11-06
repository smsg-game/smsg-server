#-*- coding:utf-8 -*-

from ldsg_admin.common import get_logging
import urllib, httplib

logging = get_logging()

class Post(object):
    """Post类用于发送一个post请求"""
    
    def __init__(self, data, host, path, port=8088):
        """
        @param data: 发post的数据，为字典格式化
        @param host: 连接的网站host,如 http://www.sina,com.cn
        @param path: 提交的路径,如/post.php?action=submit  
        """
        self._data = data
        self._host = host
        self._path = path
        self._port = port
        self._timeout = 10
        
    def submit(self, result_keyword=u"发表成功", get_content=False, debug=False):
        """提交
        @param result_keyword: 判断是否提交成功的关键字 
        """
        params = urllib.urlencode(self._data)
        headers = {"Content-Type": "application/x-www-form-urlencoded",   
                   "Connection": "Keep-Alive",
                    "Referer": "%s%s" % (self._host, self._path),
                    "Accept-Charset": "utf-8"}
        
        old_timeout = urllib.socket.getdefaulttimeout()
        conn = httplib.HTTPConnection(self._host, port=self._port);
        try:
            urllib.socket.setdefaulttimeout(self._timeout)
            conn.request(method="POST",url=self._path, body=params, headers=headers);   
            response = conn.getresponse();
        
            if response.status == 200:
                data = response.read()
                if not data:
                    logging.debug("not data return")
                    return False
                try:
                    data = data.decode('utf8')
                except:
                    pass
                if debug:
                    print "res:%s" % data
                if get_content:
                    return data
                if data.find(result_keyword) != -1:
                    return True
                else:
                    return False
            else:
                logging.debug("code[%s]" % response.status)
                return False
        except Exception, e:
            print e
        finally:
            urllib.socket.setdefaulttimeout(old_timeout)
            if conn:
                conn.close()
        
