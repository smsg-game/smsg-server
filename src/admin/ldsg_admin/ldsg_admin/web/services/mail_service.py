#!/usr/bin/python
# -*- coding: utf-8 -*-
"""邮件相关rpc"""

from datetime import datetime

from ldsg_admin.common.jsonrpcserver import jsonrpc_function
from ldsg_admin.business import user_roles, mail_business, server_business
from ldsg_admin.common import get_logging
from ldsg_admin.constants.page_constant import PageConstant

logging = get_logging()

@jsonrpc_function
def add_system_mail(request, info):
    """保存系统邮件"""
    logging.info(u"保存系统邮件")
    user = user_roles.get_userinfo(request)
    if user:
        username = user.username
    else:
        username = ""
        
    info["created_user"] = username
    info["status"] = 0
    info["approve_time"] = datetime.now()
    if not info["date"]:
        info["date"] = datetime.now()
        
    if info.get("lodo_ids"):
        lodo_id_set = set()
        lodo_id_list = info["lodo_ids"].split(",")
        for lodo_id in lodo_id_list:
            lodo_id_set.add(str(lodo_id.strip()))
        info["lodo_ids"] = ",".join(lodo_id_set)

    server_ids = info["server_ids"]
    server_id_list = server_ids.split(",")
    for server_id in server_id_list:
        server = server_business.get_server(server_id)
        if server.is_test == 0:#没有正式服权限
            if not user_roles.had_privilege(request, PageConstant.page_send_mail_id, 11):
                return -1

    return mail_business.add_system_mail(info)

@jsonrpc_function
def system_mail_approve(request, system_mail_id, approve):
    user = user_roles.get_userinfo(request)
    if user:
        username = user.username
    else:
        username = ""
        
    system_mail = mail_business.get_system_mail(system_mail_id)
    server_id_list = system_mail.server_ids.split(",")
    for server_id in server_id_list:
        server = server_business.get_server(server_id)
        if server.is_test == 0:#没有正式服权限
            if not user_roles.had_privilege(request, PageConstant.page_approve_mail_id, 11):
                return -1, None
           
    return mail_business.system_mail_approve(system_mail_id, username, int(approve) == 1)


