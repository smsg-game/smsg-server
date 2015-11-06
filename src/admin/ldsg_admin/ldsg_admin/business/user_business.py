#!/usr/bin/python
# -*- coding: utf-8 -*-

from datetime import datetime 


from ldsg_admin.common.mysql import get_connection
from ldsg_admin.business import server_business
from ldsg_admin.common.mysql import connection
from ldsg_admin.common.net.post import Post
from ldsg_admin.common import json
from ldsg_admin.constants.tool_type import ToolType
from ldsg_admin.util import table_util, http_util
from ldsg_admin.common import get_logging

logging = get_logging()


OPEN_FORCES_SQL = "insert into %s(user_id, scene_id, forces_type, forces_id, status, created_time, updated_time, times) " \
                      "select (select user_id from user where user_id = '%s'), scene_id, forces_type, forces_id, 1, now(), now(), 0 from system_forces" \
                          " where forces_id not in (select forces_id from %s where user_id = (select user_id from user where user_id = '%s')) "

def open_all_scene(server_id, user_id):
    db_config = server_business.get_server_db_connect(server_id=server_id)
    table = "user_forces_%s" % table_util.get_table_index(user_id)
    
    connect = get_connection(db_config)
    cursor = connect.cursor()
    try:
        sql = OPEN_FORCES_SQL % (table, user_id, table, user_id)
        cursor.execute(sql)
    finally:
        cursor.close()
        
    return True

def update_user_level(server_id, user_id, level):
    level = int(level)
    db_config = server_business.get_server_db_connect(server_id=server_id)
    connect = get_connection(db_config)
    cursor = connect.cursor()
    try:
        info = cursor.fetchone("select exp from system_user_level where user_level = %s" % (level + 1))
        if info:
            exp = info["exp"] - 10
        else:
            exp = 10000 * 10000
        
        server = server_business.get_server(server_id)
        host = server.server_host
    
        path = "/gameApi/updateUserLevel.do"
        logging.debug("host:[%s], path[%s]" % (host, path))
        data = {"userId": user_id, "level": level, "exp": exp}
    
        post = Post(data, host, path, port=8088)
        content = post.submit(result_keyword=None, get_content=True, debug=True)
        try:
            info = json.loads(content)
            return info.get("rc", 3000) == 1000
        except:
            logging.debug("bad json format:[%s]" % content)
    finally:
        cursor.close()
    
def get_user_info(server_id, name):
    db_config = server_business.get_server_db_connect(server_id=server_id)
    connect = get_connection(db_config)
    cursor = connect.cursor()
    user_extinfo = None
    try:
        user_info = cursor.fetchone("select * from user where lodo_id='%s'" % name)
        if not user_info:
            user_info = cursor.fetchone("select * from user where username='%s'" % name)
        if user_info:
            user_extinfo = cursor.fetchone("select * from user_extinfo where user_id='%s'" % user_info["user_id"])
    finally:
        cursor.close()
        
    if user_info and user_extinfo:
        user_info = user_info.to_dict()
        reward_vip_level = user_extinfo["reward_vip_level"]
        vip_level = user_info["vip_level"]
        if vip_level < reward_vip_level:
            user_info["vip_level"] = reward_vip_level
        if user_info["is_banned"] and user_info["due_time"] > datetime.now():
            user_info["is_banned"] = 1
        else:
            user_info["is_banned"] = 0
        
    return user_info

def get_user_register(server_id,datetime):
    db_config = server_business.get_server_db_connect(server_id=server_id)
    connect = get_connection(db_config)
    cursor_curr = connection.cursor()
    cursor = connect.cursor()
    data = {}
    try:
        info = cursor_curr.fetchone("select 1 from stat_server_data where stat_date = '%s' and server_id = '%s' " % (datetime, server_id))
        total_user = cursor.fetchone("SELECT COUNT(distinct a.user_id) as total_user FROM `user` a , user_mapper b WHERE DATE(a.reg_time) <= '%s' AND a.`user_id` = b.`user_id`" % (datetime))
        data['total_user'] = int(total_user['total_user'])
        if info:
            temp = cursor_curr.fetchone("select create_user AS new_register,date_active from stat_server_data where stat_date = '%s' and server_id = '%s'" % (datetime, server_id))
            data['new_register'] = int(temp['new_register'])
            data['date_active'] = int(temp['date_active'])
        else:
            info = cursor.fetchone("SELECT COUNT(user_id) AS new_register FROM `user` WHERE DATE(reg_time) = '%s'" % (datetime))
            data['new_register'] = int(info['new_register'])
            active_user = 0
            for i in range(128):
                info = cursor.fetchone("select count(distinct user_id) as date_active from user_online_log_%d where date(login_time) = '%s';" % (i,datetime))
                active_user = active_user+int(info['date_active'])
            data['date_active'] = active_user
    finally:
        cursor.close()
        cursor_curr.close()
    data['active'] = round(data['date_active']/float(data['total_user']),5)*100
    return data
    

