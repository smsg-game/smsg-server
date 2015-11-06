#!/usr/bin/python
# -*- coding: utf-8 -*-

'''
Created on 2014-1-13

@author: Candon
'''
from ldsg_admin.common.jsonrpcserver import jsonrpc_function
from ldsg_admin.business import activity_business

@jsonrpc_function
def add_activity(request,server_id, activity_info):
    """保存&修改活动"""
    return activity_business.add_activity(server_id,activity_info)
    
