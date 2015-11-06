#!/usr/bin/python
# -*- coding: utf-8 -*-

'''
Created on 2014-1-13

@author: Candon
'''
from ldsg_admin.common.jsonrpcserver import jsonrpc_function
from ldsg_admin.business import package_business

@jsonrpc_function
def add_package(request, ucenter, package_info):
    """保存&修改包"""
    return package_business.add_package(ucenter, package_info)

@jsonrpc_function
def sync_package(request, ucenter, pid):
    """同步到其它合作商"""
    return package_business.sync_package(ucenter, pid)
    
