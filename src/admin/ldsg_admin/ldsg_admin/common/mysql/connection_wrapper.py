#-*- coding:utf-8 -*-
from threading import Lock

import MySQLdb
from MySQLdb.converters import conversions
from _mysql_exceptions import MySQLError, Warning, Error, InterfaceError, DataError, \
     DatabaseError, OperationalError, IntegrityError, InternalError, \
     NotSupportedError, ProgrammingError

from cursor_wrapper import Cursor
from reserve_convertor import ReserveLiteral

class Connection(object):
    def __init__(self, *args, **kwargs):
        """please use host, port, db, user, passwd etc. kwargs to init Connection"""
        if not kwargs.has_key("charset"):
            kwargs["charset"] = "utf8"
        if not conversions.has_key(ReserveLiteral):
            conversions[ReserveLiteral] = ReserveLiteral.convertor
        kwargs["conv"] = conversions
        self._authInfo = (args, kwargs)
        self._connection = None
        self._locker = Lock()

    def __del__(self):
        self._close()
        
    def close(self):
        self._locker.acquire()
        try:
            self._close()
        finally:
            self._locker.release()
    
    def is_connected(self):
        if self._connection:
            try:
                self._connection.ping()
                return True
            except MySQLdb.DatabaseError:
                pass
        self._close()
        return False
    
    def ensure_connected(self):
        self._locker.acquire()
        try:
            if not self.is_connected():
#                if DEBUG:
#                    c = cache.get("connection_wrapper")
#                    if c is None:
#                        c = 1
#                    else:
#                        c += 1
#                    cache.set("connection_wrapper", c)
                self._connection = MySQLdb.Connection(*self._authInfo[0], **self._authInfo[1])
                self._connection.autocommit(True)
        finally:
            self._locker.release()
        
    def cursor(self, check_conn = True):
        if check_conn or self._connection is None:
            self.ensure_connected()
        c = self._connection.cursor()
        return Cursor(c, self)
            
    def clone(self):
        conn = Connection(*self._authInfo[0], **self._authInfo[1])
        return conn
    
    def _close(self):
        if self._connection:
            try:
                self._connection.close()
            except:
                pass
            self._connection = None

    def __getattr__(self, name):
        return getattr(self._connection, name)

    MySQLError = MySQLError
    Warning = Warning
    Error = Error
    InterfaceError = InterfaceError
    DatabaseError = DatabaseError
    DataError = DataError
    OperationalError = OperationalError
    IntegrityError = IntegrityError
    InternalError = InternalError
    ProgrammingError = ProgrammingError
    NotSupportedError = NotSupportedError
        