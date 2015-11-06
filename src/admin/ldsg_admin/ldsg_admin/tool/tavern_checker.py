
from datetime import datetime, timedelta
from ldsg_admin.business import server_business
from ldsg_admin.common.mysql import get_connection
from ldsg_admin.config import PathSettings
from ldsg_admin.common import get_logging

logging = get_logging()

def main():
   
    conn = get_connection(server_business.get_server_db_connect(server_id="h26"))
    cursor = conn.cursor()
    maps = {}
    hero_star_map = {}
    try:
        for i in range(128): 
            infos = cursor.fetchall("select * from user_hero_log_%s where use_type = 10002" % i)
            for info in infos:
                created_time = info["created_time"]
                system_hero_id = info["system_hero_id"]
                if system_hero_id in hero_star_map:
                    star = hero_star_map[system_hero_id]
                else:
                    star = cursor.fetchone("select hero_star from system_hero where system_hero_id =%s " % system_hero_id)["hero_star"]
                    hero_star_map[system_hero_id] = star
                    
                if created_time in maps:
                    count_map = maps[created_time]
                else:
                    count_map = {}
                
                count = count_map.get(star, 0)
                count += 1
                count_map[star] = count
                
                maps[created_time] = count_map
    
    finally:
        cursor.close()
        
        
    for k, v in maps.iteritems():
        if v.get(5, 0) >= 2:
            print k, v.get(1, 0), v.get(2, 0), v.get(3, 0), v.get(4, 0), v.get(5, 0)

def main2():
   
    conn = get_connection(server_business.get_server_db_connect(server_id="h26"))
    cursor = conn.cursor()
    maps = {}
    hero_star_map = {}
    try:
        for i in range(128): 
            infos = cursor.fetchall("select * from user_hero_log_%s where use_type = 10002" % i)
            for info in infos:
                created_time = info["created_time"]
                system_hero_id = info["system_hero_id"]
                if system_hero_id in hero_star_map:
                    star = hero_star_map[system_hero_id]
                else:
                    star = cursor.fetchone("select hero_star from system_hero where system_hero_id =%s " % system_hero_id)["hero_star"]
                    hero_star_map[system_hero_id] = star
                    
                if created_time in maps:
                    count_map = maps[created_time]
                else:
                    count_map = {}
                
                count = count_map.get(star, 0)
                count += 1
                count_map[star] = count
                
                maps[created_time] = count_map
    
    finally:
        cursor.close()
        
        
    for k, v in maps.iteritems():
        if v.get(5, 0) >= 2:
            print k, v.get(1, 0), v.get(2, 0), v.get(3, 0), v.get(4, 0), v.get(5, 0)

if __name__ == "__main__":
    main2()