def get_user_hero_info(server_id, name):
    db_config = server_business.get_server_db_connect(server_id=server_id)
    connect = get_connection(db_config)
    cursor = connect.cursor()
    try:
        user_info = get_user_info(server_id, name)
        user_hero_compact_info = []

        if user_info:
            user_id = user_info['user_id']
            user_hero_info = cursor.fetchall("SELECT * FROM user_hero WHERE user_id='%s' order by hero_level DESC" % user_id)
            
            for info in user_hero_info:
                system_hero_id = info['system_hero_id']
                system_hero_info = cursor.fetchone("SELECT * FROM system_hero WHERE system_hero_id='%s'" % system_hero_id)
                if system_hero_info:
                    hero_name = system_hero_info['hero_name']

                 
                    data = {
                            'hero_name' : hero_name,
                            'hero_exp' : info['hero_exp'],
                            'hero_level' : info['hero_level'],
                            'system_hero': system_hero_info
                    }
                    
                    user_hero_compact_info.append(data)
                
    finally:
        cursor.close()
    
    return user_hero_compact_info


def get_user_package_info(server_id, name):
    db_config = server_business.get_server_db_connect(server_id=server_id)
    connect = get_connection(db_config)
    cursor = connect.cursor()
    try:
        user_info = get_user_info(server_id, name)
        user_package_compact_info = []
        
        if user_info:
            user_id = user_info['user_id']
            user_package_info = cursor.fetchall("SELECT * FROM user_tool WHERE user_id='%s'" % user_id)
            
            for info in user_package_info:
                tool_id = info['tool_id']
                system_tool = cursor.fetchone("SELECT * FROM system_tool WHERE tool_id='%s'" % tool_id)
                
                if system_tool:
                    tool_num = info['tool_num']
                    tool_name = system_tool['name']
                    data = {
                        'name' : tool_name,
                        'tool_num' : tool_num
                    }
                
                    user_package_compact_info.append(data)
    finally:
        cursor.close()
    return user_package_compact_info


def get_user_purchase_info(server_id, name, start_date, end_date):
    db_config = server_business.get_server_db_connect(server_id=server_id)
    connect = get_connection(db_config)
    cursor = connect.cursor()
    try:
        user_info = get_user_info(server_id, name)
        user_purchase_compact_info = []
        
        if user_info:
            user_id = user_info['user_id']
            user_purchase_info = cursor.fetchall("SELECT * FROM user_mall_log WHERE user_id='%s' AND created_time BETWEEN '%s' AND '%s'" % (user_id, start_date, end_date))
            
            for info in user_purchase_info:
                mall_id = info['mall_id']
                system_mall = cursor.fetchone("SELECT * FROM system_mall WHERE mall_id='%s'" % mall_id)
                
                if system_mall:
                    num = info['num']
                    created_time = info['created_time']
                    name = system_mall['name']
                    data = {
                        'name' : name,
                        'num' : num,
                        'created_time' : created_time
                    }
                    user_purchase_compact_info.append(data)
    finally:
        cursor.close()
        
    return user_purchase_compact_info

