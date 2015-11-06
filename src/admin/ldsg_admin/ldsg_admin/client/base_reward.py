#!/usr/bin/python
# -*- coding: utf-8 -*-

import uuid

from ldsg_admin.constants.tool_type import ToolType
from ldsg_admin.common.mysql import get_connection
from ldsg_admin.client.base_client import BaseClient

class BaseSendReward(BaseClient):
    """"发奖励基类"""
    
    def __init__(self, name, server_ids=[], run_script=False):
        super(self.__class__, self).__init__(name)
        self.__run_script = run_script
        
    def send_tool(self, user_id, tool_type, tool_id, tool_num):
        if tool_type == ToolType.TOOL_TYPE_GOLD:
            self.__send_gold(user_id, tool_num)
        elif tool_type == ToolType.TOOL_TYPE_HERO:
            self.__send_hero(user_id, tool_id, tool_num)
        else:
            self.__send_tool(user_id, tool_id, tool_num)
        
    def send_hero(self, db_config, user_id, hero_id, num):
        connection = get_connection(db_config)
        cursor = connection.cursor()
        try:
            for _ in range(num):
                user_hero_id = str(uuid.uuid4()).replace("-","")
                sql = "INSERT INTO user_hero(user_hero_id, user_id, system_hero_id, hero_exp, hero_level, pos, created_time, updated_time) " \
                         "VALUES('%s', '%s', %s, 0, 1, 0, NOW(), NOW());\n" % (user_hero_id, user_id, hero_id)
                if self.__run_script:
                    cursor.execute(sql)
                else:
                    self.log(sql)
        finally:
            cursor.close()
        
        
    def run(self):
        pass
    
    

                
    
tool_maps = {}
    
def get_tool_type(db_config, tool_id):
    global  tool_maps
    if tool_id in tool_maps:
        return tool_maps[tool_id]
    connection = get_connection(db_config)
    cursor = connection.cursor()
    try:
        tool_type = cursor.fetchone("select type from system_tool where tool_id = %s" % tool_id)["type"]
        tool_maps[tool_id] = tool_type
        return tool_type
    finally:
        cursor.close()
    
def send_tool(f, db_config, user_id, tool_id, tool_num):
    tool_type = get_tool_type(db_config, tool_id)
    if not DEBUG:
        connection = get_connection(db_config)
        cursor = connection.cursor()
    try:
        sql = "INSERT INTO user_tool(user_id, tool_type, tool_id, tool_num, created_time, updated_time) VALUES('%s', %s, %s, %s, NOW(), NOW()) " \
               "ON DUPLICATE KEY UPDATE tool_num = tool_num + values(tool_num);\n" % (user_id, tool_type, tool_id, tool_num)
        if not DEBUG:
            cursor.execute(sql)
        else:
            f.write(sql)
    finally:
        if not DEBUG:
            cursor.close() 
    