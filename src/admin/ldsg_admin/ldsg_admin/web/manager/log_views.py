#!/usr/bin/python
# -*- coding: utf-8 -*-


from datetime import datetime, timedelta

from ldsg_admin.web.render import render_to_response

from ldsg_admin.business import user_business
from ldsg_admin.common import get_logging
from ldsg_admin.constants.tool_type import ToolType
from ldsg_admin.constants.tool_use_type import tool_use_type_map
from ldsg_admin.common.decorators import login_required

logging = get_logging()
 
@login_required
def user_gold_use_log(request):
    
    now = datetime.now()
    yesterday = now - timedelta(days=1)
    
    name = request.REQUEST.get('name', "")
    page = int(request.REQUEST.get('page', 1))
    pagesize = int(request.REQUEST.get('pagesize', 20))
    server_id = request.REQUEST.get('server_id', '')
    use_type = int(request.REQUEST.get('use_type', 0))
    flag = int(request.REQUEST.get('flag', 0))
    amount = request.REQUEST.get('amount', 0)
    start_time = request.REQUEST.get('start_time', yesterday.strftime("%Y-%m-%d 00:00:00"))
    end_time = request.REQUEST.get('end_time', now.strftime("%Y-%m-%d 00:00:00"))
    logging.debug(u"查询用户金币日志.name[%s], server_id[%s]" % (name, server_id))
    if name and server_id:    
        total, infos = user_business.get_user_gold_use_log(server_id, name, start_time, end_time, use_type, flag, page, pagesize, amount)
    else:
        total, infos = 0, []
        
    use_type_list = set()
    for info in infos:
        use_type_list.add(info["use_type"])
        
    return render_to_response(request, "manager/user_gold_use_log.html", locals())

@login_required
def user_tool_use_log(request):
    
    now = datetime.now()
    yesterday = now - timedelta(days=1)
    
    name = request.REQUEST.get('name', "")
    page = int(request.REQUEST.get('page', 1))
    pagesize = int(request.REQUEST.get('pagesize', 20))
    server_id = request.REQUEST.get('server_id', '')
    use_type = int(request.REQUEST.get('use_type', 0))
    flag = int(request.REQUEST.get('flag', 0))
    tool_type = int(request.REQUEST.get('tool_type', 0))
    tool_id = int(request.REQUEST.get('tool_id', 0))
    start_time = request.REQUEST.get('start_time', yesterday.strftime("%Y-%m-%d 00:00:00"))
    end_time = request.REQUEST.get('end_time', now.strftime("%Y-%m-%d 00:00:00"))
    logging.debug(u"查询用户道具日志.name[%s], server_id[%s]" % (name, server_id))
    if name and server_id:    
        total, infos = user_business.get_user_tool_use_log(server_id, name, start_time, end_time, use_type, flag, page, pagesize, tool_type, tool_id)
    else:
        total, infos = 0, []
        
    #use_type_list = set()
    tool_type_list = set()
    tool_id_list = set()
    tool_id_infos = []
    for info in infos:
        info_tool_id = info["tool_id"]
        info_tool_type = info["tool_type"]
        #use_type_list.add(info["use_type"])
        tool_type_list.add(info["tool_type"])
        tool_name= user_business.get_tool_name(server_id, info_tool_type, info_tool_id)
        info["tool_name"] = tool_name
        if info_tool_type not in (ToolType.TOOL_TYPE_HERO, ToolType.TOOL_TYPE_EQUIP):
            if info_tool_id not in tool_id_list:
                tool_id_list.add(info_tool_id)
                tool_id_infos.append({"tool_id": info_tool_id, "tool_name": tool_name})
                
    use_type_list = tool_use_type_map.keys()
        
    return render_to_response(request, "manager/user_tool_use_log.html", locals())

@login_required
def user_hero_log(request):
    
    now = datetime.now()
    yesterday = now - timedelta(days=1)
    
    name = request.REQUEST.get('name', "")
    page = int(request.REQUEST.get('page', 1))
    pagesize = int(request.REQUEST.get('pagesize', 20))
    server_id = request.REQUEST.get('server_id', '')
    use_type = int(request.REQUEST.get('use_type', 0))
    flag = int(request.REQUEST.get('flag', 0))
    hero_name = request.REQUEST.get('hero_name', "")
    start_time = request.REQUEST.get('start_time', yesterday.strftime("%Y-%m-%d 00:00:00"))
    end_time = request.REQUEST.get('end_time', now.strftime("%Y-%m-%d 00:00:00"))
    logging.debug(u"查询用户武将日志.name[%s], server_id[%s]" % (name, server_id))
    if name and server_id:    
        total, infos = user_business.get_user_hero_log(server_id, name, hero_name, start_time, end_time, use_type, flag, page, pagesize)
    else:
        total, infos = 0, []
        
    use_type_list = tool_use_type_map.keys()
        
    return render_to_response(request, "manager/user_hero_log.html", locals())
    
@login_required
def user_level_up_log(request):
    
    now = datetime.now()
    yesterday = now - timedelta(days=1)
    
    name = request.REQUEST.get('name', "")
    page = int(request.REQUEST.get('page', 1))
    pagesize = int(request.REQUEST.get('pagesize', 20))
    server_id = request.REQUEST.get('server_id', '')
    logging.debug(u"查询用户升级日志.name[%s], server_id[%s]" % (name, server_id))
    if name and server_id:    
        total, infos = user_business.get_user_level_up_log(server_id, name, page, pagesize)
    else:
        total, infos = 0, []

    return render_to_response(request, "manager/user_level_up_log.html", locals())
    
@login_required
def user_payment_log(request):
    
    now = datetime.now()
    yesterday = now - timedelta(days=1)
    
    name = request.REQUEST.get('name', "")
    page = int(request.REQUEST.get('page', 1))
    pagesize = int(request.REQUEST.get('pagesize', 20))
    server_id = request.REQUEST.get('server_id', '')
    start_time = request.REQUEST.get('start_time', yesterday.strftime("%Y-%m-%d 00:00:00"))
    end_time = request.REQUEST.get('end_time', now.strftime("%Y-%m-%d 00:00:00"))
    logging.debug(u"查询用户充值日志.name[%s], server_id[%s]" % (name, server_id))
    if name and server_id:    
        total, infos = user_business.get_user_payment_log(server_id, name, start_time, end_time, page, pagesize)
    else:
        total, infos = 0, []
        
    return render_to_response(request, "manager/user_payment_log.html", locals())
