#!/usr/bin/python
# -*- coding: utf-8 -*-

'''
Created on 2014-1-13

@author: Candon
'''
from ldsg_admin.common.jsonrpcserver import jsonrpc_function
from ldsg_admin.business import activity_business, user_roles, server_business, notice_business
from ldsg_admin.constants.page_constant import PageConstant

@jsonrpc_function
def add_activity(request, server_id, activity_info):
    """保存&修改活动"""
    result = activity_business.add_activity(server_id,activity_info)
    return server_id, result
  
@jsonrpc_function
def get_tool_list(request, name):
    """获取道具列表"""
    return activity_business.get_tool_list(name)
    
@jsonrpc_function
def sync_system_tool_exchange(request, eid):
    
    system_tool_exchange = activity_business.get_system_tool_exchange(eid)
    server_id_list = system_tool_exchange.server_ids.split(",")
    for server_id in server_id_list:
        server = server_business.get_server(server_id)
        if server.is_test == 0:#没有正式服权限
            if not user_roles.had_privilege(request, PageConstant.page_tool_exchange_sync_id, 11):
                return -1, None
    
    return activity_business.sync_system_tool_exchange(eid)

@jsonrpc_function
def sync_system_pay_reward(request, rid):
    
    system_pay_reward = activity_business.get_system_pay_reward(rid)
    server_id_list = system_pay_reward.server_ids.split(",")
    for server_id in server_id_list:
        server = server_business.get_server(server_id)
        if server.is_test == 0:#没有正式服权限
            if not user_roles.had_privilege(request, PageConstant.page_pay_reward_sync_id, 11):
                return -1, None
    
    return activity_business.sync_system_pay_reward(rid)

@jsonrpc_function
def sync_system_mall_discount(request, did):
    print did
    system_mall_discount = activity_business.get_system_mall_discount(did)
    server_id_list = system_mall_discount.server_ids.split(",")
    for server_id in server_id_list:
        server = server_business.get_server(server_id)
        if server.is_test == 0:#没有正式服权限
            if not user_roles.had_privilege(request, PageConstant.page_mall_discount_sync_id, 11):
                return -1, None
    
    return activity_business.sync_system_mall_discount(did)
    
@jsonrpc_function
def add_system_tool_exchange(request, eid, name, server_ids):
    user = user_roles.get_userinfo(request)
    if user:
        username = user.username
    else:
        username = ""
    
    server_id_list = server_ids.split(",")
    for server_id in server_id_list:
        server = server_business.get_server(server_id)
        if server.is_test == 0:#没有正式服权限
            if not user_roles.had_privilege(request, PageConstant.page_tool_exchange_id, 11):
                return -1
        
    return activity_business.add_system_tool_exchange(eid, name, server_ids, username)

@jsonrpc_function
def add_system_pay_reward(request, rid, name, reward_type, server_ids):
    user = user_roles.get_userinfo(request)
    if user:
        username = user.username
    else:
        username = ""
        
    server_id_list = server_ids.split(",")
    for server_id in server_id_list:
        server = server_business.get_server(server_id)
        if server.is_test == 0:#没有正式服权限
            if not user_roles.had_privilege(request, PageConstant.page_pay_reward_id, 11):
                return -1
        
    return activity_business.add_system_pay_reward(rid, name, reward_type, server_ids, username)

@jsonrpc_function
def add_system_mall_discount(request, did, name, start_time, end_time, server_ids):
    user = user_roles.get_userinfo(request)
    if user:
        username = user.username
    else:
        username = ""
        
    server_id_list = server_ids.split(",")
    for server_id in server_id_list:
        server = server_business.get_server(server_id)
        if server.is_test == 0:#没有正式服权限
            if not user_roles.had_privilege(request, PageConstant.page_mall_discount_id, 11):
                return -1
        
    return activity_business.add_system_mall_discount(did, name, start_time, end_time, server_ids, username)

@jsonrpc_function
def add_notice(request, info):
    user = user_roles.get_userinfo(request)
    if user:
        username = user.username
    else:
        username = ""
        
    nid = info["id"]
    nid = int(nid)
    if not nid:
        info["created_user"] = username

    return notice_business.add_notice(info)

