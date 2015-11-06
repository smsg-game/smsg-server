#!/usr/bin/python
# -*- coding: utf-8 -*-


from datetime import datetime

from ldsg_admin.business import stat_business
from ldsg_admin.web.render import render_to_response
from django.http import HttpResponse
from ldsg_admin.common import get_logging, json

logging = get_logging()
 
def user_level_stat(request):
    server_id = request.REQUEST.get('server_id', None)
    date = request.REQUEST.get('date', None)
    if not date:
        date = datetime.now().strftime("%Y-%m-%d")
    logging.debug(u"用户等级统计.date[%s], server_id[%s]" % (date, server_id))
    
    datas = {}
    
    if server_id:
        user_level_stat = stat_business.get_user_level_stat(server_id, date)
        if user_level_stat:
            stat_data = json.loads(user_level_stat.data)
            level_infos = []
            for i in range(1, 121):
                new_line = False
                if i % 10 == 0:
                    new_line = True
                level_infos.append({'level': i, 'total': stat_data.get(u'%s' % i, 0), 'new_line': new_line})
            datas['level_infos'] = level_infos
   
    datas['date'] = date 
    datas['server_id'] = server_id 
    
    return render_to_response(request, "stat/user_level_stat.html", datas)

def user_level_rank_stat(request):
    server_id = request.REQUEST.get('server_id', None)
    date = request.REQUEST.get('date', None)
    if not date:
        date = datetime.now().strftime("%Y-%m-%d")
    logging.debug(u"用户等级排名统计.date[%s], server_id[%s]" % (date, server_id))
    
    datas = {}
    
    if server_id:
        user_level_rank_stat = stat_business.get_user_level_rank_stat(server_id, date)
        if user_level_rank_stat:
            infos = json.loads(user_level_rank_stat.data)
   
    return render_to_response(request, "stat/user_level_rank_stat.html", locals())

def user_reg_stat(request):
    
    now = datetime.now()
    
    server_id = request.REQUEST.get('server_id', 0)
    partner_id = request.REQUEST.get('partner_id', 0)
    start_date = request.REQUEST.get('start_date', now.strftime("%Y-%m-%d"))
    end_date = request.REQUEST.get('end_date', now.strftime("%Y-%m-%d"))
    
    if server_id:
        if partner_id:
            user_reg_stat = []
            partner_id = int(partner_id)
            data = stat_business.get_user_reg_stat_by_pid(server_id, partner_id, start_date, end_date)
            user_reg_stat.append(data)
        else:
            user_reg_stat = stat_business.get_user_reg_stat_all(server_id, start_date, end_date)
    return render_to_response(request, "stat/user_reg_stat.html", locals())

def user_payment_stat(request):
    
    now = datetime.now()
    
    server_id = request.REQUEST.get('server_id', 0)
    start_date = request.REQUEST.get('start_date', now.strftime("%Y-%m-%d"))
    end_date = request.REQUEST.get('end_date', now.strftime("%Y-%m-%d"))
    
    if server_id:
        user_payment_stat = stat_business.get_user_payment_stat(server_id, start_date, end_date)
    return render_to_response(request, "stat/user_payment_stat.html", locals())

def user_online_stat(request):
    server_zone = int(request.REQUEST.get('server_zone', 2))
    
    if server_zone:
        infos = stat_business.get_server_online_list(server_zone)
    return render_to_response(request, "stat/user_online_stat.html", locals())
    
    
def user_draw_time_rank(request):
    
    server_id = request.REQUEST.get('server_id', None)
    to_excel = int(request.REQUEST.get('to_excel', 0))
    if server_id:
        infos = stat_business.get_user_draw_time_rank(server_id)
        if to_excel == 1:
            datas = []
            datas.append(u"排名,玩家名称,玩家ID,积分")
            for i, info in enumerate(infos):
                datas.append(u"%s,%s,%s,%s" % (i + 1, info["username"], info["lodo_id"], info["score"]))    
            response = HttpResponse("\n".join(datas), mimetype='application/vnd.ms-excel')
            response['Content-Disposition'] = 'attachment; filename=user_draw_rank_%s.csv' % server_id
            return response

    return render_to_response(request, "stat/user_draw_time_rank.html", locals())

