#!/usr/bin/python
# -*- coding: utf-8 -*-

import traceback
import redis

from ldsg_admin.model import StatUserLevel,StatUserLevelRank
from ldsg_admin.common.mysql import get_connection, connection
from ldsg_admin.business import server_business, partner_business
from ldsg_admin.common import get_logging, json
from ldsg_admin.common import server_util
from ldsg_admin.util.date_utils import get_date, get_tomorrow_str_by_str

logging = get_logging()

def get_user_level_stat(server_id, date):
    return StatUserLevel.load(server_id=server_id, date=date)

def get_user_level_rank_stat(server_id, date):
    return StatUserLevelRank.load(server_id=server_id, date=date)
    
'''获取某一服务器上所有渠道的用户注册信息'''
def get_user_reg_stat_all(server_id, start_date, end_date):
    user_reg_stat = []

    '''
        从管理后台数据库的 partner 表中查询出所有的渠道商。针对每个渠道商去 user_mapper 表中查询某一时间范围内的注册人数。
        针对这个些注册用户，用 user_id 去 user 表中看看能否查询到对应的记录，有的话说明用户注册后创建了角色，以此统计该渠道的创角人数
    '''
    partner_list = partner_business.get_all_partner_list()
    
    for partner in partner_list:
        partner_id = partner.partner_id
        data = get_user_reg_stat_by_pid(server_id, partner_id, start_date, end_date)
        if data:
            user_reg_stat.append(data)
    
    return user_reg_stat


def get_user_reg_stat_by_pid(server_id, partner_id, start_date, end_date):
    db_config = server_business.get_server_db_connect(server_id=server_id)
    connect = get_connection(db_config)
    cursor = connect.cursor()
    admin_db_cursor = connection.cursor()
    
    try:
     
        data = {}
        
        partner = admin_db_cursor.fetchone("SELECT * FROM partner WHERE partner_id='%s'" % partner_id)
        
        reg_num = cursor.fetchone("SELECT COUNT(user_id) AS reg_num FROM user_mapper WHERE partner_id='%s' AND created_time BETWEEN '%s' AND '%s'" % (partner_id, start_date, end_date))
        create_num = cursor.fetchone("SELECT COUNT(u.user_id) AS create_num FROM `user` u LEFT JOIN user_mapper m ON u.`user_id` = m.`user_id` WHERE m.`partner_id` = '%s' AND reg_time BETWEEN '%s' AND '%s'" % (partner_id,start_date, end_date))
        
        data['reg_num'] = int(reg_num['reg_num'])
        data['create_num']= int(create_num['create_num'])
        data['partner_name'] = partner['name']
        data['ratio'] = round(data['create_num']/ float(data['reg_num']), 4) * 100
                
    finally:
        cursor.close()
    return data

def get_user_payment_stat(server_id, start_date, end_date):
    
    start_date = "%s 00:00:00" % start_date
    end_date = "%s 23:59:59" % end_date
    
    db_config = server_business.get_server_db_connect(server_id=server_id)
    connect = get_connection(db_config)
    cursor = connect.cursor()
    
    user_payment_stat = []
    try:
        infos = cursor.fetchall("SELECT partner_id, sum(amount) AS total FROM payment_log WHERE created_time BETWEEN '%s' AND '%s' GROUP BY partner_id ORDER BY total DESC" % (start_date, end_date))
        #infos = infos.to_list()
        total = 0
        for info in infos:
            total += info["total"]
            user_payment_stat.append({"name": info["partner_id"], "total": info["total"]})
        user_payment_stat.append({"name": u"总计", "total": total})
    finally:
        cursor.close()
    
    return user_payment_stat

def get_user_online_stat(server_id):
    
    cursor = connection.cursor()
    
    today = get_date() + ' 00:00:00'
    tomorrow = get_tomorrow_str_by_str(today)
    
    try:
        user_online_stat = cursor.fetchall("SELECT * FROM stat_online WHERE server_id='%s' AND stat_time BETWEEN '%s' AND '%s'" % (server_id, today, tomorrow))
    finally:
        cursor.close()
    
    return user_online_stat

