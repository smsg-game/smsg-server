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
from ldsg_admin.model import StatUserLevelRank


class UserLevelRankStat(BaseClient):
    
    CLIENT_NAME = "user_level_rank_stat"
    
    def __init__(self):
        super(self.__class__, self).__init__(self.__class__.CLIENT_NAME)
            
    def run(self):
        server_list = self.get_server_list()
        for server in server_list:
            try:
                self.log("start total server[%s]" % server.server_id)
                self.total_server(server)
            except:
                self.log(traceback.format_exc())
                pass
            
    def total_server(self, server):
        """查询服务器"""
        db_config = server_util.str_to_dbconfig(server.db_connect)
        connect = get_connection(db_config)
        cursor = connect.cursor()
        infos = None
        try:
            sql = "select lodo_id, username, level, exp from user order by level desc, exp desc limit 100"
            infos = cursor.fetchall(sql)
            infos = infos.to_list()
        finally:
            cursor.close()
            
        stat_user_level_rank = StatUserLevelRank()
        stat_user_level_rank.server_id = server.server_id
        stat_user_level_rank.data = json.dumps(infos)
        stat_user_level_rank.date = datetime.now().strftime("%Y-%m-%d")
        stat_user_level_rank.persist()
    
    def get_server_list(self):
        return server_business.get_all_server_list()
            
if __name__ == "__main__":
    s = single_process.SingleProcess("user_level_stat")
    s.check()
    executor = UserLevelRankStat()
    executor.start()

    
