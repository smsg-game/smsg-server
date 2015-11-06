# -*- coding: utf-8 -*-
import ldsg_admin.common.filelocker as fileLocker

class SingleProcess:
    def __init__(self, uniqueProcessName):
        self.locker = fileLocker.FileLocker("ensure1Instance%s" % uniqueProcessName)

    def check(self):
        try:
            self.locker.lock(True)
        except fileLocker.HasBeenLockedError:
            import sys
            sys.exit()
    
    def __del__(self):
        self.locker.unlock()

if __name__ == "__main__":
    s = SingleProcess("test")
    s.check()