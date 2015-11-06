#!/usr/bin/python
# -*- coding: utf-8 -*-

import sys
import os
import traceback
import time
from datetime import datetime
from subprocess import Popen, PIPE
import threading

from ldsg_admin.common import single_process
from ldsg_admin.client.base_client import BaseClient, ensure_success
from ldsg_admin.client.rpc_proxy import client_proxy
from ldsg_admin.constants.command_type import CommandType
from ldsg_admin.config import PathSettings

class WorkThread(threading.Thread):
    """工作线程"""
    def __init__(self, func, *args, **kwargs):
        threading.Thread.__init__(self)
        self.func = func
        self.args = args
        self.kwargs = kwargs
        
    def run(self):
        self.func(*self.args, **self.kwargs)

class ServerUpdator(BaseClient):
    
    CLIENT_NAME = "server_updator"
    
    def __init__(self, server_ids=[]):
        super(self.__class__, self).__init__(self.__class__.CLIENT_NAME)
        self._log = None
        self.server_ids = server_ids
        self.service = client_proxy
        self.go = True
   
    def before_run(self):
        self.client_id = self.register()
        
    def run(self):
        self.start_work()
        while True:
            try:
                self.report_status()
                time.sleep(5)
            except KeyboardInterrupt:
                self.go = False
                raise
                
    @ensure_success    
    def report_status(self):
        self.log("report status")
        self.service.report_status(self.client_id)
         
    def call_cmd(self, cmd, *args):
        """call cmd """
        success = self.__call_cmd(cmd, args)
        return success
   
    def start_work(self):
        self.work_thread = WorkThread(self.mainbody)
        self.work_thread.setName('WorkThread.%s.%s.client_%s' % \
                                 (self.CLIENT_NAME, self.__class__.__name__, self.client_id))
        self.work_thread.setDaemon(False)
        self.work_thread.start()

    def mainbody(self):
        """主工作方法"""
        while self.go:
            try:
                command = self.get_command()
                if command:
                    self.log("get task[%s]" % command)
                    self.handle_command(command)
                else:
                    self.log("not task")
            except KeyboardInterrupt:
                raise
            except:
                self.log(traceback.format_exc())
                self._log = traceback.format_exc()
                
            time.sleep(5)
                
    def handle_command(self, command):
        command_type = command["command_type"]
        results = []
        extinfo = {}
        param = None
        now = datetime.now()
        if command_type == CommandType.DEPLOY_APP:#发布程序
            cmd = os.path.join(PathSettings.PROJECT_FOLDER, "shell", "deploy_game.sh")
            param = command["version"]
            extinfo = {"version": param}
            
        elif command_type == CommandType.RUN:#运行
            cmd = os.path.join(PathSettings.PROJECT_FOLDER, "shell", "run.sh")
 
        elif command_type == CommandType.RUN_RESTART_RESIN:#运行
            cmd = os.path.join(PathSettings.PROJECT_FOLDER, "shell", "run.sh")
            param = "restart"
                
        elif command_type == CommandType.STOP:#停止
            cmd = os.path.join(PathSettings.PROJECT_FOLDER, "shell", "stop.sh")

        elif command_type == CommandType.DEPLOY_SQL:
            cmd = os.path.join(PathSettings.PROJECT_FOLDER, "shell", "deploy_sql.sh")
            param = command["file_name"]
            extinfo = {"file_name": param}
        
        elif command_type == CommandType.DEPLOY_BATTLE:
            cmd = os.path.join(PathSettings.PROJECT_FOLDER, "shell", "deploy_battle.sh")
            param = command["version"]
            extinfo = {"version": param}
        
        elif command_type == CommandType.RUN_BATTLE:
                cmd = os.path.join(PathSettings.PROJECT_FOLDER, "shell", "run_battle.sh")

        for server_id in server_ids:
            if param:
                ret, output = self.call_cmd(cmd, server_id, param)
                results.append([now, " ".join([cmd, server_id, param]), ret])
            else:
                ret, output = self.call_cmd(cmd, server_id)
                results.append([now, " ".join([cmd, server_id]), ret])
                
            self.report_log(server_id, ret, command_type, output, extinfo)
            
            if command_type == CommandType.RUN_BATTLE:
                break     
        

        #上报执行结果
        self.report_result(results)
    
    @ensure_success
    def report_result(self, results):
        return self.service.report_result(self.client_id, results)
    
    @ensure_success
    def register(self):
        return self.service.register(self.CLIENT_NAME, ",".join(self.server_ids))
                
    @ensure_success
    def get_command(self):
        return self.service.get_command(self.client_id)
    
    @ensure_success
    def report_log(self, server_id, ret, command_type, output, params={}):
        self.service.report_log(self.client_id, server_id, ret, command_type, output, params)
        
    def __call_cmd(self, cmd, params=[]):
        """调用命令"""
        try:
            line = None
            output = ""
            cmds = [cmd]
            cmds.extend(params)
            self.log("execute[%s]" % " ".join(cmds))
            p = Popen(cmds, stdout=PIPE)
            while True:
                line = p.stdout.readline()
                output = "%s%s" % (output, line)
                if not line:
                    break
                self.log(line.replace("\n", ""))
                
            if p.wait() == 0:
                self.log("call %s success" % cmd)
                return 1, output
                       
            self.log("call %s failure" % cmd)
            return 0, output
        except:
            self.log(traceback.format_exc())
            return 0, traceback.format_exc()

if __name__ == "__main__":
    s = single_process.SingleProcess("server_updator")
    s.check()
    if len(sys.argv) > 1:
        server_ids = sys.argv[1].split(",")
    else:
        server_ids = ["t2"]
    executor = ServerUpdator(server_ids)
    executor.start()
    
    
    
