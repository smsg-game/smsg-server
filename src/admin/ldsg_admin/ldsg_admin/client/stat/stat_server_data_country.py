#-*- coding:utf-8 -*-

'''
Created on 2014-1-6

@author: Candon
'''
from ldsg_admin.business import server_business
from ldsg_admin.client.base_client import BaseClient
from ldsg_admin.common import server_util
from ldsg_admin.common import single_process
from ldsg_admin.common.mysql import connection
from ldsg_admin.common.server_util import get_conn
from ldsg_admin.model import Server
from ldsg_admin.model import StatServerDataCountry
from ldsg_admin.util.date_utils import get_date, get_time, get_date_str_by_day, string_to_datetime
import sys


class StatServerDateCountry(BaseClient):
    
    CLIENT_NAME = "stat_server_data_country"
    statServerDatass = []
    isInsert = True
    date = get_date()
    server_id = None
    is_input = False
    def __init__(self ,isInsert = True,date=None, server_id=None):
        super(self.__class__, self).__init__(self.__class__.CLIENT_NAME)
        self.statServerDatass = []
        self.isInsert = isInsert
        if date is not None:
            self.is_input = True
            self.date = date
        if server_id is not None:
            self.server_id = server_id
            
    def run(self):
        server_list = self.get_server_list()
        self.log("StatServerDate start %s" % get_time())
        
        if self.server_id is None:
            statServerDatas = StatServerDataCountry.query(condition="stat_date ='%s'" % self.date)
        else:
            statServerDatas = StatServerDataCountry.query(condition="stat_date ='%s' and server_id = '%s' " % (self.date, self.server_id))
        if not statServerDatas:
            for server in server_list:
                try:
                    if server.server_status==100:
                        continue
                    countries = self.getServerCountryCode(server)
                    if countries :
                        for country in countries:
                            self.stat(server, self.date, country['idfa'])
                except Exception,ex:
                    self.log("server_name[%s] error" % server.server_name)
                    self.log(ex)
                
            self.log("StatServerDate end %s" % get_time())
            
    def getServerCountryCode(self,server):
        con= get_conn(server_util.str_to_dbconfig(server.db_connect))
        ''' 新增用户  '''
        sql = "select idfa from user_mapper group by idfa"
        cursor = None
        try:
            cursor = con.cursor()
            data = cursor.fetchall(sql)
            self.log("country codes:'%s'"%(data))
        except Exception,ex:
            self.log(ex)
        finally:
            if cursor is not None:
                cursor.close()
        return data
    
    def stat(self ,server,date,country):
        while True:
            values = {}
            values['server_id'] = server.server_id 
            values['server_name'] = server.server_name
            con= get_conn(server_util.str_to_dbconfig(server.db_connect))
            ''' 新增用户  '''
            sql = "SELECT COUNT(user_id) AS new_register FROM `user_mapper` WHERE DATE(created_time) = '%s' AND idfa='%s'" % (date,country)
            self.log(sql)
            cursor = None
            try:
                cursor = con.cursor()
                data = cursor.fetchone(sql)
                values['new_register'] = int(data['new_register'])
            except Exception,ex:
                self.log(ex)
            finally:
                if cursor is not None:
                    cursor.close()
            
            ''' 创建用户 '''
            sql = "SELECT COUNT(a.user_id) AS create_user FROM `user` a left join user_mapper m on a.user_id=m.user_id  WHERE DATE(a.reg_time) = '%s' and m.idfa='%s'" % (date,country)
            self.log(sql)
            cursor = None
            try:
                cursor = con.cursor()
                data = cursor.fetchone(sql)
                values['create_user'] = int(data['create_user'])
            except Exception,ex:
                self.log(ex)
            finally:
                if cursor is not None:
                    cursor.close()
                
            ''' 日活跃用�?'''
            active_user = 0
            try:
                if self.isInsert:
                    for i in range(128):
                        sql = "select count(distinct u.user_id) as date_active from user_online_log_%d u left join user_mapper m on u.user_id=m.user_id where date(login_time) = '%s' and m.idfa='%s';" % (i,date,country)
                        self.log(sql)
                        cursor = None
                        try:
                            cursor = con.cursor()
                            data = cursor.fetchone(sql)
                            active_user = active_user+int(data['date_active'])
                        except Exception,ex:
                            self.log(ex)
                        finally:
                            if cursor is not None:
                                cursor.close()
                values['date_active'] = active_user
            except:
                self.log('not record found')
            
            ''' 充�?人数 '''
            cursor = None
            try:
                cursor = con.cursor()
                sql = "select count(DISTINCT p.user_id) as pay_people from payment_log p left join user_mapper m on p.user_id=m.user_id where date(p.created_time) = '%s' and m.idfa='%s';" % (date,country)
                self.log(sql)
                data = cursor.fetchone(sql)
                values['pay_people'] = int(data['pay_people'])
            except Exception,ex:
                self.log(ex)
            finally:
                if cursor is not None:
                    cursor.close()
            
            ''' 充�?金额 '''
            cursor = None
            try:
                cursor = con.cursor()
                #sql = "select COALESCE(SUM(amount),0) as pay_money from payment_log where date(created_time) = '%s';" % (date)
                sql = "select COALESCE(SUM(a.amount),0) as pay_money from payment_log as a join user as b on (a.user_id = b.user_id) join user_mapper as \
                c on (b.user_id = c.user_id) where date(a.created_time) = '%s' and c.idfa='%s' " % (date,country)
                self.log(sql)
                data = cursor.fetchone(sql)
                values['pay_money'] = int(data['pay_money'])
            except Exception,ex:
                self.log(ex)
            finally:
                if cursor is not None:
                    cursor.close()
            
            ''' 新用户充�?'''
            cursor = None
            try:
                cursor = con.cursor()
                sql = "select COALESCE(SUM(amount),0) as new_user_pay from payment_log a ,user b, user_mapper c  where a.user_id = b.user_id and b.user_id = c.user_id and date(b.reg_time) = '%s' \
                and  date(c.created_time) = '%s' and date(a.created_time) = '%s' and c.idfa='%s' " % (date,date,date,country)
                self.log(sql)
                data = cursor.fetchone(sql)
                values['new_user_pay'] = int(data['new_user_pay'])
            except Exception,ex:
                self.log(ex)
            finally:
                if cursor is not None:
                    cursor.close()
            
            ''' 老用户充�?'''
            values['old_user_pay'] = int(values['pay_money']) - int(values['new_user_pay'])
            
            ''' 新用户充值人�?'''
            cursor = None
            try:
                cursor = con.cursor()
                sql = "select count(DISTINCT a.user_id) as new_payer from payment_log a ,user b, user_mapper c  where a.user_id = b.user_id and b.user_id = c.user_id and date(b.reg_time) = '%s' \
                and  date(c.created_time) = '%s' and date(a.created_time) = '%s' and c.idfa='%s' " % (date, date, date,country)
                self.log(sql)
                data = cursor.fetchone(sql)
                values['new_payer'] = int(data['new_payer'])
            except Exception,ex:
                self.log(ex)
            finally:
                if cursor is not None:
                    cursor.close()
            
            ''' 老用户充值人�?'''
            values['old_payer'] = int(values['pay_people']) - int(values['new_payer'])
            
            ''' �?��在线 '''
            cursor = None
            try:
                cursor = connection.cursor()
                sql = "select COALESCE(max(online),0) as max_online from stat_online where date(stat_time) = '%s' and server_id = '%s'" % (date,values['server_id'])
                data = cursor.fetchone(sql)
                if not data and data['max_online'] is None:
                    values['max_online'] = 0
                else:
                    values['max_online'] = int(data['max_online'])
            except Exception,ex:
                self.log(ex)
            finally:
                if cursor is not None:
                    cursor.close()  
            
            statServerData = StatServerDataCountry()
            statServerData.country_code=country
            for k, v in values.iteritems():
                setattr(statServerData, k, v)
            statServerData.stat_date = date
            if self.isInsert:
                statServerData.persist()
                #print ''
            else:
                self.statServerDatass.append(statServerData)
            
            
            date = get_date_str_by_day(date,1,'%Y-%m-%d')
#             if self.is_input:
#                 break
            if string_to_datetime(date,'%Y-%m-%d') >= string_to_datetime(get_date(),'%Y-%m-%d') :
                break;
            
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
        if self.server_id is not None:
            server = Server.load(server_id = self.server_id)
            servers.append(server)
            return servers
        else:
            return server_business.get_all_server_list()
    
if __name__ == "__main__":
    if len(sys.argv) == 2:
        date = sys.argv[1]
        s = single_process.SingleProcess("stat_server_data_country")
        s.check()
        executor = StatServerDateCountry(date=date)
        executor.start()
    elif len(sys.argv) == 3:
        date = sys.argv[1]
        server_id = sys.argv[2]
        s = single_process.SingleProcess("stat_server_data_country")
        s.check()
        executor = StatServerDateCountry(date=date, server_id=server_id)
        executor.start()
    else:
        s = single_process.SingleProcess("stat_server_data_country")
        s.check()
        executor = StatServerDateCountry()
        executor.start()
    
            
             
            
        
