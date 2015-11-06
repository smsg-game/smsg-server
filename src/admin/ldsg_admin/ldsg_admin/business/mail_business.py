#!/usr/bin/python
# -*- coding: utf-8 -*-

import urllib
import traceback
import time
from datetime import datetime

from ldsg_admin.common.mysql import get_connection
from ldsg_admin.common import json
from ldsg_admin.business import server_business
from ldsg_admin.model import SystemMail, Server
from ldsg_admin.util import table_util
from ldsg_admin.common import get_logging
from optparse import isbasestring

logging = get_logging()

def get_user_mail_list(server_id, name, source_id=0, page=1, pagesize=20):
    db_config = server_business.get_server_db_connect(server_id=server_id)
    total, infos = 0, []
    connect = get_connection(db_config)
    cursor = connect.cursor()
    try:
        user_info = cursor.fetchone("select user_id from user where username='%s'" % name)
        if not user_info:
            user_info = cursor.fetchone("select user_id from user where lodo_id='%s'" % name)
        if user_info:
            user_id = user_info["user_id"]
            table_index = table_util.get_table_index(user_id)
            table = "user_mail_%s" % table_index
            where = "user_id = '%s' " % user_id
            if source_id:
                mail_info = cursor.fetchone("select * from system_mail where source_id = %s " % source_id)
                if mail_info:
                    where += " and system_mail_id = '%s' " % mail_info["system_mail_id"]
                else:
                    where += " and system_mail_id = '-1' "
            total = cursor.fetchone("select count(1) as total from %s where %s " % (table, where))["total"]
            if total > 0:
                start = (page - 1) * pagesize
                infos = cursor.fetchall("select * from %s where %s order by created_time desc limit %s, %s" % (table, where, start, pagesize))
                
                infos = infos.to_list()
                for info in infos:
                    info["mail_info"] = cursor.fetchone("select * from system_mail where system_mail_id ='%s'" % info["system_mail_id"])
    finally:
        cursor.close()
        
    return total, infos

def get_system_mail_list(page=1, pagesize=20, status=-1):
    condition = "1=1"
    if status != -1:
        condition = "status=%s" % status
    return SystemMail.paging(page, pagesize, order="system_mail_id desc", condition=condition)
        
def add_system_mail(info):
    system_mail_id = int(info["system_mail_id"])
    if system_mail_id:
        system_mail = get_system_mail(system_mail_id)
    else:
        system_mail = SystemMail()
        
    if system_mail_id:
        del info["created_user"]
        
    for k, v in info.iteritems():
        setattr(system_mail, k, v)
    system_mail.persist()
    
def get_system_mail(system_mail_id):
    return SystemMail.load(system_mail_id=system_mail_id)


def system_mail_approve(system_mail_id, approve_user, approve=False):
    system_mail = get_system_mail(system_mail_id)
    if system_mail.status != 0:
        return
    else:
        system_mail.status = 3
        system_mail.persist()
    system_mail.approve_user = approve_user
    system_mail.approve_time = datetime.now()
    result_map = {}
    if approve:
        server_ids = system_mail.server_ids.split(",")
        send_server_ids = []
        for server_id in server_ids:
            mail_info = {}
            mail_info["title"] = system_mail.title
            mail_info["content"] = system_mail.content
            mail_info["toolIds"] = system_mail.tool_ids
            mail_info["lodoIds"] = system_mail.lodo_ids
            mail_info["target"] = system_mail.target
            mail_info["sourceId"] = system_mail.system_mail_id 
            if system_mail.date:
                mail_info["date"] = int(time.mktime(system_mail.date.timetuple())) * 1000
            else:
                mail_info["date"] = int(time.time())
                
            mail_info["partner"] = system_mail.partner_id 
            server = Server.load(server_id=server_id)
            url = "http://" + server.server_host + ":8088/gameApi/sendMail.do?"
            params = {}
            for k, v in mail_info.iteritems():
                if isbasestring(v):
                    params[k] = v.encode("utf8")
                else:
                    params[k] = v
            url = url + urllib.urlencode(params)
            logging.info("server:%s,url:%s" % (server_id, url))
            
            result_map[server_id] = 0
            approve_info = {}
            try:
                ret_val = urllib.urlopen(url).read()
                approve_info[server_id] = ret_val
                result = json.loads(ret_val)
                if result.get("rt") == 1000:
                    send_server_ids.append(server_id)
                    result_map[server_id] = 1
                else:
                    logging.warn(u"邮件发送失败.server_id[%s], msg[%s]" % (server_id, result.get("msg")))
            except:
                approve_info[server_id] = traceback.format_exc()
                logging.error(traceback.format_exc())
                
                
                
        system_mail.status = 1
        system_mail.send_server_ids = ",".join(send_server_ids)
        system_mail.approve_info = json.dumps(approve_info)
    else:
        system_mail.status = 2
        
    system_mail.persist()
    
    return 1, result_map
    
    
        
