#-*- coding:utf-8 -*-
'''
Created on 2014-2-8

@author: Candon
'''
import traceback
import sys
from ldsg_admin.client.base_client import BaseClient
from ldsg_admin.business import server_business
from ldsg_admin.common import server_util
from ldsg_admin.common.mysql import get_connection
from ldsg_admin.model import ToolUseLog
from ldsg_admin.common import single_process
from ldsg_admin.model import Server
from ldsg_admin.constants.tool_type import ToolType
from ldsg_admin.constants.tool_id import ToolID
from ldsg_admin.util.date_utils import get_date,get_date_str_by_day,string_to_datetime,datetime_to_string,get_yestoday_str_by_str

class UseToolLogStat(BaseClient):
    CLIENT_NAME = "use_tool_log"
    date = get_yestoday_str_by_str(get_date(),'%Y-%m-%d')
    server_id = None
    
    def __init__(self,date=None, server_id=None):
        super(self.__class__, self).__init__(self.__class__.CLIENT_NAME)
        if date is not None:
            self.date = date
        if server_id is not None:
            self.server_id = server_id
                
    def run(self):
        
        server_list = self.get_server_list()
        for server in server_list:
            try:
                self.stat(server, self.date)
            except:
                self.log(traceback.format_exc())
                pass
            
    def stat(self, server,date):
        db_config = server_util.str_to_dbconfig(server.db_connect)
        connect = get_connection(db_config)
        page = 1
        data_map = {}
        
        """ 统计金币消耗  """
        
        self.log('server_name:[%s] stat gold start,date:[%s]'% (server.server_name,date))
        while True:
            num = (page - 1) * 10000
            sql = "SELECT SUM(amount) as total ,use_type,flag FROM (SELECT * FROM user_gold_use_log WHERE DATE(created_time) = '%s' LIMIT %d,10000) t GROUP BY use_type,flag" %(date, num)
            cursor = None
            try:
                cursor = connect.cursor()
                datas = cursor.fetchall(sql)
                if not datas:
                    break
                for data in datas:
                    key = str(data['use_type']) + ',' + str(data['flag'])
                    total = data_map.get(key,0)
                    total += data['total']
                    data_map[key] = total
            except Exception,ex:
                self.log(ex)
                self.log("server_name:[%s],page:[%s] stat fail "%(server.server_name, page))
            finally:
                page += 1 
                cursor.close()
                
        if bool(data_map):
            for k,v in data_map.items():
                use_type = k.split(',')[0]
                flag = k.split(',')[1]
                
                tool_use_log = ToolUseLog()
                tool_use_log.server_id = server.server_id
                tool_use_log.tool_type = ToolType.TOOL_TYPE_GOLD
                tool_use_log.tool_id = ToolID.TOOL_ID_GOLD
                tool_use_log.use_type = int(use_type)
                if flag == '-1':
                    tool_use_log.tool_num = 0 - v
                else:
                    tool_use_log.tool_num = v
                tool_use_log.stat_date = date
                
                tool_use_log.persist()
        
        """ 统计银币消耗 """
        page = 1
        data_map = {}
        self.log('server_name:[%s] stat copper start'% (server.server_name))
        for i in range(128):
            while True:
                num = (page - 1) * 10000
                sql = "SELECT SUM(amount) as total ,use_type,flag FROM (SELECT * FROM user_copper_use_log_%d WHERE DATE(created_time) = '%s' LIMIT %d,10000) t GROUP BY use_type,flag" %(i, date, num)
                cursor = None
                try:
                    cursor = connect.cursor()
                    datas = cursor.fetchall(sql)
                    if not datas:
                        break
                    for data in datas:
                        key = str(data['use_type']) + ',' + str(data['flag'])
                        total = data_map.get(key,0)
                        total += data['total']
                        data_map[key] = total
                except Exception,ex:
                    self.log(ex)
                finally:
                    page += 1 
                    cursor.close()
        if bool(data_map):
            for k,v in data_map.items():
                use_type = k.split(',')[0]
                flag = k.split(',')[1]
                
                tool_use_log = ToolUseLog()
                tool_use_log.server_id = server.server_id
                tool_use_log.tool_type = ToolType.TOOL_TYPE_COPPER
                tool_use_log.tool_id = ToolID.TOOL_ID_COPPER
                tool_use_log.use_type = int(use_type)
                if flag == '-1':
                    tool_use_log.tool_num = 0 - v
                else:
                    tool_use_log.tool_num = v
                tool_use_log.stat_date = date
                
                tool_use_log.persist()
        """ 统计道具消耗 """
        page = 1
        data_map = {}
        self.log('server_name:[%s] stat tool start'% (server.server_name))
        for i in range(128):
            while True:
                num = (page - 1) * 10000
                sql = "SELECT SUM(tool_num) as total ,use_type,flag,tool_id,tool_type FROM (SELECT * FROM user_tool_use_log_%d WHERE DATE(created_time) = '%s' LIMIT %d,10000) t GROUP BY use_type,flag,tool_id" %(i, date, num)
                cursor = None
                try:
                    cursor = connect.cursor()
                    datas = cursor.fetchall(sql)
                    if not datas:
                        break
                    for data in datas:
                        key = str(data['use_type']) + ',' + str(data['flag']) + ',' + str(data['tool_id']) + ',' + str(data['tool_type'])
                        total = data_map.get(key,0)
                        total += data['total']
                        data_map[key] = total
                except Exception,ex:
                    self.log(ex)
                finally:
                    page += 1 
                    cursor.close()
        if bool(data_map):
            for k,v in data_map.items():
                att_str = k.split(',')
                use_type = att_str[0]
                flag = att_str[1]
                tool_id = int(att_str[2])
                tool_type = int(att_str[3])
                
                tool_use_log = ToolUseLog()
                tool_use_log.server_id = server.server_id
                tool_use_log.tool_type = tool_type
                tool_use_log.tool_id = tool_id
                tool_use_log.use_type = use_type
                if flag == '-1':
                    tool_use_log.tool_num = 0 - v
                else:
                    tool_use_log.tool_num = v
                tool_use_log.stat_date = date
                
                tool_use_log.persist()
    
    def get_server_list(self):
        servers = []
        if self.server_id is not None:
            server = Server.load(server_id = self.server_id)
            servers.append(server)
            return servers
        else:
            return server_business.get_all_server_list()
            
if __name__ == "__main__":
    if len(sys.argv) == 2:
        date = sys.argv[1]
        s = single_process.SingleProcess("use_tool_log")
        s.check()
        executor = UseToolLogStat(date = date)
        executor.start()
    elif len(sys.argv) == 3:
        date = sys.argv[1]
        if not date:
            server_id = sys.argv[2]
            s = single_process.SingleProcess("use_tool_log")
            s.check()
            executor = UseToolLogStat(server_id = server_id)
            executor.start()
        else:
            date = sys.argv[1]
            server_id = sys.argv[2]
            s = single_process.SingleProcess("use_tool_log")
            s.check()
            executor = UseToolLogStat(date = date, server_id = server_id)
            executor.start()
        
    else:
        s = single_process.SingleProcess("use_tool_log")
        s.check()
        executor = UseToolLogStat()
        executor.start()
    
    
    