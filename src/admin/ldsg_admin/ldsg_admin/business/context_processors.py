#!/usr/bin/python
# -*- coding: utf-8 -*-

from ldsg_admin.business import server_business, user_roles
from ldsg_admin.business import partner_business
from ldsg_admin.constants.server_constant import server_zone_map

def set_server_list(request):
    
    server_id = request.REQUEST.get('server_id', None)
    if server_id:
        request.COOKIES['sid'] = server_id
    
    server_id = request.COOKIES.get('sid', server_id)
    
    server_list = server_business.get_all_server_list()
    partner_list = partner_business.get_all_partner_list()
    user = user_roles.get_userinfo(request)
    
    server_list_map = {}
    for server in server_list:
        server_zone = server.server_zone
        server_zone_server_list = server_list_map.get(server_zone, [])
        server_zone_server_list.append(server)
        server_list_map[server_zone] = server_zone_server_list
        
    for k, values in server_list_map.iteritems():
        values = sorted(values, cmp=lambda x, y : cmp(int(x.server_id[1:]), int(y.server_id[1:])))
        server_list_map[k] = values
        
    return {'server_list': server_list, 'server_id': server_id, 'partner_list': partner_list, "user": user, "server_list_map": server_list_map, "server_zone_map": server_zone_map} 

if __name__ == "__main__":
    a = [{"s": 1}, {"s": 7}, {"s": 5}, ]
    print sorted(a, cmp=lambda x, y : cmp(x["s"], y["s"]))
    print a