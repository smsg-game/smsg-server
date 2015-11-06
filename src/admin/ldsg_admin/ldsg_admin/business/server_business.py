#!/usr/bin/python
# -*- coding: utf-8 -*-
"""
服务器相关操作
"""


from ldsg_admin.common.mysql import get_connection, connection
from ldsg_admin.model import Server, ServerPartnerList, Partner
from ldsg_admin.common.server_util import str_to_dbconfig


def add_server(server_info):
    server = Server()
    for k, v in server_info.iteritems():
        setattr(server, k, v)
    server.persist()
    
def delete_server(sid):
    server = Server()
    server.id = sid
    server.is_del = 1 
    server.persist()

def get_server_list(page, pagesize, server_zone):
    return Server.paging(page, pagesize, condition="is_del=0 and server_zone=%s" % server_zone, order="open_time asc")

def get_all_server_list():
    return Server.query(condition="is_del=0")

def get_server_id_by_sid(sid):
    server = Server.load(id=sid)
    if server:
        return server.server_id

def get_server(server_id):
    return Server.load(server_id=server_id)

def get_server_db_connect(sid=None, server_id=None): 
    if sid:
        server = Server.load(id=sid)
    else:
        server = Server.load(server_id=server_id)
        
    db_connect = server.db_connect
    return str_to_dbconfig(db_connect)

def fix():
    server_list = get_all_server_list()
    for server in server_list:
        db_config = str_to_dbconfig(server.db_connect)
        connect = get_connection(db_config)
        cursor = connect.cursor()
        try:
            reg_time = cursor.fetchone("select reg_time from (select reg_time from user order by reg_time asc limit 500)d order by reg_time desc limit 1")["reg_time"]
            print server.server_id, reg_time
            server.open_time = reg_time
            server.persist()
        finally:
            cursor.close()
            
def get_server_partner_list(server_id):
    """获取服务器合作商列表"""
    return ServerPartnerList.query(condition="server_id='%s'" % server_id, order="partner_id desc")

def set_server_partner_list(server_id, server_zone, partner_ids):
    server_partner_list = get_server_partner_list(server_id)
    delete_partner = []
    
    partner_id_set = set()
    partner_id_list = partner_ids.split(",")
    #增加的parnter
    params = []
    for partner_id in partner_id_list:
        partner_id_set.add(partner_id)
        params.append([partner_id, ])
        
    for server_partner in server_partner_list:
        if server_partner.partner_id not in partner_id_set:
            partner = Partner.load(partner_id=server_partner.partner_id)
            if partner.server_zone != server_zone:
                continue
            delete_partner.append(server_partner.partner_id)
        
    cursor = connection.cursor()
    try:
        if delete_partner:
            cursor.execute("delete from server_partner_list where server_id = '%s' and partner_id in (%s)" % (server_id, ",".join(delete_partner)))
        sql = "insert ignore into server_partner_list(server_id, partner_id, created_time) values('%s', %%s, now())" % server_id
        cursor.executemany(sql, params)
    finally:
        cursor.close()
        
def get_partner_server_list(partner_id):
    
    sql = """select b.server_id, b.server_name, b.server_status, b.open_time, b.server_port from server_partner_list a 
                  left join server b on a.server_id = b.server_id where a.partner_id = %s order by b.open_time asc """
                  
    cursor = connection.cursor()
    try:
        return cursor.fetchall(sql, [partner_id, ])
    finally:
        cursor.close()
                  
if __name__ == '__main__':
    pass
    #set_server_status("127.0.0.1", 1, "112.225.56")
