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
        
        f_sql_1 = open("/data/game_fix.sql", "wb")
        f_sql_2 = open("/data/web_fix.sql", "wb")
        try:
            servers, _ = server_business.get_server_list(1, 30, 2)
            for server in servers:
                db_config = server_util.str_to_dbconfig(server.db_connect)
                conn = get_connection(db_config)
                cursor = conn.cursor()
                try:
                    infos = cursor.fetchall("select partner_user_id, partner_id from user_mapper where partner_id in (1020, 1010) ")
                    for info in infos:
                        partner_user_id_old = info["partner_user_id"]
                        partner_id = info["partner_id"]
                        partner_user_id_new = self.get_partner_user_id(partner_user_id_old)
                        if partner_user_id_new:
                            self.log("old[%s], new[%s]" % (partner_user_id_old, partner_user_id_new))
                            f_sql_1.write("update user_mapper set partner_user_id='%s' where partner_user_id='%s' and partner_id=%s limit 1;\n" % (partner_user_id_new, partner_user_id_old, partner_id))
                            f_sql_1.write("update payment_log set partner_user_id='%s' where partner_user_id='%s' and partner_id=%s limit 1;\n" % (partner_user_id_new, partner_user_id_old, partner_id))
                            f_sql_2.write("update payment_order set partner_user_id='%s' where partner_user_id='%s' and partner_id=%s limit 1;\n" % (partner_user_id_new, partner_user_id_old, partner_id))
                            
                finally:
                    cursor.close()
        finally:
            f_sql_1.close()
            f_sql_2.close()
            
    def get_partner_user_id(self, username):

        url = "http://42.62.4.167:9015/web/api/sdk/third/1/queryuidbyloginname"
        success, ret_val = http_util.request(url, {"param": username})
        if success:
            try:
                _ = json.loads(ret_val)
                return None
            except:
                return ret_val
            
          
if __name__ == "__main__":
    s = single_process.SingleProcess("an_zhi_id_handle")
    s.check()
    executor = AnZhiIdHandle()
    executor.start()

    
