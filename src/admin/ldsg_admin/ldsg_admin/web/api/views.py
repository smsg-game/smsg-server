#-*- coding:utf-8 -*-

'''
Created on 2014-2-9

@author: Candon
'''

import time

from ldsg_admin.web.render import json_response
from ldsg_admin.common import get_logging, json
from ldsg_admin.business import api_business, server_business, client_business


logging = get_logging()

def tool_use_info(request):
    server_id = request.GET.get('server_id', "")
    date = request.GET.get('date','')
    logging.debug(u"查询道具消耗server_id[%s],date[%s]" % (server_id, date))
    data = {}
    if server_id and date:    
        infos = api_business.get_tool_use_info(server_id, date)
        for i in infos:
            if i.server_id in data.keys():
                temp = []
                temp.append(i.tool_type)
                temp.append(i.tool_id)
                temp.append(i.use_type)
                temp.append(i.tool_num)
                data[i.server_id].append(temp)
            else:
                data[i.server_id] = []
                temp = []
                temp.append(i.tool_type)
                temp.append(i.tool_id)
                temp.append(i.use_type)
                temp.append(i.tool_num)
                data[i.server_id].append(temp)
    else:
        data = {'rc':3000,'msg':"服务器ID和日期不能为空!"}
    return json_response(data)

def get_server_online(request):
    server_id = request.GET.get('server_id', "")
    logging.debug(u"查询在线人数server_id[%s]" % (server_id))
    data = {}
    if server_id:
        data = api_business.get_online(server_id)
    else:
        data = {'rc':3000,'msg':"服务器ID和日期不能为空!"}
    return json_response(data)

def get_server_list(request):
    partner_id = request.GET.get('partnerId', 1001)
    server_list = server_business.get_partner_server_list(partner_id)
    server_info_list = []
    for server in server_list:
        info = {}
        info["serverName"] = server["server_name"]
        info["serverPort"] = server["server_port"]
        info["status"] = server["server_status"]
        info["serverId"] = server["server_id"]
        info["openTime"] = int(time.mktime(server["open_time"].timetuple())) * 1000
        server_info_list.append(info)
    data = {"rc": 1000, "servers": json.dumps(server_info_list, False)}
    return json_response(data, False)
    
def battle_status_report(request):
    ip = request.META['REMOTE_ADDR']
    port = request.GET.get('port', 6379)
    logging.debug("battle_status_reportip[%s], port[%s]" % (ip, port))
    client_business.battle_status_report(ip, port)
    return json_response({"rc": 1000}, False)