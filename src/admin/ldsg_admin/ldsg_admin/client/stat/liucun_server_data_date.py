#-*- coding:utf-8 -*-
'''
Created on 2014-1-8

@author: Candon
'''
import sys
import datetime
from ldsg_admin.client.base_client import BaseClient
from ldsg_admin.business import server_business
from ldsg_admin.common.server_util import get_conn
from ldsg_admin.util.date_utils import datetime_to_string,string_to_datetime,get_time,get_datetime_str_by_day
from ldsg_admin.model import StatLiucunDataDate
from ldsg_admin.common import single_process
from ldsg_admin.common import server_util
from ldsg_admin.model import Server
from ldsg_admin.common.mysql import connection

class LiuCunServerDataDate(BaseClient):
    CLIENT_NAME = "liucun_server_data_date"
    statServerDatas = []
    server_id = None
    def __init__(self ,isInsert = True,server_id=None):
        super(self.__class__, self).__init__(self.__class__.CLIENT_NAME)
        self.isInsert = isInsert
        if server_id:
            self.server_id = server_id
            
    def run(self):
        if self.server_id:
            #ServerPartnerList.query(condition="server_id='%s'" % server_id, order="partner_id desc")
            statLiucunData = StatLiucunDataDate.load(server_id = server_id)
            if statLiucunData:
                statLiucunData.is_hostory = 1
                statLiucunData.persist()
        else:
            curr_cursor = None
            try:
                curr_cursor = connection.cursor()
                sql = "update stat_liucun_data set is_hostory = 1"
                curr_cursor.execute(sql)
            except Exception,e:
                self.log(e)
            finally:
                if curr_cursor is not None:
                    curr_cursor.close()
        server_list = self.get_server_list()
        now_time = datetime.datetime.now()
        for server in server_list:
            try:
                if server.server_status==100:
                    continue
                begin_date = server.open_time
                while(datetime_to_string(now_time,"%Y-%m-%d") != datetime_to_string(begin_date,"%Y-%m-%d") and now_time < string_to_datetime(get_datetime_str_by_day(begin_date,30))):
                    statLiucunData = StatLiucunDataDate.query(condition="server_id='%s' and data_date='%s'" % (server.server_id,datetime_to_string(begin_date,"%Y-%m-%d")))
                    if statLiucunData:
                        statLiucunData[0].is_hostory = 1
                        statLiucunData[0].persist()
                    self.stat(server,begin_date)
                    begin_date = string_to_datetime(get_datetime_str_by_day(begin_date,1));
                    statLiucunData = StatLiucunDataDate()
                    statLiucunData.delete_mul(is_hostory=1)
            except Exception,ex:
                self.log(ex)
        
    
    def stat(self,server,open_time):
        self.log("LiuCunServerData start %s,server:[%s]" % (get_time(),server.server_name))
        values = {}
        days = {'one_user':0,'two_user':1,'three_user':3,'four_user':4,'five_user':5,'six_user':6,'seven_user':7,'fivteen_user':15,'thirty_user':30}
        values['server_id'] = server.server_id
        values['server_name'] = server.server_name
        con= get_conn(server_util.str_to_dbconfig(server.db_connect))
        for k,v in days.iteritems():
            temp_date = get_datetime_str_by_day(open_time,v,"%Y-%m-%d")
            values[k] = self.stat_login_user(temp_date,con,get_datetime_str_by_day(open_time,0,"%Y-%m-%d"))
               
        statLiucunData = StatLiucunDataDate()
        one_user = values['one_user']
        for k, v in values.iteritems():
            if k == 'one_user' or k == 'server_id' or k == 'server_name':
                setattr(statLiucunData, k, v)
            else:
                value = 0
                if one_user != 0 and v != 0:
                    value = round(v/float(one_user),5)*100
                    setattr(statLiucunData, k, value)
                else:
                    setattr(statLiucunData, k, value)
        statLiucunData.data_date=datetime_to_string(open_time)
        statLiucunData.persist()
            
    def stat_login_user(self, date, conn,fdate):
        login_user  = 0        
        if self.isInsert:
            for i in range(128):
                self.log("user_online_log_%d" % i)
                temp_cursor = None
                try:
                    temp_cursor = conn.cursor()
                    sql = "select count(distinct a.user_id) as login_user from user_online_log_%d as a join (SELECT u.`user_id` FROM `user` u WHERE date(u.`reg_time`) = '%s') b on a.user_id = b.user_id where date(a.login_time) = '%s'" % (i,fdate,date)
                    data = temp_cursor.fetchone(sql)
                    if data:
                        login_user = login_user+int(data['login_user'])
                except Exception,e:
                    self.log(e);
                finally:
                    if temp_cursor is not None:
                        temp_cursor.close()
        
        return login_user
    
    def get_server_list(self):
        servers = []
#         servers = []
#         s1 = Server()
#         s1.db_connect = "server=118.244.198.81,3306;database=ldsg_h10;user=ldsg_h10_op;pwd=3e454ss3kd40e24;"
#         s1.server_id = 1
#         s2 = Server()
#         s2.db_connect = "server=118.244.198.86,3306;database=ldsg_h11;user=ldsg_h11_rd;pwd=e727484b3251bc;"
#         s2.server_id = 2
#         servers.append(s1)
#         servers.append(s2)
        if self.server_id:
            server = Server.load(server_id = self.server_id)
            servers.append(server)
            return servers
        else:
            return server_business.get_all_server_list()
    
if __name__ == "__main__":
    if len(sys.argv) == 2:
        server_id = sys.argv[1]
        s = single_process.SingleProcess("liucun_server_data")
        s.check()
        executor = LiuCunServerDataDate(server_id = server_id)
        executor.start()
    else:
        s = single_process.SingleProcess("liucun_server_data")
        s.check()
        executor = LiuCunServerDataDate()
        executor.start()