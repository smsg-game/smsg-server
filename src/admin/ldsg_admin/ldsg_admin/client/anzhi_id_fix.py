#!/usr/bin/python
# -*- coding: utf-8 -*-

from ldsg_admin.util import http_util
from ldsg_admin.common import json, server_util
from ldsg_admin.common import single_process
from ldsg_admin.common.mysql import get_connection
from ldsg_admin.client.base_client import BaseClient
from ldsg_admin.business import server_business, notice_business


class AnZhiIdHandle(BaseClient):
    
    CLIENT_NAME = "an_zhi_id_handle"
    
    def __init__(self):
        super(self.__class__, self).__init__(self.__class__.CLIENT_NAME)
            
    def run(self):
        
        f = open("/data/update.sql", "wb")
        try:
            servers, _ = server_business.get_server_list(1, 30, 2)
            for server in servers:
                db_config = server_util.str_to_dbconfig(server.db_connect)
                conn = get_connection(db_config)
                cursor = conn.cursor()
                try:
                    sql ="""select * from user_mapper where user_id in (select user_id from user where level <=5 and user_id in (select user_id from user_mapper where partner_user_id in (select partner_user_id from (select count(1) as total, partner_user_id from user_mapper where partner_id = 1010 group by partner_user_id)t where total > 1))) ;"""
                    infos = cursor.fetchall(sql)
                    for info in infos:
                        user_mapper_id = info["user_mapper_id"]
                        print user_mapper_id
                        f.write("update user_mapper set partner_user_id = CONCAT(user_id, '_',  partner_user_id) where user_mapper_id = %s limit 1;\n" % user_mapper_id)
                finally:
                    cursor.close()
        finally:
            f.close()
            pass
            
            
          
if __name__ == "__main__":
    s = single_process.SingleProcess("an_zhi_id_handle")
    s.check()
    executor = AnZhiIdHandle()
    executor.start()

    
