# -*- coding:utf-8 -*-

from connection_wrapper import Connection as __Connection
from ldsg_admin.config import DB_Mysql


def get_connection(db_config):
    connection = __Connection(
                          user=db_config['user'],
                          db=db_config['db'],
                          passwd=db_config['passwd'],
                          host=db_config['host'],
                          port=db_config.get("port",3306),
                          charset="utf8",
                          init_command="set wait_timeout = 300",

                          )
    return connection

connection = get_connection(DB_Mysql)

__all__ = ("connection", )

