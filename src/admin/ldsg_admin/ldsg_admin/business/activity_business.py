#!/usr/bin/python
# -*- coding: utf-8 -*-

'''
Created on 2014-1-13

@author: Candon
'''

import uuid

from ldsg_admin.common.mysql import get_connection, connection
from ldsg_admin.common.net.post import Post
from ldsg_admin.util import http_util
from ldsg_admin.business import server_business
from ldsg_admin.model import Server, SystemToolExchange, SystemToolExchangeDetail, SystemTool, SystemPayReward, SystemPayRewardDetail, SystemMallDiscountActivity, \
                   SystemMallDiscountActivityDetail
from ldsg_admin.common import get_logging, json
import traceback

logging = get_logging()

def get_activity_list(server_id):
    
    ''' 跟住服务器ID拿活动列表'''
    
    db_config = server_business.get_server_db_connect(server_id=server_id)
    connect = get_connection(db_config)
    cursor = connect.cursor()
    try:
        activity_list = cursor.fetchall("select * from system_activity")
    finally:
        cursor.close()
    return activity_list

def add_activity(server_id,activity_info):
    server = Server.load(server_id=server_id)
    url = "http://" + server.server_host + ":8088//gameApi//modifyActivity.do"
    _, ret_val = http_util.request(url, activity_info)
    return ret_val
    
def add_system_tool_exchange(eid, name, server_ids, username):
    """
    @param eid: 配置id
    @param name: 配置名称
    """
    eid = int(eid)
    systemToolExchange = SystemToolExchange()
    systemToolExchange.id = eid
    systemToolExchange.name = name
    systemToolExchange.server_ids = server_ids
    if not eid:
        systemToolExchange.created_user_id = username
    systemToolExchange.persist()
    
def add_system_pay_reward(rid, name, reward_type, server_ids, username):
    """
    @param eid: 配置id
    @param name: 配置名称
    """
    rid = int(rid)
    system_pay_reward = SystemPayReward()
    system_pay_reward.id = rid
    system_pay_reward.name = name
    system_pay_reward.server_ids = server_ids
    if not rid:
        system_pay_reward.created_user_name = username
    system_pay_reward.reward_type = reward_type
    system_pay_reward.persist()
    
def add_system_mall_discount(did, name, start_time, end_time, server_ids, username):
    """
    @param eid: 配置id
    @param name: 配置名称
    """
    did = int(did)
    info = SystemMallDiscountActivity()
    info.id = did
    info.name = name
    info.server_ids = server_ids
    if not did:
        info.created_user_name = username
        info.activity_id = str(uuid.uuid4()).replace("-","")
    info.start_time = start_time
    info.end_time = end_time
    
    info.persist()
    
def get_system_tool_exchange_list(page, pagesize, username=None):
    """
    获取道具兑换列表
    """
    where = "1=1"
    if username:
        where = " created_user_id = '%s'" % username
    return SystemToolExchange.paging(page, pagesize, condition=where, order="created_time desc")

def get_system_pay_reward_list(page, pagesize, reward_type, username=None):
    """
    获取单次充值列表
    """
    condition = "reward_type=%s"
    if username:
        condition += " and created_user_name = '%s'" % username
    return SystemPayReward.paging(page, pagesize, condition=condition % reward_type, order="created_time desc")

def get_system_mall_discount_list(page, pagesize):
    """
    获取单次充值列表
    """
    return SystemMallDiscountActivity.paging(page, pagesize, order="created_time desc")

def get_system_tool_exchange(eid):
    """
    获取道具兑换
    """
    return SystemToolExchange.load(id=eid)

def get_system_pay_reward(rid):
    """
    获取单次充值兑换
    """
    return SystemPayReward.load(id=rid)

def get_system_tool_exchange_detail_list(eid):
    """
    获取道具兑换列表
    """
    return SystemToolExchangeDetail.query(condition="system_tool_exchange_id=%s" % eid, order="id asc")

def get_system_pay_reward_detail_list(rid):
    """
    获取充值道具列表
    """
    return SystemPayRewardDetail.query(condition="system_pay_reward_id=%s" % rid, order="id asc")
    
def get_system_mall_discount(did):
    """
    获取充值道具列表
    """
    return SystemMallDiscountActivity.load(id=did)
    
def get_system_mall_discount_detail_list(did):
    """
    获取充值道具列表
    """
    return SystemMallDiscountActivityDetail.query(condition="system_mall_discount_activity_id=%s" % did, order="id asc")
    
def data_sync(server_id, sync_type=1, table=None, sqls=[]):
    """数据同步"
    @param sync_type: 同步类型1:修改 ;2:新增 ;3:删除;4:全表同步 
    @param table:同步的表
    @param sqls:同步的sql
    """
    server = Server.load(server_id=server_id)
    host = server.server_host

    path = "/gameApi/dataSync.do"
    logging.debug("host:[%s], path[%s]" % (host, path))
    data = {"syncType": sync_type, "table": table, "sqls": json.dumps(sqls)}

    post = Post(data, host, path, port=8088)
    content = post.submit(result_keyword=None, get_content=True, debug=True)
    try:
        info = json.loads(content)
        return info.get("rc", 3000) == 1000
    except:
        logging.debug("bad json format:[%s]" % content)
        return False
    
