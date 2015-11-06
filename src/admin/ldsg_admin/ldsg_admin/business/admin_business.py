#!/usr/bin/python
# -*- coding: utf-8 -*-
"""
后台相关操作
"""

import time

from ldsg_admin.common.mysql import get_connection, connection
from ldsg_admin.constants.db_constant import ldsg_center_db_configs
from ldsg_admin.constants.server_constant import server_zone_api_url_map

import datetime
from ldsg_admin.model import User
from ldsg_admin.model import Page
from ldsg_admin.model import UserPage
from ldsg_admin.model import WhiteIp
from ldsg_admin.common import json, md5mgr
from ldsg_admin.util import http_util
from ldsg_admin.common import get_logging
from ldsg_admin.business import server_business

logging = get_logging()

def add_user(uid, username, password, nickname):
    uid = int(uid)
    if uid:
        user = User.load(id=uid)
        if password:
            user.password = password
    else:
        user = User()
        user.password = password
    
    user.id = uid
    user.username = username
    user.nickname = nickname
    user.persist()

def get_user_list(page=1, pagesize=20):
    return User.paging(page, pagesize)

def add_page(page_id, name):
    page_id = int(page_id)
    if page_id:
        page = Page.load(page_id=page_id)
        if page:
            page.updated_time = datetime.datetime.now()
    else:
        page = Page()
        page.created_time = datetime.datetime.now()
        page.updated_time = datetime.datetime.now()
    page.name = name
    page.persist()

def get_page_list(page=1,pagesize=20):
    return Page.paging(page, pagesize)

def get_page_all_list(user_id):
    new_pages = []
    pages = Page.query()
    for p in pages:
        if not p:
            continue
        user_page = UserPage.load(user_id=int(user_id), page_id=p.page_id)
        if user_page:
            p.user_page_id = user_page.user_page_id
            p.value = user_page.value
            new_pages.append(p)
        else :
            p.user_page_id = 0
            p.value = 0
            new_pages.append(p)            
    return new_pages

def get_game_web_status(ucenter='android'):
    conn = get_connection(ldsg_center_db_configs[ucenter])
    cursor = conn.cursor()
    try:
        if ucenter == "android":
            pid = 5
        else:
            pid = 6
        info = cursor.fetchone('select status from server_status where id=%s' % pid)
        if info:
            return info["status"]
    finally:
        cursor.close()
        
    return 0

def set_game_web_status(ucenter='android', status=0):
    
    white_ip_list = WhiteIp.query(condition="ucenter='%s'" % ucenter)
    ip_list = []
    for white_ip in white_ip_list:
        ip_list.append(white_ip.ip)
    
    info = {}
    timestamp = int(time.time() * 1000)
    info["timestamp"] = timestamp
    info["sign"] = md5mgr.mkmd5fromstr("%s%s" % (timestamp, "gCvKaE0tTcWtHsPkbRdE"))
    info["status"] = status
    info["id"] = 5 if ucenter == "android" else 6
    info["whiteList"] = ",".join(ip_list)

    if ucenter == "android":
        host = "wapi.android.3qchibi.com"
    elif ucenter == "ios":
        host = "wapi.ios.3qchibi.com"

    url = "http://" + host + ":8088//webApi//setServerStatus.do"
    success, ret_val = http_util.request(url, info)
    if success:
        logging.info(ret_val)
        result = json.loads(ret_val)
        return result.get("rc") == 1000

def get_game_web_white_ip_list(ucenter, page, pagesize, ip):
    condition = "ucenter='%s'" % ucenter
    if ip:
        condition += " and ip = '%s'" % ip
    
    return WhiteIp.paging(page, pagesize, condition=condition)

def add_white_ip(ucenter, ip):
    white_ip = WhiteIp()
    white_ip.ucenter = ucenter
    white_ip.ip = ip
    white_ip.persist()
    
    status = get_game_web_status(ucenter)
    set_game_web_status(ucenter, status)
    
def delete_white_ip(ucenter, ip):
    white_ip = WhiteIp.load(ucenter=ucenter, ip=ip)
    white_ip.delete()
    
    status = get_game_web_status(ucenter)
    set_game_web_status(ucenter, status)

