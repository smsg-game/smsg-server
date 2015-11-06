#!/usr/bin/python
# -*- coding: utf-8 -*-

'''
Created on 2014-1-14

@author: Candon
'''

from ldsg_admin.common.jsonrpcserver import jsonrpc_function
from ldsg_admin.business import forces_business

@jsonrpc_function
def add_forces(request,server_id, forces_info):
    """保存&修改关卡次数"""
    return forces_business.add_forces(server_id,forces_info)
    