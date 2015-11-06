#!/usr/bin/python
# -*- coding: utf-8 -*-
'''
Created on 2014-1-7

@author: Candon
'''
import redis
from ldsg_admin.common import json
from ldsg_admin.business import server_business
from ldsg_admin.common.server_util import str_to_redisconfig
from ldsg_admin.common.mysql import connection
from ldsg_admin.common import get_logging
from ldsg_admin.util.date_utils import get_time

logging = get_logging()

def main():
    """main"""
    servers = server_business.get_all_server_list()
    date = get_time()
    for server in servers:
        if not server:
            continue
        redis_cfg = str_to_redisconfig(server.redis_connect)
        logging.debug("host[%s], port[%s]" % (redis_cfg['host'], redis_cfg['port']))
        client = redis.Redis(host=redis_cfg['host'], port=redis_cfg['port'])
        s = client.get("ldsg_local_system_status_key")
        if s:
            info = json.loads(s)
            online = info["connCount"]
            cursor = connection.cursor()
            try:
                cursor.execute("INSERT INTO stat_online(online, stat_time,server_id) VALUES(%d, '%s','%s')" % (online, date, server.server_id))
            finally:
                connection.close()
if __name__ == "__main__":
    main()
