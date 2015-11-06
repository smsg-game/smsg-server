#!/usr/bin/python
# -*- coding: utf-8 -*-

'''
Created on 2014-1-13

@author: Candon
'''
import urllib
from ldsg_admin.common.mysql import get_connection
from ldsg_admin.business import server_business
from ldsg_admin.model import Server
from optparse import isbasestring
from ldsg_admin.common import get_logging

logging = get_logging("get_forces_list")

def get_forces_list(server_id):
    
    ''' 跟住服务器ID拿关卡'''
    
    db_config = server_business.get_server_db_connect(server_id=server_id)
    connect = get_connection(db_config)
    cursor = connect.cursor()
    try:
        forces_list = cursor.fetchall("select * from system_forces where forces_type = 3")
    finally:
        cursor.close()
    return forces_list

def add_forces(server_id,forces_info):
    server = Server.load(server_id = server_id)
    try:
        if server.server_host.index(':') > 0:
            url = "http://%s/gameApi/modifyForcesTimes.do?" % server.server_host
    except:
        url = "http://%s:8088/gameApi/modifyForcesTimes.do?" % server.server_host
    logging.info(url)
    #url = "http://localhost:8088/gameApi/modifyForcesTimes.do?"
    params = {}
    for k, v in forces_info.iteritems():
        if isbasestring(v):
            params[k] = v.encode("utf8")
        else:
            params[k] = v
    url = url + urllib.urlencode(params)
    data = None
    try:
        data = urllib.urlopen(url).read()
    except Exception,e:
        logging.info(e)
    return data