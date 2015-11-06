#!/usr/bin/python
# -*- coding: utf-8 -*-
"""
升级包相关操作
"""

from ldsg_admin.common.mysql import get_connection, connection
from ldsg_admin.constants.db_constant import ldsg_center_db_configs

def add_package(ucenter, package_info):
    conn = get_connection(ldsg_center_db_configs[ucenter])
    cursor = conn.cursor()
    params = (int(package_info['id']), package_info['partner_id'], package_info['version'], package_info['versions'], package_info['full_url'], package_info['upgrade_url'],
              int(package_info['pkg_type']), package_info['description'], package_info['frs'], int(package_info['is_test']), package_info['partner_id']
              , package_info['version'], package_info['versions'], package_info['full_url'], package_info['upgrade_url'],
              int(package_info['pkg_type']), package_info['description'], package_info['frs'], int(package_info['is_test']))
    sql = "INSERT INTO package_info(id,partner_id, version, versions,full_url,upgrade_url,pkg_type,description,frs,is_test) VALUES(%s,%s, %s, %s, %s, %s, %s, %s, %s, %s) ON DUPLICATE KEY \
    UPDATE partner_id= %s, version= %s, versions= %s, full_url= %s, upgrade_url= %s, pkg_type= %s, description= %s, frs= %s, is_test= %s "
    try:
        return cursor.execute(sql,params)
    finally:
        cursor.close()
        
def sync_package(ucenter, pid):
    conn = get_connection(ldsg_center_db_configs[ucenter])
    cursor = conn.cursor()
    cursor_local = connection.cursor()
    try:
        
        info = cursor.fetchone("select * from package_info where id=%s" % pid)
        if not info:
            return
        
        partner_id = info["partner_id"]
        package_partner_info = cursor_local.fetchone("select * from partner where partner_id = %s" % partner_id)
        partner_list = cursor_local.fetchall("select * from partner where server_zone=%s" % package_partner_info["server_zone"])
        params = []
        for partner in partner_list:
            if partner["partner_id"] == partner_id:
                continue
            cursor.execute("delete from package_info where partner_id=%s and version='%s' limit 1" % (partner["partner_id"], info["version"]))
            params.append([partner["partner_id"], ])
        
        sql = "INSERT INTO package_info(partner_id, version, versions, full_url, upgrade_url, pkg_type, description, " \
                    " frs, is_test) VALUES(%%s, '%s', '%s', '%s', '%s', %s, '%s', '%s', %s) " % \
                           (info['version'], info['versions'], info['full_url'], info['upgrade_url'], info['pkg_type'], info['description'], info['frs'], info['is_test'])
        
        return cursor.executemany(sql, params)
    
    finally:
        cursor_local.close()
        cursor.close()
        
def get_package_list(ucenter='android',  version=None, page=1, pagesize=20):
    conn = get_connection(ldsg_center_db_configs[ucenter])
    cursor = conn.cursor()
    total, infos = 0, []
    try:
        where = '1=1'
        if version:
            where = "version='%s'" % version
        total = cursor.fetchone('select count(1) as total from package_info where %s' % where)["total"]
        start = (page - 1) * pagesize 
        if total > 0:
            infos = cursor.fetchall("select * from package_info where %s order by id desc limit %s, %s" % (where, start, pagesize))
    finally:
        cursor.close()
        
    return total, infos


if __name__ == '__main__':
    pass