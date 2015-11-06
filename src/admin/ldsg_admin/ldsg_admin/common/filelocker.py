#!/usr/bin/python
# -*- coding: utf-8 -*-


import os

from ldsg_admin.config import PathSettings
import file_utility
from threading import local

if os.name == 'nt':
    import win32con
    import win32file
    import pywintypes
elif os.name == 'posix':
    import fcntl

else:
    raise RuntimeError("FileLocker only defined for nt and posix platforms")

class HasBeenLockedError(Exception):pass

class FileLocker(local):
    def __init__(self, lockerName, expiredSeconds = 0):
        import md5mgr
        lockerName = repr(lockerName)
        if os.name == 'nt':
            lockerName = lockerName.lower()
        md5 = md5mgr.mkmd5fromstr(lockerName)
        lockerFolder = os.path.join(PathSettings.FILE_LOCKER, md5[:2])
        self.__lockerFilePath = os.path.join(lockerFolder, md5)
        self.__expiredSeconds = expiredSeconds
        self.__file = None
        self.__isLocked = False
        file_utility.ensure_dir_exists(lockerFolder)

    def lock(self, breakOnFailed = False):
        self.__file = file(self.__lockerFilePath, "w")
        self.__lock(breakOnFailed)
        self.__isLocked = True
        if self.__expiredSeconds > 0:
            import threading
            t = threading.Thread(target = self.__daemonMethod)
            t.setDaemon(True)
            t.start()
    acquire = lock
    
    __enter__ = acquire
    
    def unlock(self):
        if not self.__isLocked: return
        self.__isLocked = False
        if not self.__file: return
        self.__unlock()
        self.__file.close()
        self.__file = None
    release = unlock
    
    def __exit__(self, typ, val, tb):
        self.release()
    
    def __del__(self):
        self.unlock()
    
    def __daemonMethod(self):
        waitSeconds = 0
        import time
        while self.__isLocked and waitSeconds < self.__expiredSeconds:
            waitSeconds += 2
            time.sleep(2)
        if self.__isLocked:
            self.unlock()

    if os.name == 'nt':
        __overlapped = pywintypes.OVERLAPPED()
        def __lock(self, breakOnFailed):
            lockType = win32con.LOCKFILE_EXCLUSIVE_LOCK
            if breakOnFailed:
                lockType = lockType | win32con.LOCKFILE_FAIL_IMMEDIATELY
            hfile = win32file._get_osfhandle(self.__file.fileno())
            try:
                win32file.LockFileEx(hfile, lockType, 0, 0xf0, FileLocker.__overlapped)
            except pywintypes.error:
                self.__file.close()
                self.__file = None
                raise HasBeenLockedError("Locker has been acquired before")
    
        def __unlock(self):
            hfile = win32file._get_osfhandle(self.__file.fileno())
            win32file.UnlockFileEx(hfile, 0, 0xf0, FileLocker.__overlapped)
    
    elif os.name =='posix':
        def __lock(self, breakOnFailed):
            lockType = fcntl.LOCK_EX
            if breakOnFailed:
                lockType = lockType | fcntl.LOCK_NB
            try:
                fcntl.flock(self.__file.fileno(), lockType)
            except IOError, e:
                self.__file.close()
                self.__file = None
                import errno
                if e.errno == errno.EACCES or e.errno == errno.EAGAIN:
                    raise HasBeenLockedError("Locker has been acquired before")
                raise
    
        def __unlock(self):
            fcntl.flock(self.__file.fileno(), fcntl.LOCK_UN)

if __name__ == '__main__':
    i = 0
    while i < 10000:
        locker = FileLocker('testLocker._tmp_%s' % i)
        locker.lock(True)
#        import time
        print "sleep 5 ..."
#        time.sleep(1)
        locker.unlock()
        i += 1 
    print "end"
    