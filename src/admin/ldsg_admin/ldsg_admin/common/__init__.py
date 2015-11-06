#!/usr/bin/python
# -*- coding: utf-8 -*-

import os
import logging
from logging import handlers
import errno
import traceback
import sys

from ldsg_admin.common import memcache
from ldsg_admin.config import PathSettings

cache = memcache.Client(["127.0.0.1:12000"])

class _InitLog(object):
    """初始化日志系统"""
    def __init__(self):
        self.inited = False

    def __call__(self, prefix=''):
        '''初始化 logging
        
        把 WARNING 及以上级别的日志发到 SYSLOG_HOST 的 syslog，DEBUG 及以上的级别发
        到终端。

        第二次调用该函数时不进行任何操作。
        '''
        if self.inited:
            return
        
        return self.force_init(prefix)
    
    def force_init(self, prefix=''):        
        format1 = '%(process)d|%(threadName)s|%(levelname)s|%(pathname)s|%(lineno)s|%(funcName)s|%(message)s'
        format2 = '%(asctime)s|%(message)s'
        
        [x.close() for x in logging.root.handlers]
        del logging.root.handlers[:]
        logging.root.setLevel(logging.DEBUG)

        handler1 = handlers.RotatingFileHandler(filename=os.path.join(PathSettings.LOG, 'all.log'), maxBytes=1024*3)
        handler1.setLevel(logging.DEBUG)
        handler1.setFormatter(logging.Formatter(format1))
        logging.root.addHandler(handler1)
        
        handler3 = logging.StreamHandler()
        handler3.setLevel(logging.DEBUG)
        handler3.setFormatter(logging.Formatter(format2))
        logging.root.addHandler(handler3)
        
        self.inited = True
        
    def get_local_logfile(self, logdir, prefix=''):
        if prefix and not prefix.endswith('.'):
            prefix += '.'
        
        if not os.path.exists(logdir):
            os.makedirs(logdir)
            
        i = 1
        while 1:
            logfile = os.path.join(logdir, '%slog.%i' % (prefix, i))
            if self.lock_file(logfile):
                return logfile
            i += 1
            
    def is_locked(self, lockpath):
        if not os.path.exists(lockpath) or os.name != 'posix': # windows 不检测
            return False
        f = open(lockpath)
        try:
            pid = int(f.read().strip())
        finally:
            f.close()
        try:
            os.kill(pid, 0)
        except OSError, e:
            if e.errno in (errno.ESRCH, errno.EPERM):
                return False
            else:
                raise
        return True

    def lock_file(self, fname):
        lockFname = fname + '.lock'
        if self.is_locked(lockFname):
            return False
        f = open(lockFname, 'w')
        try:
            f.write('%d\n' % os.getpid())
        finally:
            f.close()
        return True
        
init_log = _InitLog()

def get_logging(name=None):
    init_log()
    return logging.getLogger(name)

def ensure_utf8(s):
    for charset in ("utf8", "gbk", "gb2312"):
        try:
            return s.decode(charset)
        except:
            pass
    return s

def log_execption(msg = None, track_index = 0):
    """自动记录异常信息"""
    init_log()
    backup = logging.currentframe
    need_format = True
    if not msg:
        msg = traceback.format_exc(track_index)
        need_format = False
    msg = ensure_utf8(msg)
    try:
        currentframe = lambda: sys._getframe(5)
        logging.currentframe = currentframe # hook, get the real frame
        if need_format:
            logging.error(repr(traceback.format_exc(msg, track_index)) + '\n')
        else:
            logging.error(msg + '\n')
    except:
        pass
    finally:
        logging.currentframe = backup
        
        
if __name__ == '__main__':
    logging = get_logging("test")
    logging.error("test")
        
    
