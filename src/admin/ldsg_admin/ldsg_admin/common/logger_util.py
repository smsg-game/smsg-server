#!/usr/bin/python
# -*- coding: utf-8 -*-

from ldsg_admin.common import get_logging

def debug(msg):
    logger = get_logging()
    logger.debug(msg)
    
def info(msg):
    logger = get_logging()
    logger.info(msg)

def error(msg):
    logger = get_logging()
    logger.error(msg)
    
    
if __name__ == "__main__":
    debug("test")