def save_system_tool_exchange_detail(eid, infos):
    cursor = connection.cursor()
    try:
        cursor.execute("delete from system_tool_exchange_detail where system_tool_exchange_id = %s" % eid)
        cursor.insert(infos, "system_tool_exchange_detail")
    finally:
        cursor.close()
        
def save_system_pay_reward_detail(rid, infos):
    cursor = connection.cursor()
    try:
        cursor.execute("delete from system_pay_reward_detail where system_pay_reward_id = %s" % rid)
        cursor.insert(infos, "system_pay_reward_detail")
    finally:
        cursor.close()
        
def save_system_mall_discount_detail(did, infos):
    cursor = connection.cursor()
    try:
        cursor.execute("delete from system_mall_discount_activity_detail where system_mall_discount_activity_id = %s" % did)
        cursor.insert(infos, "system_mall_discount_activity_detail")
    finally:
        cursor.close()
        
def sync_system_tool_exchange(eid):
    system_tool_exchange = get_system_tool_exchange(eid)
    system_tool_exchange_list = get_system_tool_exchange_detail_list(eid)
    success = 0
    content= ""
    try:
        sqls =[]
        sqls.append({"sql": "truncate table $table;"})
        for i, info in enumerate(system_tool_exchange_list):
            sql = "insert into $table values(%s, '%s', '%s', '', %s);" % (i + 1, info.pre_exchange_items, info.post_exchange_items, info.times)
            sqls.append({"sql": sql})
            
        server_list = system_tool_exchange.server_ids.split(",")
        for server_id in server_list:
            if data_sync(server_id, 4, "system_tool_exchange", sqls):
                content = "%s<br/>%s" % (content, u"%s:<font color='green'>成功</font>" % server_id)
            else:
                content = "%s<br/>%s" % (content, u"%s:<font color='red'>失败</font>" % server_id)
                
        success = 1
    except:
        logging.error(traceback.format_exc())
    
    return success, content

def sync_system_pay_reward(rid):
    system_pay_reward = get_system_pay_reward(rid)
    system_pay_reward_detail_list = get_system_pay_reward_detail_list(rid)
    success = 0
    if system_pay_reward.reward_type == 1:
        table = "system_once_pay_reward"
    else:
        table = "system_total_pay_reward"
    content= ""
    try:
        sqls =[]
        sqls.append({"sql": "truncate table $table;"})
        for i, info in enumerate(system_pay_reward_detail_list):
            sql = "insert into $table values(%s, %s, %s, '%s', '%s');" % (i + 1, info.pay_money, info.times_limit, info.tool_ids_name, info.tool_ids)
            sqls.append({"sql": sql})
            
        server_list = system_pay_reward.server_ids.split(",")
        for server_id in server_list:
            if data_sync(server_id, 4, table, sqls):
                content = "%s<br/>%s" % (content, u"%s:<font color='green'>成功</font>" % server_id)
            else:
                content = "%s<br/>%s" % (content, u"%s:<font color='red'>失败</font>" % server_id)
                
        success = 1
    except:
        logging.error(traceback.format_exc())
    
    return success, content

def sync_system_mall_discount(did):

    system_mall_discount = get_system_mall_discount(did)
    system_mall_discount_detail = get_system_mall_discount_detail_list(did)
    success = 0
    content= ""
    try:
        sql_main =[]
        sql_detail = []
        
        sql_main.append({"sql": "truncate table $table;"})
        sql_main.append({"sql": "insert into $table values(1, '%s', '%s', '%s', '');" % \
                            (system_mall_discount.activity_id, system_mall_discount.start_time, system_mall_discount.end_time)})
        sql_detail.append({"sql": "truncate table $table;"})
        
        for _, info in enumerate(system_mall_discount_detail):
            sql = "insert into $table values('%s', %s, %s);" % (system_mall_discount.activity_id, info.mall_id, info.discount)
            sql_detail.append({"sql": sql})
            
        server_list = system_mall_discount.server_ids.split(",")
        for server_id in server_list:
            result = True
            if not data_sync(server_id, 4, "system_mall_discount_activity", sql_main):
                result = False
            if not data_sync(server_id, 4, "system_mall_discount_items", sql_detail):
                result = False
                
            if result:
                content = "%s<br/>%s" % (content, u"%s:<font color='green'>成功</font>" % server_id)
            else:
                content = "%s<br/>%s" % (content, u"%s:<font color='red'>失败</font>" % server_id)
                
        success = 1
    except:
        logging.error(traceback.format_exc())
    
    return success, content
    
def get_tool_list(name):
    return SystemTool.query(condition="tool_name like '%%%s%%'" % name, limit=20)
    
def get_system_mall_list(server_id):
    
    ''' 跟住服务器ID拿商品列表'''
    
    db_config = server_business.get_server_db_connect(server_id=server_id)
    connect = get_connection(db_config)
    cursor = connect.cursor()
    try:
        system_mall_list = cursor.fetchall("select * from system_mall where tag in (1, 2)")
    finally:
        cursor.close()
    return system_mall_list
    
    
if __name__ == "__main__":
    print sync_system_pay_reward(7)