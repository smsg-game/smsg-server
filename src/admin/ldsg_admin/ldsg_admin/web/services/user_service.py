#!/usr/bin/python
# -*- coding: utf-8 -*-
"""用户相关rpc"""

from ldsg_admin.common.jsonrpcserver import jsonrpc_function
from ldsg_admin.business import user_business, server_business, user_roles
from ldsg_admin.common import get_logging
from ldsg_admin.constants.page_constant import PageConstant

logging = get_logging()

@jsonrpc_function
def open_all_scene(request, server_id, user_id):
    """用户地图全开"""
    logging.info(u"地图全开.server_id[%s, user_id[%s]" % (server_id, user_id))
    return user_business.open_all_scene(server_id, user_id)

@jsonrpc_function
def update_user_level(request, server_id, user_id, level):
    """用户调级"""
    logging.info(u"用户调级.server_id[%s, user_id[%s], level[%s]" % (server_id, user_id, level))
    server = server_business.get_server(server_id)
    if server.is_test == 0:#没有正式服权限
        if not user_roles.had_privilege(request, PageConstant.page_update_user_level, 11):
            return -1
    user_business.update_user_level(server_id, user_id, level)
    return 1

@jsonrpc_function
def update_user_vip_level(request, server_id, user_id, vip_level):
    """用户调级"""
    logging.info(u"用户VIP调级.server_id[%s, user_id[%s], level[%s]" % (server_id, user_id, vip_level))
    server = server_business.get_server(server_id)
    if server.is_test == 0:#没有正式服权限
        if not user_roles.had_privilege(request, PageConstant.page_update_user_vip_level, 11):
            return -1
    user_business.update_user_vip_level(server_id, user_id, vip_level)
    return 1

@jsonrpc_function
def block_user(request, server_id, user_id, due_time):
    """封号"""
    logging.info(u"用户封号.server_id[%s, user_id[%s], due_time[%s]" % (server_id, user_id, due_time))
    server = server_business.get_server(server_id)
    if server.is_test == 0:#没有正式服权限
        if not user_roles.had_privilege(request, PageConstant.page_ban_user, 11):
            return -1
    user_business.block_user(server_id, user_id, due_time)
    return 1