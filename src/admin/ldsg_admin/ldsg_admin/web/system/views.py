#!/usr/bin/python
# -*- coding: utf-8 -*-

from datetime import datetime

from ldsg_admin.web.render import render_to_response

from ldsg_admin.business import client_business, server_business

from ldsg_admin.common.decorators import login_required, privilege_required
from ldsg_admin.common import get_logging

logging = get_logging()

@login_required
@privilege_required(page_id=8, operator_id=4)
def client_list(request):
    infos = client_business.get_client_list()
    now = datetime.now()
    client_maps = {}
    for info in infos:
        last_report_time = info.last_report_time
        s = (now - last_report_time).seconds
        if s > 300:
            info.status = 0
    
        server_ids = info.server_ids
        if server_ids:
            server_id_list = server_ids.split(",")
            server = server_business.get_server(server_id_list[0])
            if server:
                server_zone = server.server_zone
                server_zone_client_list = client_maps.get(server_zone, [])  
                server_zone_client_list.append(info)      
                setattr(info, "sort", int(server_id_list[0][1:]))
                client_maps[server_zone] = server_zone_client_list
            
    for k, values in client_maps.iteritems():
        values = sorted(values, cmp=lambda x, y : cmp(x.sort, y.sort))
        client_maps[k] = values
        
            
    return render_to_response(request, "system/client_list.html", locals())

@login_required
def client_run_log_list(request):
    page = int(request.REQUEST.get("page", 1))
    client_id = int(request.REQUEST.get("client_id", 1))
    pagesize = int(request.REQUEST.get("pagesize", 10))
    infos, total =  client_business.get_client_run_log_list(client_id, page, pagesize)
    return render_to_response(request, "system/client_run_log_list.html", locals())

@login_required
@privilege_required(page_id=22, operator_id=4)
def battle_client_list(request):
    infos = client_business.get_battle_client_list()
    
    now = datetime.now()
    
    client_maps = {}
    for info in infos:
        last_report_time = info.last_report_time
        s = (now - last_report_time).seconds
        if s > 300:
            info.status = 0
        else:
            info.status = 1
    
        server_id = info.server_id
        
        if not server_id:
            server_id = "d1"
            
        server = server_business.get_server(server_id)
        if server:
            server_zone = server.server_zone
            server_zone_client_list = client_maps.get(server_zone, [])  
            server_zone_client_list.append(info)      
            setattr(info, "sort", int(server_id[1:]))
            client_maps[server_zone] = server_zone_client_list
            
    for k, values in client_maps.iteritems():
        values = sorted(values, cmp=lambda x, y : cmp(x.sort, y.sort))
        client_maps[k] = values
    
    return render_to_response(request, "system/battle_client_list.html", locals())