'''
Created on 2014-2-9

@author: Candon
'''

import redis
from ldsg_admin.common import json
from ldsg_admin.business import server_business
from ldsg_admin.common.server_util import str_to_redisconfig
from ldsg_admin.model import ToolUseLog
from ldsg_admin.model import Server

def get_tool_use_info(server_id, date):
    if server_id == 'all':
        return ToolUseLog.query(condition="stat_date = '%s'" % date)
    else:
        return ToolUseLog.query(condition="server_id= '%s' and stat_date = '%s' "%(server_id, date))
    
def get_online(server_id):
    servers = get_server_list(server_id)
    data = {}
    for server in servers:
        if not server:
            continue
        try:
            redis_cfg = str_to_redisconfig(server.redis_connect)
            client = redis.Redis(host=redis_cfg['host'], port=redis_cfg['port'])
            s = client.get("ldsg_local_system_status_key")
            if s:
                info = json.loads(s)
                online = info["connCount"]
                data[server.server_id] = online
        except:
            pass
        
    return data
    
def get_server_list(server_id):
        servers = []
#         servers = []
#         s1 = Server()
#         s1.db_connect = "server=118.244.198.81,3306;database=ldsg_h10;user=ldsg_h10_op;pwd=3e454ss3kd40e24;"
#         s1.server_id = 1
#         s2 = Server()
#         s2.db_connect = "server=118.244.198.86,3306;database=ldsg_h11;user=ldsg_h11_rd;pwd=e727484b3251bc;"
#         s2.server_id = 2
#         servers.append(s1)
#         servers.append(s2)
        if server_id != "all":
            server = Server.load(server_id = server_id)
            servers.append(server)
            return servers
        else:
            return server_business.get_all_server_list()
    