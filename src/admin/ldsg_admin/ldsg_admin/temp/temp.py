'''
Created on 2014-3-11

@author: Candon
'''
import traceback
from ldsg_admin.business import server_business
from ldsg_admin.common.server_util import get_conn
from ldsg_admin.util.date_utils import get_date_str_by_day
from ldsg_admin.common import server_util
from ldsg_admin.common import get_logging

def stat():
    server_list = get_server_list()
    file_name = "/data/apps/ldsg_admin/ldsg_admin/client/stat/stat/temp.csv"
    new_file = open(file_name,"wb")
    new_file.write("server_name,lodo_id\n")
    server_ids = ['h1','h2','h3','h4','h5','h6','h7','h8','h9','h10','h11','h12','h13','h14','h15','h16','h17','h18','h19','h20','h21','h22']
    for server in server_list:
        if server.server_id not in server_ids:
            continue
        date_map_o = []
        date_map_t = []
        date_map_tt = []
        
        try:
            connection = get_conn(server_util.str_to_dbconfig(server.db_connect))
            cursor = connection.cursor()
            date_o = "2014-03-08"
            date_t = get_date_str_by_day(date_o,1,'%Y-%m-%d')
            date_tt = get_date_str_by_day(date_t,1,'%Y-%m-%d')
    
            sql_o = "SELECT DISTINCT user_id FROM payment_log WHERE DATE_FORMAT(created_time,'%%Y-%%m-%%d') = '%s' group by user_id" % date_o
            sql_t = "SELECT DISTINCT user_id FROM payment_log WHERE DATE_FORMAT(created_time,'%%Y-%%m-%%d') = '%s' group by user_id" % date_t
            sql_tt = "SELECT DISTINCT user_id FROM payment_log WHERE DATE_FORMAT(created_time,'%%Y-%%m-%%d') = '%s' group by user_id" % date_tt
            infos_o = cursor.fetchall(sql_o)
            infos_t = cursor.fetchall(sql_t)
            infos_tt = cursor.fetchall(sql_tt)
            
            for info in  infos_o:date_map_o.append(info['user_id'])
            for info in  infos_t:date_map_t.append(info['user_id'])
            for info in  infos_tt:date_map_tt.append(info['user_id'])
            
            for k in date_map_o:
                if date_map_t.count(k) > 0 and date_map_tt.count(k) > 0:
                    str_t = "%s,%s\n" % (server.server_name, cursor.fetchone("select lodo_id from user where user_id = '%s'" % k)["lodo_id"])
                    new_file.write(str_t.encode("gbk"))
        except:
            print traceback.format_exc()
        
        finally:
            cursor.close()
            
    new_file.close()

def get_server_list():
    return server_business.get_all_server_list()
    
if __name__ == '__main__':
    stat()
    