def sync_server_status(server_zone):
    server_zone = int(server_zone)
    host = server_zone_api_url_map[server_zone]
    url = "http://" + host + ":8088//webApi//updateConfigs.do"
    success, ret_val = http_util.request(url, {})
    if success:
        result = json.loads(ret_val)
        success = result.get("rc") == 1000
    
    logging.debug("success sync url[%s], result[%s]" % (url, success))
    
    return success

def get_notice_list(ucenter, server_id, partner_id, page, pagesize):
    conn = get_connection(ldsg_center_db_configs[ucenter])
    cursor = conn.cursor()
    total, infos = 0, []
    try:
        where = '1=1'
        if server_id:
            where += " and server_id='%s'" % server_id
        if partner_id:
            where += " and partner_id='%s'" % partner_id
        total = cursor.fetchone('select count(1) as total from notice where %s' % where)["total"]
        start = (page - 1) * pagesize 
        if total > 0:
            infos = cursor.fetchall("select * from notice where %s order by created_time desc limit %s, %s" % (where, start, pagesize))
    finally:
        cursor.close()
        
    return total, infos


def sync_tool_table(server_id):
    db_config = server_business.get_server_db_connect(server_id=server_id)
    connect = get_connection(db_config)
    cursor = connect.cursor()
    cursor_loc = connection.cursor()
    tool_infos = []
    sqls = []
    try:
        
        colors = {0: u'白', 1: u'绿', 2: u'蓝',  3: u'紫', 4: u'橙', 5: u'鬼'}
        
        sqls.append(u"truncate table system_tool;")
        sqls.append(u"insert into system_tool(tool_id, tool_type, tool_name) values(0, 1, '元宝');")
        sqls.append(u"insert into system_tool(tool_id, tool_type, tool_name) values(0, 2, '银币');")
        sqls.append(u"insert into system_tool(tool_id, tool_type, tool_name) values(0, 6, '武将背包');")
        sqls.append(u"insert into system_tool(tool_id, tool_type, tool_name) values(0, 7, '装备背包');")
        sqls.append(u"insert into system_tool(tool_id, tool_type, tool_name) values(0, 8, '国战声望');")
        
        infos = cursor.fetchall("select hero_name, hero_color, system_hero_id  from system_hero")
        logging.debug("sync system hero")
        for info in infos:
            system_hero_id = info["system_hero_id"]
            hero_desc = '%s-%s' % (info["hero_name"], colors.get(info["hero_color"]))
            #sqls.append(u"insert into system_tool(tool_id, tool_type, tool_name) values(%s, 3001, '%s');" % (system_hero_id, hero_desc))
            tool_infos.append({"tool_id": system_hero_id, "tool_type": 3001, "tool_name": hero_desc})
                
        infos = cursor.fetchall("select equip_name, color, equip_id  from system_equip")
        logging.debug("sync system equip")
        for info in infos:
            equip_id = info["equip_id"]
            equip_desc = '%s-%s' % (info["equip_name"], colors.get(info["color"]))
            
            #sqls.append(u"insert into system_tool(tool_id, tool_type, tool_name) values(%s, 2001, '%s');" % (equip_id, equip_desc))
            tool_infos.append({"tool_id": equip_id, "tool_type": 2001, "tool_name": equip_desc})
            
    
        infos = cursor.fetchall("select name, color, tool_id, type from system_tool")
        logging.debug("sync system tool")
        for info in infos:
            tool_id = info["tool_id"]
            tool_desc = '%s-%s' % (info["name"], colors.get(info["color"]))
            tool_type = info["type"]
            #sqls.append(u"insert into system_tool(tool_id, tool_type, tool_name) values(%s, %s, '%s');" % (tool_id, tool_type, tool_desc))
            tool_infos.append({"tool_id": tool_id, "tool_type": tool_type, "tool_name": tool_desc})
    
        for sql in sqls:
            cursor_loc.execute(sql)
            
        cursor_loc.insert(tool_infos, "system_tool")
    
    finally:
        cursor.close()
        cursor_loc.close()