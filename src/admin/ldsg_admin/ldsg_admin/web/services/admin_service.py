#!/usr/bin/python
# -*- coding: utf-8 -*-
"""文章相关rpc"""

from ldsg_admin.common.jsonrpcserver import jsonrpc_function
from ldsg_admin.business import server_business, blackimei_business, partner_business, admin_business, gift_code_business, notice_business, payment_business
from ldsg_admin.common import get_logging

logging = get_logging()


@jsonrpc_function
def add_server(request, server_info):
    """保存&修改服务器"""
    return server_business.add_server(server_info)
    
@jsonrpc_function
def delete_server(request, sid):
    """删除服务器"""
    return server_business.delete_server(sid)
    
@jsonrpc_function
def add_partner(request, partner_id, name, server_zone):
    """添加合作商"""
    return partner_business.add_partner(partner_id, name, server_zone)

@jsonrpc_function
def delete_partner(request, partner_id):
    """删除合作商"""
    return partner_business.delete_partner(partner_id)

@jsonrpc_function
def add_user(request, uid, username, password, nickname):
    """添加用户"""
    return admin_business.add_user(uid, username, password, nickname)

@jsonrpc_function
def del_user(request, uid):
    """删除用户"""
    return admin_business.del_user(uid)

@jsonrpc_function
def add_page(request, page_id, name):
    """添加页面"""
    return admin_business.add_page(page_id, name)

@jsonrpc_function
def add_giftcode(request, category, gift_bag_type, sub_type, amount):
    logging.debug("create gift code.category[%s], gift_bag_type[%s], sub_type[%s], amount[%s]" % (category, gift_bag_type, sub_type, amount))
    return gift_code_business.add_giftcode(category, gift_bag_type, sub_type, amount)

@jsonrpc_function
def set_server_partner_list(request, server_id, server_zone, partner_ids):
    return server_business.set_server_partner_list(server_id, int(server_zone), partner_ids)

@jsonrpc_function 
def set_game_web_status(request, ucenter, status):
    return admin_business.set_game_web_status(ucenter, status)

@jsonrpc_function 
def add_white_ip(request, ucenter, ip):
    return admin_business.add_white_ip(ucenter, ip)

@jsonrpc_function 
def delete_white_ip(request, ucenter, ip):
    return admin_business.delete_white_ip(ucenter, ip)

@jsonrpc_function 
def sync_server_status(request, server_zone):
    return admin_business.sync_server_status(server_zone)

@jsonrpc_function
def add_system_notice(request, ucenter, info):
    return notice_business.add_system_notice(ucenter, info)

@jsonrpc_function
def sync_tool_table(request, server_id):
    return admin_business.sync_tool_table(server_id)

@jsonrpc_function
def fill_order(request, ucenter, order_id, partner_order_id):
    return payment_business.fill_payment_order(ucenter, order_id, partner_order_id)

@jsonrpc_function
def add_black_imei(request, ucenter,imei,partner_id):
    return blackimei_business.add_black_imei(ucenter,imei,partner_id)

@jsonrpc_function
def delete_black_imei(request,ucenter,id):
    return blackimei_business.del_black_imei(ucenter,id)