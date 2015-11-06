#!/usr/bin/python
# -*- coding: utf-8 -*-


from ldsg_admin.client.base_client import BaseClient
import redis
import json
from ldsg_admin.stat.common.mysql.connection_wrapper import Connection
import sys
from ldsg_admin.stat.config import game_src_db_cfg, game_redis_cfg

class UserOnlineStat(BaseClient):
    
    CLIENT_NAME = "user_online_stat"
    
    def __init__(self):
        super(self.__class__, self).__init__(self.__class__.CLIENT_NAME)
        
    def run(self):
        pass
    
    def collect_info(self, s_key):
        redis_cfg = game_redis_cfg[s_key]
        client = redis.Redis(host=redis_cfg['HOST'], port=redis_cfg['PORT'])
        s = client.get("ldsg_local_system_status_key")
        if s:
            info = json.loads(s)
            online = info["connCount"]
            
            game_db = game_src_db_cfg[s_key]
            connection = self.get_conn(game_db['HOST'], game_db['PORT'], game_db['DB'], game_db['USER'], game_db['PASSWD'], game_db['CHARSET'])
            cursor = connection.cursor()
            try:
                cursor.execute("INSERT INTO online_stat(online, stat_time) VALUES(%s, now())" % online)
            finally:
                connection.close()

    def get_conn(self, host, port, db, user, passwd, charset="utf8"):
        return Connection(host=host, port=port, db=db, user=user, passwd=passwd, charset=charset)