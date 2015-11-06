#!/usr/bin/python
# -*- coding: utf-8 -*-

'''
Created on 2013-12-26

@author: Candon
'''
import sys
import urllib
from ldsg_admin.common import json
from ldsg_admin.business.user_business import get_user_register,get_user_hero_info 
from ldsg_admin.util.date_utils import *
from ldsg_admin.business import api_business
from ldsg_admin.business import activity_business
from ldsg_admin.business import server_business
from ldsg_admin.model import Server
from ldsg_admin.model import StatServerData
from ldsg_admin.business import admin_business
from ldsg_admin.model import UserPage
from ldsg_admin.client.stat.tool_use_stat import UseToolLogStat
import types 
from ldsg_admin.model import ToolUseLog

url = 'http://localhost:8088/gameApi/modifyActivity.do?startTime=2013-06-15 00:00:00&endTime=3014-09-01 00:00:00&openWeeks=1,2,3,4,5,6,7&param=30001&display=0&sort=0&'

def test():
    a = 1
    try:
        a = int('bb')
    except:
        print 'bb'
    return a

def test1(b, c, user_id=None,):
    print b,c    
        
def test2():
    data = {}
    data["n1"] = 1
    data["n2"] = 2
    print data.get("n2", 0) / data.get("n1", 1)

if __name__ == '__main__':
    pass
    pass
    data = {}
    infos = api_business.get_tool_use_info('d1', '2013-10-19')
    for i in infos:
        if i.server_id in data.keys():
            temp = []
            temp.append(i.tool_type)
            temp.append(i.tool_id)
            temp.append(i.use_type)
            temp.append(i.tool_num)
            data[i.server_id].append(temp)
        else:
            data[i.server_id] = []
            temp = []
            temp.append(i.tool_type)
            temp.append(i.tool_id)
            temp.append(i.use_type)
            temp.append(i.tool_num)
            data[i.server_id].append(temp)
  
    print json.dumps(data)
    

   
   
   
