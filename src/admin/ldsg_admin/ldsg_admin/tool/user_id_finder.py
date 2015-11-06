
from datetime import datetime, timedelta
from ldsg_admin.business import server_business
from ldsg_admin.common.mysql import get_connection
from ldsg_admin.config import PathSettings
from ldsg_admin.common import get_logging

logging = get_logging()


db_config = {
    'db': 'ldsg_center',       
    'user': 'center_reader',
    'passwd': 'reader@1a2b3c',
    'host': '118.244.198.69'   
}

def main():
   
    conn = get_connection(db_config)
    cursor = conn.cursor()
    f = open("data.csv", "rb")
    ff = open("data_out.csv", "wb")
    i = 0
    for line in f:
        if i == 0:
            ff.write(line)
            i += 1
            continue
    
        line = line.replace("\n", "")
        line = line.decode("gbk").encode("utf8")
        #print line
        datas = line.split(",")
        if len(datas) < 5:
            continue
        pid, partner_user_id, amount = datas[0], datas[6], datas[9]
        amount = float(amount) * 100
        pid = int(pid)
        
        if pid == 1:
            continue
        
        if pid > 263:
            break
        
        sql = "select * from payment_order where partner_id = 1003 and partner_user_id = '%s' and amount = %s order by created_time desc limit 1" % (partner_user_id, amount)
        info = cursor.fetchone(sql)
        if not info:
            print sql
            break
        
        server_id = info["server_id"]
        
        
        server_db_config = server_business.get_server_db_connect(server_id=server_id)
        server_conn = get_connection(server_db_config)
        server_cursor = server_conn.cursor()
        user_info = server_cursor.fetchone("select * from user where user_id = (select user_id from user_mapper where partner_user_id = '%s' and partner_id = 1003)" % partner_user_id)
        print server_id, user_info["lodo_id"]
        
        datas[7] = server_id
        datas[8] = "%s" % user_info["lodo_id"]
        
        ff.write("%s\n" % ",".join(datas))
   

if __name__ == "__main__":
    main()