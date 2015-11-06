#!/usr/bin/python
# -*- coding: utf-8 -*-
"""公共custom tags and filter"""

from ldsg_admin.constants.tool_use_type import tool_use_type_map
from ldsg_admin.constants.tool_type import tool_type_map
from ldsg_admin.constants.server_constant import server_zone_map
from ldsg_admin.common import json
from ldsg_admin.business import server_business, partner_business
from django import template

register = template.Library()

@register.filter
def check_attr(attr_oten):
    return 0

@register.filter
def partner_name(partner_id):
    try:
        partner_id = int(partner_id)
        partner = partner_business.get_partner(partner_id)
        if partner:
            return partner.name
    except:
        pass
    
    
    return partner_id



@register.filter
def hero_color_name(hero_color):
    name = '未知'
    if hero_color == 0:
        name = u'白色'
    elif hero_color == 1:
        name = u'绿色'
    elif hero_color == 2:
        name = u'蓝色'
    elif hero_color == 3:
        name = u'紫色'
    elif hero_color == 4:
        name = u'橙色'
    elif hero_color == 5:
        name = u'红色'
    elif hero_color == 6:
        name = u'鬼将'
    
    return name

@register.filter
def server_id_width(server_id):
    if len(server_id) == 3:
        return "&nbsp;"
    else:
        return "&nbsp;" * 2

@register.filter
def use_type_name(use_type):
    return tool_use_type_map.get(use_type, u'未知[%s]' % use_type)

@register.filter
def tool_type_name(tool_type):
    return tool_type_map.get(tool_type, u'未知[%s]' % tool_type)


@register.filter
def server_status_name(status):
    return {2: u'<font color="red">火爆</font>', 3: u'<font color="green">最新</font>', 100: u'<font color="blue">未开放</font>'}.get(status, u'未知')

@register.filter
def command_type_name(command_type):
    command_map = {}
    command_map[1] = u"重启逻辑服(不重启resin)"
    command_map[2] = u"关闭逻辑服"
    command_map[3] = u"发布数据"
    command_map[4] = u"发布逻辑服"
    command_map[5] = u"发布战斗服"
    command_map[6] = u"运行战斗服"
    command_map[7] = u"重启逻辑服(重启resin)"
    return command_map.get(command_type)

@register.filter
def flag_name(flag):
    if flag == 1:
        return "<font color='green'>获得</font>"
    elif flag == -1:
        return "<font color='red'>消费</font>"
    
@register.filter
def server_name(server_id):
    server = server_business.get_server(server_id)
    if server:
        if server.is_test:
            return u"%s-%s" % (server.server_name, server.server_id)
        else:
            return u"%s-%s" % (server.server_name, server.server_id)

@register.filter
def server_names(server_ids):
    s = ""
    if not server_ids:
        return ""
    server_id_list = server_ids.split(",")
    for server_id in server_id_list:
        server = server_business.get_server(server_id)
        if server:
            if s:
                s = "%s|%s[%s]" % (s, server.server_name, server_id)
            else:
                s = "%s[%s]" % (server.server_name, server_id)
        else:
            if s:
                s = "%s|%s[%s]" % (s, server_id, server_id)
            else:
                s = "%s[%s]" % (server_id, server_id)
    return s

@register.filter
def mail_target_detail(system_mail):
    target = system_mail.target
    if target == 1:
        return u"%s之前注册的用户" % system_mail.date
    elif target == 2:
        return u"用户ID为%s的用户" % system_mail.lodo_ids
    elif target == 3:
        return u"%s有登录的用户" % system_mail.date
    elif target == 4:
        return u"%s有充值的用户" % system_mail.date
    elif target == 5:
        partner = partner_business.get_partner(system_mail.partner_id)
        return u"渠道[%s]的所有用户" % partner.name
    
    return target

@register.filter
def command_report_detail(detail):
    if not detail:
        return ""
    try:
        infos = json.loads(detail)
        info_list = []
        for info in infos:
            info_list.append("[%s][%s][%s]" % (info[0], info[1].split("/")[-1], u"<font color='green'>成功</font>" if info[2] == 1 else u"<font color='green'>失败</font>"))
        return "<br/>".join(info_list)
    except:
        return detail

@register.filter
def server_zone_name(server_zone):
    return server_zone_map.get(server_zone, "未知")

@register.filter
def truncate_str(s):
    if not s:
        return s
    if len(s) > 30:
        s = s[:20] + '...' + s[-10:]
    return s

@register.filter
def system_mail_status(status):
    if status == 1:
        return "审核通过"
    elif status == 2:
        return "审核未通过 "
    else:
        return "未审核"

@register.filter
def mail_status(status):
    if status == 1:
        return "已读"
    elif status == 2:
        return "已经删除"
    else:
        return "未读"
    
@register.filter
def infos_amount_total(infos):
    total = 0
    for info in infos:
        total += info["amount"]
    return total
   
@register.filter 
def empty(s):
    if not s:
        return ""
    return s

@register.filter 
def ucenter_name(ucenter):
    if ucenter == "android":
        return u"android登录服"
    if ucenter == "ios":
        return u"ios登录服"