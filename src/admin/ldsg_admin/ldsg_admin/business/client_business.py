#!/usr/bin/python
# -*- coding: utf-8 -*-
"""
后台相关操作
"""

from datetime import datetime

from ldsg_admin.model import ClientRunLog, Client, BattleClient
from ldsg_admin.common import json
from ldsg_admin.common import get_logging

logging = get_logging()

def register(ip, client_name, server_ids):
    client = Client.load(client_name=client_name, ip=ip)
    if client:
        client.server_ids = server_ids
        client.last_report_time = datetime.now()
        client.persist()
        return client.id
    else:
        client = Client()
        client.client_name = client_name
        client.ip = ip
        client.last_report_time = datetime.now()
        client.server_ids = server_ids
        client.persist()
        return client.id

def get_command(client_id):
    client = Client.load(id=client_id)
    if client and client.command:
        command = client.command
        client.command = ""
        client.last_command = command
        client.persist() 
        return json.loads(command)
    
def get_client_list():
    return Client.query()
    
def report_status(client_id):
    client = Client.load(id=client_id)
    if client:
        client.last_report_time = datetime.now()
        logging.info(client.last_report_time)
        client.status = 1
        client.persist()
    
def report_log(client_id, server_id, ret, command_type, output, params):
    client_run_log = ClientRunLog()
    client_run_log.client_id = client_id
    client_run_log.status = ret
    client_run_log.content = output
    client_run_log.extinfo = json.dumps(params)
    client_run_log.command_type = command_type
    client_run_log.server_id = server_id
    client_run_log.persist()
    
def report_result(client_id, data):
    client = Client.load(id=client_id)
    logging.debug("report_result, client_id[%s], data[%s]" % (client_id, data))
    if client:
        client.last_command_report = json.dumps(data)
        client.persist()
    
def add_command(client_ids, command):
    client_id_list = client_ids.split(",")
    command_type = int(command["command_type"])
    command["command_type"] = command_type
    command_json = json.dumps(command)
    for client_id in client_id_list:
        logging.debug("client_id[%s], command[%s]" % (client_id, command))
        client = Client.load(id=client_id)
        if client:
            client.command = command_json
            client.persist()
            
def get_client_run_log_list(client_id, page, pagesize):
    return ClientRunLog.paging(page, pagesize, condition="client_id=%s" % client_id, order="id desc")

def battle_status_report(ip, port):
    battle_client = BattleClient()
    battle_client.ip = ip
    battle_client.port = port
    battle_client.last_report_time = datetime.now()
    battle_client.persist()
    
def update_battle_clinet(cid, server_id):
    battle_client = BattleClient.load(id=cid)
    battle_client.server_id = server_id
    battle_client.persist()
    
def delete_battle_clinet(cid):
    battle_client = BattleClient.load(id=cid)
    battle_client.delete()

    
def get_battle_client_list():
    return BattleClient.query()