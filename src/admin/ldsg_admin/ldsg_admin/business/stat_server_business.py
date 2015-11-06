#!/usr/bin/python
# -*- coding: utf-8 -*-

'''
Created on 2014-1-7

@author: Candon
'''
from ldsg_admin.common.mysql import connection
from ldsg_admin.util import date_utils
from ldsg_admin.client.stat.stat_server_data import StatServerDate
from ldsg_admin.model import StatLiucunData,StatLiucunDataDate
from ldsg_admin.model import Server
from ldsg_admin.constants.server_constant import server_zone_map

def get_stat_server(begindate, enddate=None):
    statServerDatas = []
    if begindate == date_utils.get_date():
        statServerDate = StatServerDate(False)
        statServerDate.start()
        statServerDatas = statServerDate.statServerDatass
    elif enddate is not None:
        sql = "SELECT server_name,server_id, sum(new_register) as new_register, sum(create_user) as create_user, sum(date_active) as date_active, sum(pay_people) as pay_people, sum(pay_money) as pay_money, sum(max_online) as max_online, sum(new_user_pay) as new_user_pay\
        , sum(old_user_pay) as old_user_pay, sum(new_payer) as new_payer, sum(old_payer) as old_payer  FROM stat_server_data where stat_date>= '%s' and stat_date<='%s' group by server_id;" % (begindate, enddate)
        cursor = connection.cursor()
        try:
            datas = cursor.fetchall(sql)
            for data in datas:
                statServerDatas.append(data)
        finally:
            cursor.close()
    else:
        sql = "SELECT * from stat_server_data where stat_date = '%s'" % begindate
        cursor = connection.cursor()
        try:
            datas = cursor.fetchall(sql)
            for data in datas:
                statServerDatas.append(data)
        finally:
            cursor.close()
        #statServerDatas = StatServerData.query(condition="stat_date ='%s'" % begindate)
    
    total = 0
    statServerDatas_s = []
    for s in statServerDatas:
        if not s:
            continue
        try:
            server_id = s["server_id"]
        except:
            server_id = s.server_id
        server = Server.load(server_id = server_id)
        try:
            if server and server.is_test == 0:
                statServerDatas_s.append(s)
                total += s["pay_money"]
            if s["pay_people"] != 0 and s["date_active"] != 0:
                value = round(s["pay_people"]/(s["date_active"]),5)*100
                s.temp = value
            else:
                s.temp = 0
        except:
            if server and server.is_test == 0:
                total += s.pay_money
            if s.pay_money != 0 and s.date_active != 0:
                value = round(s.pay_money/(s.date_active),5)*100
                s.temp = value
            else:
                s.temp = 0
    stat_server_datas_map = {}
    for s in statServerDatas_s:
        if not s:
            continue
        try:
            server_id = s["server_id"]
        except:
            server_id = s.server_id
        server = Server.load(server_id = server_id)
        server_zone = server.server_zone
        server_zone_server_list = stat_server_datas_map.get(server_zone, [])
        server_zone_server_list.append(s)
        stat_server_datas_map[server_zone] = server_zone_server_list
        
    items = stat_server_datas_map.items()
    items.sort()
    for k, values in stat_server_datas_map.iteritems():
        try:
            server_id = s["server_id"]
            values = sorted(values, cmp=lambda x, y : cmp(int(x["server_id"][1:]), int(y["server_id"][1:])))
        except:
            values = sorted(values, cmp=lambda x, y : cmp(int(x.server_id[1:]), int(y.server_id[1:])))
        stat_server_datas_map[k] = values
        
    #statServerDatas_s.sort(cmp=lambda x,y:cmp(int(x.server_id[1:]), int(y.server_id[1:])))
    return stat_server_datas_map, total, server_zone_map

def get_stat_server_country(begindate,enddate=None):
    statServerDatas = []
    if begindate == date_utils.get_date():
        statServerDate = StatServerDate(False)
        statServerDate.start()
        statServerDatas = statServerDate.statServerDatass
    elif enddate is not None:
        sql = "SELECT server_name,server_id,country_code, sum(new_register) as new_register, sum(create_user) as create_user, sum(date_active) as date_active, sum(pay_people) as pay_people, sum(pay_money) as pay_money, sum(max_online) as max_online, sum(new_user_pay) as new_user_pay\
        , sum(old_user_pay) as old_user_pay, sum(new_payer) as new_payer, sum(old_payer) as old_payer  FROM stat_server_data_country where stat_date>= '%s' and stat_date<='%s' group by server_id;" % (begindate, enddate)
        cursor = connection.cursor()
        try:
            datas = cursor.fetchall(sql)
            for data in datas:
                statServerDatas.append(data)
        finally:
            cursor.close()
    else:
        sql = "SELECT * from stat_server_data where stat_date = '%s'" % begindate
        cursor = connection.cursor()
        try:
            datas = cursor.fetchall(sql)
            for data in datas:
                statServerDatas.append(data)
        finally:
            cursor.close()
        #statServerDatas = StatServerData.query(condition="stat_date ='%s'" % begindate)
    
    total = 0
    statServerDatas_s = []
    for s in statServerDatas:
        if not s:
            continue
        try:
            server_id = s["server_id"]
        except:
            server_id = s.server_id
        server = Server.load(server_id = server_id)
        try:
            if server and server.is_test == 0:
                statServerDatas_s.append(s)
                total += s["pay_money"]
            if s["pay_people"] != 0 and s["date_active"] != 0:
                value = round(s["pay_people"]/(s["date_active"]),5)*100
                s.temp = value
            else:
                s.temp = 0
        except:
            if server and server.is_test == 0:
                total += s.pay_money
            if s.pay_money != 0 and s.date_active != 0:
                value = round(s.pay_money/(s.date_active),5)*100
                s.temp = value
            else:
                s.temp = 0
    stat_server_datas_map = {}
    for s in statServerDatas_s:
        if not s:
            continue
        try:
            server_id = s["server_id"]
        except:
            server_id = s.server_id
        server = Server.load(server_id = server_id)
        server_zone = server.server_zone
        server_zone_server_list = stat_server_datas_map.get(server_zone, [])
        server_zone_server_list.append(s)
        stat_server_datas_map[server_zone] = server_zone_server_list
        
    items = stat_server_datas_map.items()
    items.sort()
    for k, values in stat_server_datas_map.iteritems():
        try:
            server_id = s["server_id"]
            values = sorted(values, cmp=lambda x, y : cmp(int(x["server_id"][1:]), int(y["server_id"][1:])))
        except:
            values = sorted(values, cmp=lambda x, y : cmp(int(x.server_id[1:]), int(y.server_id[1:])))
        stat_server_datas_map[k] = values
        
    #statServerDatas_s.sort(cmp=lambda x,y:cmp(int(x.server_id[1:]), int(y.server_id[1:])))
    return stat_server_datas_map, total, server_zone_map

def get_stat_liucun():
    return StatLiucunData.query()

def get_stat_liucun_server(server_id):
    return StatLiucunDataDate.query(condition="server_id='%s'" % server_id)

    
    
