#!/usr/bin/python
# -*- coding: utf-8 -*-

import time

from django.http import HttpResponseRedirect
from ldsg_admin.common import exception_mgr, log_execption
from ldsg_admin.business import user_roles
from ldsg_admin.model import UserPage


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


def login_required(input_function):
    """登录检测装饰器，若request带参数password，则会校验密码是否正确
    @param input_function:被装饰函数 
    @return: 替换函数
    """
    def replace_function(*args, **kwargs):
        request = args[0]
        user = user_roles.get_userinfo(request)
        if user:
            request.user = user
            try:
                return input_function(*args, **kwargs)
            except:                
                full_path = request.get_full_path()
                exception_mgr.on_except('request url: %r' % full_path, 1)
                log_execption('request url: %r' % full_path, 1)
                raise
        else:
            response = HttpResponseRedirect("/login/")
            return response
    replace_function.func_name = input_function.func_name
    replace_function.__doc__ = input_function.__doc__
    return replace_function

def privilege_required(page_id=0, operator_id=1):
    """装饰函数，检查当前用户是否对指定功能有查询权限
    @param page_id:页面编号 
    @param operator_id:操作编号，从低到高，按位-> 1增加，2删除 ，3更改 ，4查询 ，5特殊 
    """
    def decorator_funtion(input_function): #定义一个新函数，接收被装饰的函数
        def replace_function(*args, **kwargs): #被替换执行的函数，主要工作就在这里完成
            request = args[0]#这里要求参数传进来，第一个必须是request对象
            user = user_roles.get_userinfo(request)
            user_page = UserPage.load(user_id=int(user.id), page_id=page_id)
            if not user_page:
                response = HttpResponseRedirect("/privilege_error/")
                return response
            value = user_page.value
            if value == 1:
                response = HttpResponseRedirect("/privilege_error/")
                return response
            if operator_id and operator_id == 1 and value % 2 == 0:
                return input_function(*args, **kwargs)
            if operator_id and operator_id == 2 and value % 3 == 0:
                return input_function(*args, **kwargs)
            if operator_id and operator_id == 3 and value % 5 == 0:
                return input_function(*args, **kwargs)
            if operator_id and operator_id == 4 and value % 7 == 0:
                return input_function(*args, **kwargs)
            if operator_id and operator_id == 5 and value % 11 == 0:
                return input_function(*args, **kwargs)
            else:
                response = HttpResponseRedirect("/privilege_error/")
                return response
            
        replace_function.func_name = input_function.func_name
        replace_function.__doc__ = input_function.__doc__
        return replace_function
    return decorator_funtion

def rpc_login_required(input_function):
    """登录检测装饰器，若request带参数password，则会校验密码是否正确
    @param input_function:被装饰函数 
    @return: 替换函数
    """
    def replace_function(*args, **kwargs):
        request = args[0]
        user = user_roles.get_userinfo(request)
        if user or True:
            request.user = user
            try:
                return input_function(*args, **kwargs)
            except:                
                full_path = request.get_full_path()
                exception_mgr.on_except('request url: %r' % full_path, 1)
                log_execption('request url: %r' % full_path, 1)
                raise
        else:
            response = HttpResponseRedirect('/login/')
            return response
    replace_function.func_name = input_function.func_name
    replace_function.__doc__ = input_function.__doc__
    return replace_function