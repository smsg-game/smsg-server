#!/usr/bin/python
# -*- coding: utf-8 -*-

import random
from datetime import timedelta

from ldsg_admin.common.mysql import get_connection
from ldsg_admin.constants.db_constant import ldsg_common_db_configs
from ldsg_admin.common import get_logging

logging = get_logging()

def get_giftcode_list(category, gift_bag_type, sub_type, giftcode, page=1, pagesize=30):
    print ldsg_common_db_configs[category]
    conn = get_connection(ldsg_common_db_configs[category])
    cursor = conn.cursor()
    try:
        if giftcode:
            where = " code = '%s'" % giftcode
        else:
            where = " gift_bag_type=%s and type=%s" % (gift_bag_type, sub_type)
            
        if pagesize == 1000000:
            info = cursor.fetchone("select version from gift_code where %s order by version desc limit 1" % where)
            if info:
                version = info["version"]
                where += " and version = %s " % version
            
        start = (page - 1) * pagesize
        total = cursor.fetchone("select count(1) as total from gift_code where %s " % where)["total"]
        sql = "select * from gift_code where %s limit %s, %s" % (where, start, pagesize)
        infos = cursor.fetchall(sql)
        return total, infos
    finally:
        cursor.close()
        
def get_code():
    ll = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', \
           'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'] 
    
    code = ""
    for _ in range(6):
        code = code + ll[random.randint(0, 25)]
    return code
    
def add_giftcode(category, gift_bag_type, sub_type, amount):
    amount = int(amount)
    connection = get_connection(ldsg_common_db_configs[category])
    cursor = connection.cursor()
    try:
        
        version = 1
        info = cursor.fetchone("select version from gift_code where gift_bag_type=%s and type=%s order by version desc limit 1" % (gift_bag_type, sub_type))
        if info:
            version = info["version"] + 1
        
        add_count = 0
        while True:
            values = []
            for _ in range(100 if amount > 100 else amount):             
                code = get_code()
                values.append([code, ])
                
            effect_count = cursor.executemany("insert ignore into gift_code values(%%s, %s, 0, now(), null, %s, 1, 'all', %s)" % (sub_type, gift_bag_type, version), values)
            logging.debug("effect_count[%s]" % effect_count)
            add_count += effect_count
            if add_count > amount:
                break
        
    finally:
        cursor.close()
        
    return True
        

if __name__ == "__main__":
    add_giftcode("test", 1197, 0, 120)

    