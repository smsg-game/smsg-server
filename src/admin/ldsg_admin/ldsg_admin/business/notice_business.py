#!/usr/bin/python
# -*- coding: utf-8 -*-


from ldsg_admin.model import Notice
from ldsg_admin.common.mysql import get_connection
from ldsg_admin.constants.db_constant import ldsg_center_db_configs

from ldsg_admin.common import get_logging

logging = get_logging()


def get_notice_list(page, pagesize):
    return Notice.paging(page, pagesize, order="id desc")
    
def add_notice(info):
    notice = Notice()
    for k, v in info.iteritems():
        setattr(notice, k, v)
    notice.persist()
    
def get_effect_notice_list():
    return Notice.query(condition="start_time<=now() and end_time>=now() and status=1")
    
def add_system_notice(ucenter, info):
    conn = get_connection(ldsg_center_db_configs[ucenter])
    cursor = conn.cursor()
    total, infos = 0, []
    try:
        sql = "INSERT INTO notice(server_id, title, content, is_enable, created_time, updated_time, partner_id) " \
                "VALUES(%s, %s, %s, %s, now(), now(), %s) ON DUPLICATE KEY UPDATE title=VALUES(title), content=VALUES(content), is_enable=VALUES(is_enable), partner_id=VALUES(partner_id)"
        cursor.execute(sql, [info["server_id"], info["title"], info["content"], info["is_enable"], info["partner_id"]])
    finally:
        cursor.close()
        
    return total, infos
if __name__ == "__main__":
    pass