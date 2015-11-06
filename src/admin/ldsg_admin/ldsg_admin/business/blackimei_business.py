#!/usr/bin/python
# -*- coding: utf-8 -*-
"""
升级包相关操作
"""

from ldsg_admin.common.mysql import get_connection
from ldsg_admin.constants.db_constant import ldsg_center_db_configs

def add_black_imei(ucenter,imei,partner_id):
    conn = get_connection(ldsg_center_db_configs[ucenter])
    cursor = conn.cursor()
    try:
        if imei != "" and partner_id != "":
                value=[imei,partner_id]
                cursor.execute("insert into black_imei values (null,%s,%s)",value)
    finally:
        cursor.close()

def del_black_imei(ucenter,id):
    conn = get_connection(ldsg_center_db_configs[ucenter])
    cursor = conn.cursor()
    try:
        if id:
            cursor.execute("DELETE  FROM black_imei WHERE id="+id)
    finally:
        cursor.close()

def get_black_imei_list(ucenter='android',  page=1, imei=None, pagesize=20):
    conn = get_connection(ldsg_center_db_configs[ucenter])
    cursor = conn.cursor()
    total, infos = 0, []
    try:
        where = '1=1'
        if imei:
            where = "imei='%s'" % imei
        total = cursor.fetchone('select count(1) as total from black_imei where %s' % where)["total"]
        start = (page - 1) * pagesize 
        if total > 0:
            infos = cursor.fetchall("select * from black_imei where %s order by id desc limit %s, %s" % (where, start, pagesize))
    finally:
        cursor.close()
        
    return total, infos
         

if __name__ == '__main__':
    print get_black_imei_list()