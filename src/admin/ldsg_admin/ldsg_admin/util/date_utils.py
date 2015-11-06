#coding=utf-8
'''
日期工具类
Created on 2013-6-27

@author: chenjian
'''

import time
import datetime
 
def timestamp_to_str(timestamp, fmt="%Y-%m-%d %H:%M:%S"):
    """
    @@时间戳转换为字符串
    @param timestamp:
    @param fmt:  
    """
    return time.strftime(fmt, time.localtime(timestamp))

def datetime_to_timestamp(date_time):
    """
    @@datetime转换为时间戳
    @param date_time: 
    """
    return time.mktime(date_time.timetuple())  

def datetime_to_string(date_time, fmt="%Y-%m-%d %H:%M:%S"):
    """
    @@转换时间为字符串
    """
    timestamp = datetime_to_timestamp(date_time)
    return timestamp_to_str(timestamp, fmt)
    
def string_to_datetime(date_str, fmt="%Y-%m-%d %H:%M:%S"):  
    """
    @@字符串转换为datetime
    @param date_str:
    @param fmt:  
    """
    return datetime.datetime.strptime(date_str, fmt)  

def get_date_str_by_day(date_str, days, fmt="%Y-%m-%d %H:%M:%S"):
    """
    @@获取日期date_str间隔days日的日期
    """
    date_time = string_to_datetime(date_str, fmt)
    ret_date = date_time + datetime.timedelta(days=days)
    return timestamp_to_str(datetime_to_timestamp(ret_date), fmt)

def get_datetime_str_by_day(date_time, days, fmt="%Y-%m-%d %H:%M:%S"):
    """
    @@获取日期datetime间隔days日的日期
    """
    ret_date = date_time + datetime.timedelta(days=days)
    return timestamp_to_str(datetime_to_timestamp(ret_date), fmt)

def get_yestoday(date_time):
    """
    @@根据datetime获取前一日的日期对象
    """
    yestoday = date_time + datetime.timedelta(days=-1)
    return yestoday

def get_tomorrow(date_time):
    tomorrow = date_time + datetime.timedelta(days=+1)
    return tomorrow

def get_yestoday_str_by_str(date_str, fmt="%Y-%m-%d %H:%M:%S"):
    """
    @@根据字符串获得前一天日期的字符串
    @param date_str:
    @param fmt:  
    """
    date_time = string_to_datetime(date_str, fmt)
    yes_time = get_yestoday(date_time)
    timestamp = datetime_to_timestamp(yes_time)
    return timestamp_to_str(timestamp, fmt)

def get_tomorrow_str_by_str(date_str, fmt="%Y-%m-%d %H:%M:%S"):
    date_time = string_to_datetime(date_str, fmt)
    tomorrow_time = get_tomorrow(date_time)
    timestamp = datetime_to_timestamp(tomorrow_time)
    return timestamp_to_str(timestamp, fmt)

def get_date():
    now = time.strftime("%Y-%m-%d")
    return now

def get_time():
    now = datetime.datetime.now() 
    return str(now)

def get_string_date(date):
    try:
        t = time.strptime(date, "%Y-%m-%d")
        y,m,d = t[0:3]
        t1=datetime.datetime(y,m,d)
        d3 = t1 + datetime.timedelta()
        return d3.strftime('%Y-%m-%d')
    except Exception,ex:
        print "日期格式不对，格式：yyyy-MM-dd"
        print ex

if __name__ == "__main__":
    print get_date_str_by_day("2013-08-13", -3, "%Y-%m-%d")
    