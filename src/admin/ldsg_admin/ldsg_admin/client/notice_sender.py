#!/usr/bin/python
# -*- coding: utf-8 -*-

import time
import traceback
from datetime import datetime, timedelta


from ldsg_admin.util import http_util
from ldsg_admin.common import json, md5mgr
from ldsg_admin.common import single_process
from ldsg_admin.client.base_client import BaseClient
from ldsg_admin.business import server_business, notice_business


class NoticeSender(BaseClient):
    
    CLIENT_NAME = "notice_sender"
    
    def __init__(self):
        super(self.__class__, self).__init__(self.__class__.CLIENT_NAME)
            
    def run(self):
        notice_list = self.get_notice_list()
        for notice in notice_list:
            try:
                self.send_notice(notice)
            except:
                self.log(traceback.format_exc())
                pass
            
    def send_notice(self, notice):
        """发送走马灯"""
        last_send_time = notice.last_send_time
        now = datetime.now()
        if last_send_time:
            if last_send_time + timedelta(minutes=notice.interval_time) > now:
                return
        server_id_list = notice.server_ids.split(",")
        for server_id in server_id_list:
            try:
                self.log("send to server_id[%s], content[%s]" % (server_id, notice.content))
                self.send_notice_to_server(server_id, notice.content)
            except:
                self.log(traceback.format_exc())
                
        notice.last_send_time = now
        notice.persist()
            
    def send_notice_to_server(self, server_id, content):
        info = {}
        timestamp = int(time.time() * 1000)
        info["timestamp"] = timestamp
        info["sign"] = md5mgr.mkmd5fromstr("%s%s%s" % (content, timestamp, "gCvKaE0tTcWtHsPkbRdE"))
        info["partnerIds"] = "all"
        info["content"] = content

        server = server_business.get_server(server_id=server_id)
        url = "http://" + server.server_host + ":8088//gameApi/sendSysMsg.do"
        success, ret_val = http_util.request(url, info)
        if success:
            result = json.loads(ret_val)
            return result.get("rt") == 1000

    
    def get_notice_list(self):
        return notice_business.get_effect_notice_list()
            
if __name__ == "__main__":
    s = single_process.SingleProcess("notice_sender")
    s.check()
    executor = NoticeSender()
    executor.start()
    #executor.send_notice_to_server("t2", "中文走马灯")

    
