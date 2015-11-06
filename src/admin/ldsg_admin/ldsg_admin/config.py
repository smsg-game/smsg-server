#!/usr/bin/python
# -*- coding: utf-8 -*-
"""project settings"""

import os

DEBUG = False
#1 大陆  2 TW
SERVER_TYPE = 1

USERNAME = "admin"
PASSWORD = "123456"

class PathSettings:
    if os.name == 'nt':
        WORKING_FOLDER = "D:/data/appdatas/ldsg_admin_working"
    else:
        WORKING_FOLDER = "/data/appdatas/ldsg_admin_working"

    LOG = WORKING_FOLDER + "/logs"
    SCRIPT = WORKING_FOLDER + "/script"
    SCRIPT_BACKUP = WORKING_FOLDER + "/script_backup"
    FILE_LOCKER = WORKING_FOLDER + "/file_locker"
    EXCEPTION = WORKING_FOLDER + "/exception"
    
    if not os.path.exists(LOG):
        os.makedirs(LOG)
    if not os.path.exists(SCRIPT):
        os.makedirs(SCRIPT)
    if not os.path.exists(FILE_LOCKER):
        os.makedirs(FILE_LOCKER)
    if not os.path.exists(SCRIPT_BACKUP):
        os.makedirs(SCRIPT_BACKUP)
    if not os.path.exists(EXCEPTION):
        os.makedirs(EXCEPTION)


    PROJECT_FOLDER = os.path.dirname(os.path.abspath(__file__))
    
class DjangoSettings:
    URL_PREFIX = ''
    FILE_UPLOAD_TEMP_DIR = PathSettings.WORKING_FOLDER + "/file_upload_temp"
    if not os.path.exists(FILE_UPLOAD_TEMP_DIR):
        os.makedirs(FILE_UPLOAD_TEMP_DIR)
    WEB_ROOT = PathSettings.WORKING_FOLDER + '/www' # static-generator
    if not os.path.exists(WEB_ROOT):
        os.makedirs(WEB_ROOT)
    if not os.path.exists(WEB_ROOT + '/rrd'): # rrd image path
        os.makedirs(WEB_ROOT + '/rrd')
    
DB_Mysql = {
    'db': 'ldsg_game_admin',
    'user': 'root',
    'passwd': 'allen_liu',
    'host': '127.0.0.1'      
}
GOLD_TYPE = 1
GOLD_ID = 1
COPPER_TYPE = 2
COPPER_ID = 2
