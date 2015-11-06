#!/usr/bin/python
# -*- coding: utf-8 -*-
"""Service proxy define"""


from ldsg_admin.common.jsonrpclib import ServerProxy
from ldsg_admin.config import DEBUG

if DEBUG:
    SERVICE_HOST = "http://127.0.0.1:9000/"
else:
    SERVICE_HOST = "http://admin.sg.fantingame.com/"

client_proxy = ServerProxy("%sservices/client/" % SERVICE_HOST)


