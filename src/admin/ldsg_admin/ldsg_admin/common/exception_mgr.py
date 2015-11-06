#!/usr/bin/python
# -*- coding: utf-8 -*-
"""异常处理
"""
import traceback
import sys
import os
from datetime import datetime

from filelocker import FileLocker
from ldsg_admin.config import PathSettings, DEBUG
from ldsg_admin.common import get_logging
import file_utility

class ExceptionManager:
    MaxLogSize = 1024 * 1024 * 20
    def __init__(self, logDir = None):
        self._logDir = logDir
        if self._logDir == None:
            self._logDir = PathSettings.EXCEPTION
    
    def format_except(self, msg = None, track_index = 0):
        try:
            etype, value, tb = sys.exc_info()
            if etype == SystemExit:
                raise
            if not tb:
                return
            
            for i in range(track_index):
                if tb.tb_next:
                    tb = tb.tb_next
                else:
                    break
            s = ''.join(traceback.format_exception(etype, value, tb, 10))
            if tb and tb.tb_frame:
                local_variables = tb.tb_frame.f_locals
                s += "local_variables:%s" % os.linesep
                for name, var in local_variables.iteritems():
                    var_r = '%r' % var
                    if len(var_r) > 100:
                        var_r = var_r[:100] + '...'
                    s += '%s: %s%s' % (name, var_r, os.linesep)
                s += "sys.path:%r%s" % (sys.path, os.linesep)
            if msg:
                s += msg + os.linesep
            return s
        except SystemExit:
            raise
        except Exception, e:
            self._print(e)
            return None
    
    def on_except(self, msg = None, track_index = 0):
        s = None
        try:
            etype, value, tb = sys.exc_info()
            if etype == SystemExit:
                raise
            if not tb:
                return
            f = tb.tb_frame
            co = f.f_code
            
            for i in range(track_index):
                if tb.tb_next:
                    tb = tb.tb_next
                    co = tb.tb_frame.f_code
                else:
                    break
            s = ''.join(traceback.format_exception(etype, value, tb, 10))
            if tb and tb.tb_frame:
                local_variables = tb.tb_frame.f_locals
                s += "local_variables:%s" % os.linesep
                for name, var in local_variables.iteritems():
                    var_r = '%r' % var
                    if len(var_r) > 100:
                        var_r = var_r[:100] + '...'
                    s += '%s: %s%s' % (name, var_r, os.linesep)
                s += "sys.path:%r%s" % (sys.path, os.linesep)
            code_path = co.co_filename
            if code_path.startswith(PathSettings.PROJECT_FOLDER):
                code_path = code_path[len(PathSettings.PROJECT_FOLDER):]
                if code_path[0] in ("\\", "/"):
                    code_path = code_path[1:]
                filename = code_path.replace("\\", ".").replace("/", ".")
            else:
                filename = os.path.basename(code_path)
            print filename
            logPath = self._getLogPath("error.log")
            locker = FileLocker(logPath)
            locker.lock()
            try:
                if os.path.isfile(logPath) and os.path.getsize(logPath) >= ExceptionManager.MaxLogSize:
                    self._backupLog(logPath)
                logFile = file(logPath, "ab")
                try:
                    now = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
                    logFile.write("========================%s%s%s" % (os.linesep, now, os.linesep))
                    logFile.write(s)
                    if msg:
                        if DEBUG:
                            print msg
                        logFile.write(msg)
                        logFile.write(os.linesep)
                    logFile.write("========================" + os.linesep)
                finally:
                    logFile.close()
            finally:
                locker.unlock()
        except SystemExit:
            raise
        except Exception, e:
            self._print(e)
        self._print(s)
        return s
    
    def _backupLog(self, logPath):
        bakPath1 = logPath + ".1"
        bakPath2 = logPath + ".2"
        if os.path.exists(bakPath1):
            if os.path.exists(bakPath2):
                os.remove(bakPath2)
            os.rename(bakPath1, bakPath2)
        os.rename(logPath, bakPath1)
    
    def _getLogPath(self, fileName):
        if not fileName:
            fileName = "except.log"
        else:
            fileName = os.path.splitext(fileName)[0] + ".log"
        file_utility.ensure_dir_exists(self._logDir)
        return os.path.join(self._logDir, fileName)
    
    def _print(self, msg):
        try:
            print str(msg)
        except:
            pass

__except_obj = ExceptionManager()
def on_except(msg = None, track_index = 0):
    return __except_obj.on_except(msg, track_index)

def format_except(msg = None, track_index = 0):
    return __except_obj.format_except(msg, track_index)

def test_except():
    a = 'aaaaa'
    b = range(100)
    c = 1 / 0
    
if __name__ == '__main__':
    try:
        test_except()
    except:
        on_except(track_index=1)