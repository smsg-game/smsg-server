
from datetime import datetime, timedelta
from ldsg_admin.business import server_business
from ldsg_admin.common.mysql import get_connection
from ldsg_admin.config import PathSettings
from ldsg_admin.common import get_logging

logging = get_logging()


def send(server_id):
   
    date = datetime.now() - timedelta(days=1)
    date_str = date.strftime("%Y-%m-%d")
    
    date_s = datetime.now().strftime("%Y-%m-%d")
    
    file_name = "%s/reward_payment_double_%s_%s.sql" % (PathSettings.SCRIPT, date_s, server)
    logging.debug(file_name)
    f = open(file_name, "wb")
    
    db_config = server_business.get_server_db_connect(server_id=server_id)
    
    connection = get_connection(db_config)
    cursor = connection.cursor()
    try:
        sql = "SELECT sum(gold) as gold, user_id FROM payment_log where DATE_FORMAT(created_time,'%%Y-%%m-%%d') = '%s' group by user_id" % date_str
        logging.debug(sql)
        infos = cursor.fetchall(sql)
        for info in infos:
            gold = info["gold"]
            user_id = info["user_id"]
            sql1 = "UPDATE user SET gold_num = gold_num + %s WHERE user_id ='%s' LIMIT 1;\n" % (gold, user_id)
            sql2 = "INSERT INTO user_gold_use_log(user_id, use_type, amount, flag, created_time) VALUES('%s', 10004, %s, 1, now());\n" % (user_id, gold)
            f.write(sql1)
            f.write(sql2)

    finally:
        f.close()
        cursor.close()

if __name__ == "__main__":
    for server in ("a1", "a2", "a3", "a4", "a5", "a6", "j1", "j2", "j3"):
        send(server)