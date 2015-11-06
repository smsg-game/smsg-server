#!/usr/bin/python
# -*- coding: utf-8 -*-

from ldsg_admin.common.mysql.connection_wrapper import Connection

def str_to_dbconfig(db_connect):
    db_config = {}
    ds = db_connect.split(";")
    #server=118.144.92.41,3306;database=ldst_t3;user=zgame;pwd=123456;
    for d in ds:
        if not d:
            continue
        infos = d.split("=")
        try:
            key, value = infos[0], infos[1]
            if key == 'server':
                db_config['host'] = value.split(",")[0]
                db_config['port'] = int(value.split(",")[1])
            elif key == 'database':
                db_config['db'] = value
            elif key == 'user':
                db_config['user'] = value
            elif key == 'pwd':
                db_config['passwd'] = value
        except:
            pass
        
            
    return db_config

def str_to_redisconfig(redis_connect):
    redis_config = {}
    ds = redis_connect.split(":")
    #d1.ldsg.lodogame.com:6379
    redis_config['host'] = ds[0]
    redis_config['port'] = int(ds[1])
            
    return redis_config

def get_conn(db_config, charset="utf8"):
    return Connection(host=db_config['host'], port=db_config['port'], db=db_config['db'], user=db_config['user'], passwd=db_config['passwd'], charset=charset)