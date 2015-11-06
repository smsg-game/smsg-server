#!/usr/bin/python
# -*- coding: utf-8 -*-

import traceback
from datetime import datetime

from ldsg_admin.common.mysql import get_connection
from ldsg_admin.common import server_util
from ldsg_admin.common import json
from ldsg_admin.common import single_process
from ldsg_admin.client.base_client import BaseClient
from ldsg_admin.business import server_business
from ldsg_admin.model import StatUserLevel


class UserLevelStat(BaseClient):
    
    CLIENT_NAME = "user_level_stat"
    
    def __init__(self):
        super(self.__class__, self).__init__(self.__class__.CLIENT_NAME)
            
    def run(self):
        server_list = self.get_server_list()
        for server in server_list:
            try:
                self.total_server(server)
            except:
                self.log(traceback.format_exc())
                pass
            
    def total_server(self, server):
        """查询服务器"""
        db_config = server_util.str_to_dbconfig(server.db_connect)
        connect = get_connection(db_config)
        cursor = connect.cursor()
        level_map = {}
        try:
            start_time = None
            while True:
                where = "1=1"
                if start_time:
                    where =  "reg_time > '%s'" % start_time 
                sql = "select reg_time, level from user where %s order by reg_time asc limit 10000" % where
                    
                infos = cursor.fetchall(sql)
                if not infos:
                    break
                
                self.log("size:[%s], start_time[%s]" % (len(infos), start_time))
                start_time = infos[-1]["reg_time"]
                for info in infos:
                    level = info["level"]
                    total = level_map.get(level, 0)
                    total += 1
                    level_map[level] = total
                    
        finally:
            cursor.close()
            
        stat_user_level = StatUserLevel()
        stat_user_level.server_id = server.server_id
        stat_user_level.data = json.dumps(level_map)
        stat_user_level.date = datetime.now().strftime("%Y-%m-%d")
        stat_user_level.persist()
    
    def get_server_list(self):
        return server_business.get_all_server_list()
            
if __name__ == "__main__":
    s = single_process.SingleProcess("user_level_stat")
    s.check()
    executor = UserLevelStat()
    executor.start()

    
