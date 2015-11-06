#!/usr/bin/python
# -*- coding: utf-8 -*

import os

import time
import traceback
import logging
from logging import handlers
from ldsg_admin.common import get_logging
from ldsg_admin.config import PathSettings
from ldsg_admin.common import exception_mgr, log_execption

def ensure_success(input_function):
    """确保程序一成功执行装饰器
    @param input_function:被装饰函数 
    @return: 替换函数
    """
    def replace_function(*args, **kwargs):
        while True:
            try:
                return input_function(*args, **kwargs)
            except KeyboardInterrupt:
                raise
            except:
                log_execption()
                exception_mgr.on_except()
                time.sleep(10)
                
    replace_function.func_name = input_function.func_name
    replace_function.__doc__ = input_function.__doc__
    return replace_function

class BaseClient(object):
    
    
    def __init__(self, name):
        self.__name = name
        self.inited = False
        self.init()
 
        
    def init(self):
        """初始化"""
        
        if self.inited:
            return
        
        self.inited = True
        
        self.logger = get_logging(self.__name)
        format1 = '%(asctime)s|%(process)d|%(threadName)s|%(levelname)s|%(pathname)s|%(lineno)s|%(funcName)s|%(message)s'

        self.logger.setLevel(logging.DEBUG)

        handlers.RotatingFileHandler
        handler1 = handlers.TimedRotatingFileHandler(when="d", filename=os.path.join(PathSettings.LOG, self.__name + '.log'))
        handler1.setLevel(logging.INFO)
        handler1.setFormatter(logging.Formatter(format1))
        self.logger.addHandler(handler1)
        
        
    def start(self):
        
        self.log("client start[%s]" % self.__name)
        
        self.before_run()
        
        try:
            self.run()
        except:
            self.log(traceback.format_exc())
        
        self.after_run()
        
    def run(self):
        pass
        
    def log(self, message):
        self.logger.info(message);
        
    def before_run(self):
        pass
        
    def after_run(self):
        pass