def get_user_gold_use_log(server_id, name, start_time, end_time, use_type=0, flag=0, page=1, pagesize=20, amount=0):
    """获取用户金币日志"""
    start = (page - 1)  * pagesize
    limit = pagesize
    
    db_config = server_business.get_server_db_connect(server_id=server_id)
    connect = get_connection(db_config)
    cursor = connect.cursor()
    total, infos = 0, []    
    try:
        user_info = cursor.fetchone("select * from user where lodo_id='%s'" % name)
        if not user_info:
            user_info = cursor.fetchone("select * from user where username='%s'" % name)
        if user_info:
            where = " user_id = '%s' " % user_info['user_id']
            if use_type:
                where += " and use_type = %s " % use_type
            if flag:
                where += " and flag = %s " % flag
            if start_time:
                where += " and created_time >= '%s' " % start_time
            if end_time:
                where += " and created_time <= '%s' " % end_time
            if amount:
                where += " and amount >= %s " % amount
            total = cursor.fetchone("select count(1) as total from user_gold_use_log where %s " % where)["total"]
            if total > 0:
                sql = "select * from user_gold_use_log where %s order by log_id desc limit %s, %s" % (where, start, limit)
                
                infos = cursor.fetchall(sql)
                
    finally:
        cursor.close()
        
    return total, infos

def get_user_hero_log(server_id, name, hero_name, start_time, end_time, use_type=0, flag=0, page=1, pagesize=20):
    """获取用户武将日志"""
    start = (page - 1)  * pagesize
    limit = pagesize
    
    db_config = server_business.get_server_db_connect(server_id=server_id)
    connect = get_connection(db_config)
    cursor = connect.cursor()
    total, infos = 0, []    
    try:
        user_info = cursor.fetchone("select * from user where lodo_id='%s'" % name)
        if not user_info:
            user_info = cursor.fetchone("select * from user where username='%s'" % name)
        if user_info:
            user_id = user_info['user_id']
            table = "user_hero_log_%s" % table_util.get_table_index(user_id)
            
            where = " user_id = '%s' " % user_info['user_id']
            if use_type:
                where += " and use_type = %s " % use_type
            if flag:
                where += " and flag = %s " % flag
            if start_time:
                where += " and created_time >= '%s' " % start_time
            if end_time:
                where += " and created_time <= '%s' " % end_time
            if hero_name:
                where += " and system_hero_id in (select system_hero_id from system_hero where hero_name like '%%%s%%') " % hero_name
            sql = "select count(1) as total from %s where %s " % (table, where)
            total = cursor.fetchone(sql)["total"]
            if total > 0:
                sql = "select * from %s where %s order by log_id desc limit %s, %s" % (table, where, start, limit)
                
                infos = cursor.fetchall(sql)
                
                hero_info_map = {}
                infos = infos.to_list()
                for info in infos:
                    system_hero_id = info["system_hero_id"]
                    if system_hero_id in hero_info_map:
                        hero_info = hero_info_map[system_hero_id]
                    else:
                        hero_info =  cursor.fetchone("select * from system_hero where system_hero_id = %s " % system_hero_id )
                    if hero_info:
                        hero_info_map[system_hero_id] = hero_info
                    info["hero_info"] = hero_info
                    
                
    finally:
        cursor.close()
        
    return total, infos

def get_user_level_up_log(server_id, name, page=1, pagesize=20):
    """获取用户升级日志"""
    start = (page - 1)  * pagesize
    limit = pagesize
    
    db_config = server_business.get_server_db_connect(server_id=server_id)
    connect = get_connection(db_config)
    cursor = connect.cursor()
    total, infos = 0, []    
    try:
        user_info = cursor.fetchone("select * from user where lodo_id='%s'" % name)
        if not user_info:
            user_info = cursor.fetchone("select * from user where username='%s'" % name)
        if user_info:
            user_id = user_info['user_id']
            table = "user_level_up_log"
            where = " user_id = '%s' " % user_id
            total = cursor.fetchone("select count(1) as total from %s where %s " % (table, where))["total"]
            if total > 0:
                sql = "select * from %s where %s order by log_id desc limit %s, %s" % (table, where, start, limit)
                
                infos = cursor.fetchall(sql)
                
                infos = infos.to_list()
                for info in infos:
                    info["username"] = user_info["username"]
                     
    finally:
        cursor.close()
        
    return total, infos

