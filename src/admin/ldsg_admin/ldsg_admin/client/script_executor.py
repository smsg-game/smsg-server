#!/usr/bin/python
# -*- coding: utf-8 -*-

import os
import traceback
import shutil
import time
from datetime import datetime
from _mysql_exceptions import OperationalError

from ldsg_admin.common.mysql import get_connection
from ldsg_admin.common import md5mgr
from ldsg_admin.common import file_utility, single_process
from ldsg_admin.client.base_client import BaseClient
from ldsg_admin.constants import db_configs
from ldsg_admin.config import PathSettings
from ldsg_admin.config import DB_Mysql


class ScriptExecutor(BaseClient):
    
    CLIENT_NAME = "script_executor"
    
    def __init__(self, batch_count=1000):
        super(self.__class__, self).__init__(self.__class__.CLIENT_NAME)
        self.batch_count = batch_count
    
    def db_log(self, info):
        conn = get_connection(DB_Mysql)
        cursor = conn.cursor()
        try:
            cursor.insert(info, "script_execute_log")
        finally:
            cursor.close()
            
    def backup(self, file_name):
        dir_name = os.path.join(PathSettings.SCRIPT_BACKUP, datetime.now().strftime("%Y%m%d"))
        if not os.path.exists(dir_name):
            os.makedirs(dir_name)
        name = file_name.split(os.path.sep)[-1]
        self.log("dir_name[%s]" % dir_name)
        target = os.path.join(dir_name, name.replace(".sql", ".done"))
        self.log("mvoe[%s] to[%s]" % (file_name, target))
        shutil.move(file_name, target)
            
    def check(self, file_name):
        
        conn = get_connection(DB_Mysql)
        cursor = conn.cursor()
        file_name_md5 = md5mgr.mkmd5fromstr(file_name)
        try:
            info = cursor.fetchone("select 1 from script_execute_log where file_name_md5 = '%s'" % file_name_md5)
            if info :
                return False
            else:
                return True
        finally:
            cursor.close()
            
    def get_server_list(self, server):
        if server[1:] == "all":
            server_list = []
            for k in db_configs.keys():
                if k[:1] == server[:1]:
                    server_list.append(k)    
            return server_list
        else:
            return [server]
        
        
    def handle(self, file_name):
        if not file_name.endswith(".sql"):
            self.log("is not a sql file[%s]" % file_name)
            return
        
        if self.check(file_name) == False:
            self.log("file had execute[%s]" % file_name)
            return;
        
        info = {}
        info["file_name"] = file_name
        info["file_name_md5"] = md5mgr.mkmd5fromstr(file_name)
        info["start_time"] = datetime.now()
        
        status = 1
        
        server = file_name.replace(".sql", "").split("_")[-1]
        self.log("server[%s]" % server)
        
        server_list = self.get_server_list(server);
        self.log("server list[%s]" % server_list)
        
        finish_count = 0
        
        for server_id in server_list:
            conn = get_connection(db_configs[server_id])
            cursor = conn.cursor()
                
            start = time.time()
                
            sql_list = self.get_sql_list(file_name)
            cursor.autocommit(True)
            try:
                sqls = ""
                for sql in sql_list:
                    finish_count += 1
                    sql = sql.strip()
                    if not sql:
                        continue
                    sql = sql.replace("\n", "").replace("\r", "")
                    #self.log("sql[%s]" % sql)
                    sqls += sql
                    if finish_count % self.batch_count == 0:
                        try:
                            cursor.execute(sqls)
                        except OperationalError, e:
                            if e.args[0] not in (1050, 1060):
                                raise
                        sqls = ""
                        self.log("execute[%s]" % finish_count)
                        
                if sqls:
                    try:
                        cursor.execute(sqls)
                    except OperationalError, e:
                        if e.args[0] != 1006:
                            raise
                #cursor.execute("commit")  
            except:
                status = 2
                self.log(traceback.format_exc())
                info["start_time"] = datetime.now()
                info["extinfo"] = traceback.format_exc()
            finally:
                cursor.close()
            
            end = time.time()
            self.log("server[%s], times[%s]" % (server_id, end - start))
            
        self.backup(file_name)
        
        info["end_time"] = datetime.now()
        info["finish_count"] = finish_count
        info["status"] = status
        info["server_id"] = server
        self.db_log(info)
        
    def run(self):
        file_names = file_utility.get_files(PathSettings.SCRIPT)
        for file_name in file_names:
            self.log("handle file[%s]" % file_name)
            self.handle(file_name)
        

    def get_sql_list(self, file_name):
        f = open(file_name, "rb")
        sql_list = []
        try:
            temp_sql = ""
            for line in f:
                line = line.strip()
                if temp_sql:
                    temp_sql = "%s %s" % (temp_sql, line)
                else:
                    temp_sql = line
                if not line:
                    continue
                if line.endswith(";"):
                    sql_list.append(temp_sql)
                    temp_sql = ""
        finally:
            f.close()
            
            
        return sql_list
            

if __name__ == "__main__":
    s = single_process.SingleProcess("script_executor")
    s.check()
    import sys
    batch_count = 10000
    if len(sys.argv) == 2:
        batch_count = int(sys.argv[1])
    executor = ScriptExecutor(batch_count=batch_count)
    executor.start()
    # executor.get_sql_list("C:\\Users\\jacky\\Desktop\\update\\update_table_ad_hall.sql");
    
    
    