def user_draw_power_rank(request):
    
    server_id = request.REQUEST.get('server_id', None)
    to_excel = int(request.REQUEST.get('to_excel', 0))
    now = datetime.now()
    date = request.REQUEST.get('start_date', now.strftime("%Y-%m-%d"))
    if server_id:
        infos = stat_business.get_user_draw_power_rank(server_id,date)
        for info in infos:
            jsoninfo = json.loads(info["rank_data"])
        if to_excel == 1:
            datas = []
            datas.append(u"排名,玩家名称,战力")
            for i, info in enumerate(jsoninfo):
                datas.append(u"%s,%s,%s" % (i + 1, info["username"], info["power"]))    
            response = HttpResponse("\n".join(datas), mimetype='application/vnd.ms-excel')
            response['Content-Disposition'] = 'attachment; filename=user_draw_rank_%s.csv' % server_id
            return response

    return render_to_response(request, "stat/user_draw_power_rank.html", locals())

def user_draw_pk_rank(request):
    server_id = request.REQUEST.get('server_id', None)
    to_excel = int(request.REQUEST.get('to_excel', 0))
    if server_id:
        infos = stat_business.get_user_draw_pk_rank(server_id)
        if to_excel == 1:
            datas = []
            datas.append(u"排名,玩家名称,玩家ID,积分")
            for i, info in enumerate(jsoninfo):
                datas.append(u"%s,%s,%s" % (i + 1, info["username"], info["score"]))    
            response = HttpResponse("\n".join(datas), mimetype='application/vnd.ms-excel')
            charset=UTF-8
            response['ContentType'] = 'application/vnd.ms-excel;charset=UTF-8'
            response['Content-Disposition'] = 'attachment; filename=user_draw_rank_%s.csv' % server_id
            return response

    return render_to_response(request, "stat/user_draw_pk_rank.html", locals())

def user_use_gold_rank(request):
    server_id = request.REQUEST.get('server_id', None)
    now = datetime.now()
    start_date = request.REQUEST.get('start_date', now.strftime("%Y-%m-%d"))
    end_date = request.REQUEST.get('end_date', now.strftime("%Y-%m-%d"))
    amount = request.REQUEST.get('amount', 0)
    to_excel = int(request.REQUEST.get('to_excel', 0))
    if server_id:
        infos = stat_business.get_user_use_gold_rank(server_id, start_date, end_date, amount)
        if to_excel == 1:
            datas = []
            datas.append(u"排名,玩家名称,玩家ID,消耗元宝")
            for i, info in enumerate(infos):
                datas.append(u"%s,%s,%s,%s" % (i + 1, info["username"], info["lodo_id"], info["amount"]))    
            response = HttpResponse("\n".join(datas), mimetype='application/vnd.ms-excel')
            response['Content-Disposition'] = 'attachment; filename=user_gold_use_rank_%s.csv' % server_id
            return response

    return render_to_response(request, "stat/user_use_gold_rank.html", locals())

def user_payment_rank(request):
    server_id = request.REQUEST.get('server_id', None)
    now = datetime.now()
    start_date = request.REQUEST.get('start_date', now.strftime("%Y-%m-%d"))
    end_date = request.REQUEST.get('end_date', now.strftime("%Y-%m-%d"))
    amount = request.REQUEST.get('amount', 0)
    to_excel = int(request.REQUEST.get('to_excel', 0))
    if server_id:
        infos = stat_business.get_user_payment_rank(server_id, start_date, end_date, amount)
        if to_excel == 1:
            datas = []
            datas.append(u"排名,玩家名称,玩家ID,充值金额")
            for i, info in enumerate(infos):
                datas.append(u"%s,%s,%s,%s" % (i + 1, info["username"], info["lodo_id"], info["amount"]))    
            response = HttpResponse("\n".join(datas), mimetype='application/vnd.ms-excel')
            response['Content-Disposition'] = 'attachment; filename=user_payment_rank_%s.csv' % server_id
            return response

    return render_to_response(request, "stat/user_payment_rank.html", locals())
