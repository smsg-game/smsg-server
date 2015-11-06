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
        
        
        qa_db_config = server_business.get_server_db_connect(server_id="t2")
        qa_conn = get_connection(qa_db_config)
        qa_cursor = qa_conn.cursor()
        infos = qa_cursor.fetchall("show tables")
        tables = []
        for info in infos:
            table = info["Tables_in_ldsg_t2"]
            tables.append(table)
        
        try:
            servers, _ = server_business.get_server_list(1, 30, 2)
            for server in servers:
                db_config = server_util.str_to_dbconfig(server.db_connect)
                conn = get_connection(db_config)
                cursor = conn.cursor()
                try:
                    for table in tables:
                        try:
                            info = cursor.fetchone("select 1 from %s limit 1" % table)
                        except:
                            print "error:server:%s,table:%s" % (server.server_id, table)
                           
                finally:
                    cursor.close()
        finally:
            pass
            
            
          
if __name__ == "__main__":
    
    for i in range(1, 25):
        qa_db_config = server_business.get_server_db_connect(server_id="h%s" % i)
        qa_conn = get_connection(qa_db_config)
        qa_cursor = qa_conn.cursor()
        print qa_cursor.fetchone("select count(1) as count from user_level_up_log")["count"]

    
