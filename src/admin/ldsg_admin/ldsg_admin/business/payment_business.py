#!/usr/bin/python
# -*- coding: utf-8 -*-
"""
订单相关操作
"""

import time

from ldsg_admin.common import md5mgr, json
from ldsg_admin.util import http_util

from ldsg_admin.common.mysql import get_connection
from ldsg_admin.business import server_business
from ldsg_admin.constants.db_constant import ldsg_center_db_configs
from ldsg_admin.common import get_logging

logging = get_logging()

def fill_payment_order(ucenter, order_id, partner_order_id):
    info = {}
    timestamp = int(time.time() * 1000)
    info["timestamp"] = timestamp
    info["sign"] = md5mgr.mkmd5fromstr("%s%s%s%s" % (timestamp, order_id, partner_order_id, "$#@$%%eweqwlkfef"))
    info["orderNo"] = order_id
    info["partnerOrderId"] = partner_order_id


    if ucenter == "android":
        host = "wapi.android.3qchibi.com"
    elif ucenter == "wapi" or ucenter == "bapi":
        host = "wapi.ldsg.tkp.wartown.com.tw"
    elif ucenter == "ios":
        host = "wapi.ios.3qchibi.com"

    url = "http://" + host + ":8088//webApi/fillOrder.do"
    logging.debug(url)
    success, ret_val = http_util.request(url, info)
    logging.debug(ret_val)
    if success:
        logging.info(ret_val)
        result = json.loads(ret_val)
        return result.get("rc") == 1000
        
def get_payment_order_list(ucenter, name, server_id, page=1, pagesize=20):
    
    start = (page - 1)  * pagesize
    limit = pagesize
    
    ucenter_conn = get_connection(ldsg_center_db_configs[ucenter])
    ucenter_cursor = ucenter_conn.cursor()
    
    db_config = server_business.get_server_db_connect(server_id=server_id)
    game_conn = get_connection(db_config)
    game_cursor = game_conn.cursor()
    
    total, infos = 0, []
    try:
        user_info = game_cursor.fetchone("select * from user where lodo_id='%s'" % name)
        if not user_info:
            user_info = game_cursor.fetchone("select * from user where username='%s'" % name)
            
        if user_info:
            user_mapper_info = game_cursor.fetchone("select * from user_mapper where user_id = '%s'" % user_info["user_id"])
            
            partner_user_id = user_mapper_info["partner_user_id"]
            
            where = " server_id = '%s' and partner_user_id = '%s'" % (server_id, partner_user_id)
            
            total = ucenter_cursor.fetchone("select count(1) as total from payment_order where %s order by created_time desc " % where)["total"]

            if total > 0:
                sql = "select * from payment_order where %s order by created_time desc limit %s, %s" % (where, start, limit)
                infos = ucenter_cursor.fetchall(sql)
                infos = infos.to_list()
                for info in infos:
                    if not info["partner_order_id"]:
                        info["partner_order_id"] = ""
                    info["username"] = user_info["username"]
                    
            
    finally:
        ucenter_cursor.close()
        game_cursor.close()
        
    return total, infos


if __name__ == '__main__':
    pass