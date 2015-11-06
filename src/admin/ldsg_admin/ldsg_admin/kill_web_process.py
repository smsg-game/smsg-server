#!/usr/bin/env python
# -*- coding: utf-8 -*-
from subprocess import Popen, call, PIPE
import time


def main():
    out, err = Popen(['ps -ef | grep manage.py'], stdout=PIPE, close_fds=True, shell=True).communicate()
    for l in out.split('\n'):
        l = l.strip()
        if l and 'grep' not in l:
            call(['kill', '-9', l.split()[1]])
    time.sleep(1)
            

if __name__ == '__main__':
    main()