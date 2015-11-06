#!/usr/bin/python
# -*- coding: utf-8 -*-

'''
Created on 2014-1-13

@author: Candon
'''
from ldsg_admin.common.jsonrpcserver import jsonrpc_function
from ldsg_admin.business import client_business
from ldsg_admin.common import get_logging

logging = get_logging()

@jsonrpc_function
def register(request, client_name, server_ids):
    """客户端注册"""
    ip = request.META['REMOTE_ADDR']
    logging.debug("cleint register.ip[%s], client_name[%s]" % (ip, client_name))
    return client_business.register(ip, client_name, server_ids)

@jsonrpc_function
def get_command(request, client_id):
    """客户端注册"""
    logging.debug("get command.client_id[%s]" % client_id)
    return client_business.get_command(client_id)

@jsonrpc_function
def report_status(request, client_id):
    """客户端上报状态"""
    logging.debug("cleint report_status.client_id[%s]" % client_id)
    return client_business.report_status(client_id)

@jsonrpc_function
def report_result(request, client_id, data):
    """客户端上报结果"""
    logging.debug("cleint report_result.client_id[%s]" % client_id)
    return client_business.report_result(client_id, data)

@jsonrpc_function
def report_log(request, client_id, server_id, ret, command_type, output, params):
    logging.debug("report log.client_id[%s], server_id[%s], command_type[%s], ret[%s], params[%s]" % (client_id, server_id, command_type, ret, params))
    return client_business.report_log(client_id, server_id, ret, command_type, output, params)

@jsonrpc_function
def add_command(request, client_ids, command):
    return client_business.add_command(client_ids, command)

@jsonrpc_function
def add_battle_client(request, cid, server_id):
    return client_business.update_battle_clinet(cid, server_id)

@jsonrpc_function
def delete_battle_client(request, cid):
    return client_business.delete_battle_clinet(cid)



