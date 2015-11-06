#!/usr/bin/python
# -*- coding: utf-8 -*-

import traceback

import urllib
from optparse import isbasestring
from ldsg_admin.common import get_logging

logging = get_logging()

def request(url, params):
    """http request"""
    for k, v in params.iteritems():
        if isbasestring(v):
            params[k] = v.encode("utf8")
        else:
            params[k] = v
    
    try:
        url = url + "?" + urllib.urlencode(params)
        logging.debug(url)
        ret_val = urllib.urlopen(url).read()
        return True, ret_val
    except:
        logging.error(traceback.format_exc())
        
    return False, None