def get_user_draw_time_rank(server_id):
    db_config = server_business.get_server_db_connect(server_id=server_id)
    connect = get_connection(db_config)
    cursor = connect.cursor()
    try:
        sql = "select b.username, b.lodo_id, a.score from user_draw_time a left join user b on a.user_id = b.user_id order by a.score desc limit 50"
        return cursor.fetchall(sql)
    finally:
        cursor.close()
        
def get_user_draw_power_rank(server_id,date):
    db_config = server_business.get_server_db_connect(server_id=server_id)
    connect = get_connection(db_config)
    cursor = connect.cursor()
    try:
        sql = u"SELECT r.`rank_data` FROM rank_log r WHERE r.`rank_key` = 'ldsg_local_power_rank_key' AND r.`date` = '%s' " % (date)
        return cursor.fetchall(sql)
    finally:
        cursor.close()

def get_user_draw_pk_rank(server_id):
    db_config = server_business.get_server_db_connect(server_id=server_id)
    connect = get_connection(db_config)
    cursor = connect.cursor()
    try:
        sql = "SELECT u.`rank`,u.`score`,u.`username` FROM user_pk_info u ORDER BY u.`rank` LIMIT 30"
        return cursor.fetchall(sql)
    finally:
        cursor.close()
        
        
def get_user_use_gold_rank(server_id, start_date, end_date, min_amount):
    
    start_time = "%s 00:00:00" % start_date
    end_time = "%s 23:59:59" % end_date
    
    db_config = server_business.get_server_db_connect(server_id=server_id)
    connect = get_connection(db_config)
    cursor = connect.cursor()
    try:
        sql = u"select b.username, b.lodo_id, a.amount from (select sum(amount) as amount, user_id from user_gold_use_log where flag = - 1 and created_time >= '%s' and created_time <= '%s' group by user_id)a " \
                 "left join user b on a.user_id = b.user_id where a.amount >= %s order by a.amount desc " % (start_time, end_time, min_amount)
        logging.debug(sql)
        return cursor.fetchall(sql)
    finally:
        cursor.close()
        
def get_user_payment_rank(server_id, start_date, end_date, min_amount):
    
    start_time = "%s 00:00:00" % start_date
    end_time = "%s 23:59:59" % end_date
    
    db_config = server_business.get_server_db_connect(server_id=server_id)
    connect = get_connection(db_config)
    cursor = connect.cursor()
    try:
        sql = u"select b.username, b.lodo_id, a.amount, a.count, a.max_amount from (select sum(amount) as amount, count(1) as count, max(amount) as max_amount, user_id from payment_log where created_time >= '%s' and created_time <= '%s' group by user_id)a " \
                 "left join user b on a.user_id = b.user_id where a.amount >= %s order by a.amount desc " % (start_time, end_time, min_amount)
        logging.debug(sql)
        return cursor.fetchall(sql)
    finally:
        cursor.close()
        
def get_server_online(redis_connect):
    try:
        redis_cfg = server_util.str_to_redisconfig(redis_connect)
        logging.debug("host[%s], port[%s]" % (redis_cfg['host'], redis_cfg['port']))
        client = redis.Redis(host=redis_cfg['host'], port=redis_cfg['port'], socket_timeout=1)
        s = client.get("ldsg_local_system_status_key")
        if s:
            info = json.loads(s)
            online = info["connCount"]
            return online
    except:
        logging.debug(traceback.format_exc())
        pass
    return 0
        
def get_server_online_list(server_zone):
    server_list, _ = server_business.get_server_list(0, 10000, server_zone)
    server_online_infos = []
    for server in server_list:
        online = get_server_online(server.redis_connect)
        logging.debug("server:%s, online:%s" % (server.server_id, online))
        server_online_infos.append({"server_id": server.server_id, "count": online})
    return server_online_infos
        
if __name__ == "__main__":
    get_server_online_list(2)