def get_user_payment_log(server_id, name, start_time, end_time, page=1, pagesize=20):
    """获取用户充值日志"""
    start = (page - 1)  * pagesize
    limit = pagesize
    
    db_config = server_business.get_server_db_connect(server_id=server_id)
    connect = get_connection(db_config)
    cursor = connect.cursor()
    total, infos = 0, []    
    try:
        user_info = cursor.fetchone("select * from user where lodo_id='%s'" % name)
        if not user_info:
            user_info = cursor.fetchone("select * from user where username='%s'" % name)
        if user_info:
            where = " user_id = '%s' " % user_info['user_id']
            if start_time:
                where += " and created_time >= '%s' " % start_time
            if end_time:
                where += " and created_time <= '%s' " % end_time
            total = cursor.fetchone("select count(1) as total from payment_log where %s " % where)["total"]
            if total > 0:
                sql = "select * from payment_log where %s order by payment_log_id desc limit %s, %s" % (where, start, limit)
                
                infos = cursor.fetchall(sql)
                
    finally:
        cursor.close()
        
    return total, infos

def get_tool_name(server_id, tool_type, tool_id):
    
    if tool_type == ToolType.TOOL_TYPE_HERO:
        return u"武将卡牌"
    elif tool_type == ToolType.TOOL_TYPE_EQUIP:
        return u"装备"
    elif tool_type == ToolType.TOOL_TYPE_EXP:
        return u"经验"
    elif tool_type == ToolType.TOOL_TYPE_POWER:
        return u"体力"
    else:
        sql = "select name from system_tool where tool_id = %s" % tool_id
        key = "name"
         
    db_config = server_business.get_server_db_connect(server_id=server_id)
    connect = get_connection(db_config)
    cursor = connect.cursor()
    try:
        info = cursor.fetchone(sql)
        if info:
            return info[key]
        else:
            return "[%s][%s]" % (tool_type, tool_id)
    finally:
        cursor.close
        
        
def get_user_tool_use_log(server_id, name, start_time, end_time, use_type=0, flag=0, page=1, pagesize=20, tool_type=0, tool_id=0):
    """获取用户道具日志"""
    start = (page - 1)  * pagesize
    limit = pagesize
    
    db_config = server_business.get_server_db_connect(server_id=server_id)
    connect = get_connection(db_config)
    cursor = connect.cursor()
    total, infos = 0, []    
    try:
        user_info = cursor.fetchone("select * from user where lodo_id='%s'" % name)
        if not user_info:
            user_info = cursor.fetchone("select * from user where username='%s'" % name)
        if user_info:
            user_id = user_info['user_id']
            table = "user_tool_use_log_%s" % table_util.get_table_index(user_id)
            where = " user_id = '%s' " % user_id
            if use_type:
                where += " and use_type = %s " % use_type
            if flag:
                where += " and flag = %s " % flag
            if tool_type:
                where += " and tool_type = %s " % tool_type
            if tool_id:
                where += " and tool_id = %s " % tool_id
            if start_time:
                where += " and created_time >= '%s' " % start_time
            if end_time:
                where += " and created_time <= '%s' " % end_time
            sql = "select count(1) as total from %s where %s " % (table, where)
            total = cursor.fetchone(sql)["total"]
            if total > 0:
                sql = "select * from %s where %s order by log_id desc limit %s, %s" % (table, where, start, limit)
                infos = cursor.fetchall(sql)
                infos = infos.to_list();
    finally:
        cursor.close()
        
    return total, infos

def update_user_vip_level(server_id, user_id, vip_level):
    info = {}
    info["userId"] = user_id
    info["vipLevel"] = vip_level

    server = server_business.get_server(server_id=server_id)
    url = "http://" + server.server_host + ":8088//gameApiAdmin/assignVipLevel.do"
    success, ret_val = http_util.request(url, info)
    if success:
        result = json.loads(ret_val)
        return result.get("rt") == 1000
    
    return False

def block_user(server_id, user_id, due_time):
    info = {}
    info["userId"] = user_id
    info["dueTime"] = due_time

    server = server_business.get_server(server_id=server_id)
    url = "http://" + server.server_host + ":8088//gameApiAdmin/banUser.do"
    success, ret_val = http_util.request(url, info)
    if success:
        result = json.loads(ret_val)
        return result.get("rt") == 1000
    
    return False
    

if __name__ == "__main__":
    s = u"abc"
    for d in s:
        print d