#-*- coding:utf-8 -*-

from weakref import ref

import MySQLdb
from _mysql_exceptions import OperationalError

from schema import DatabaseSchema
from data_row import DataRow, DataRowCollection


class Cursor(object):
    def __init__(self, db_cursor, conn):
        self._cursor = db_cursor
        self._conn = ref(conn)
        self._pool = None
        self._closed = False
    
    def __del__(self):
        self.close()
        
    def close(self):
        if not self._closed:
            if self._cursor:
                self._cursor.close()
            if self._conn:
                conn = self._conn
                self._conn = None
                if self._pool:
                    self._pool.release(conn)
        self._closed = True
    
    def __enter__(self):
        return self
    
    def __exit__(self, typ, val, tb):
        self.close()
    
    def get_db_time(self):
        sql = "select now()"
        return self.fetchone(sql)[0]
    
    def literal(self, params):
        return self._cursor.connection.literal(params)

    def autocommit(self, value = True):
        self._cursor.connection.autocommit(value)

    def execute(self, sql, params = ()):
        try:
            while self._cursor.nextset(): pass
            if params:
                return self._cursor.execute(sql, params)
            else:
                return self._cursor.execute(sql)
        except OperationalError, e:
            if e.args[0] == 2006 and e.args[1].find("MySQL server has gone away") > -1:
                if self._conn:
                    self._conn().close()
            raise
    
    def executemany(self, query, args):
        try:
            while self._cursor.nextset(): pass
            return self._cursor.executemany(query, args)
        except OperationalError, e:
            if e.args[0] == 2006 and e.args[1].find("MySQL server has gone away") > -1:
                if self._conn:
                    self._conn().close()
            raise
    
    def fetchone(self, sql = None, params = ()):
        if sql:
            self.execute(sql, params)
        rs = self._cursor.fetchone()
        if rs:
            desc = self._cursor.description
            column_map = dict((d[0], i) for i, d in enumerate(desc))
            return DataRow(rs, column_map)
        return rs
    
    def fetchall(self, sql = None, params = ()):
        if sql:
            self.execute(sql, params)
        rs = self._cursor.fetchall()
        if rs:
            desc = self._cursor.description
            column_map = dict((d[0], i) for i, d in enumerate(desc))
            result = DataRowCollection(rs, column_map)
            return result
        return rs
    
    def update(self, data, table, key):
        """"""
        if not data:
            return 0
        if isinstance(key, basestring):
            keys = (key, )
        else:
            keys = key
        
        if isinstance(data, dict):
            datas = (data,)
        else:
            datas = data
        columns = []
        values = []
        for k in datas[0].keys():
            if k not in keys:
                columns.append(k)
        if not columns:
            return 0
        for d in datas:
            i = [d[k] for k in columns]
            for key in keys:
                i.append(d[key])
            values.append(i)
            
        items = ['%s=%%s' % k for k in columns]
        sql = 'update %s set %s where %s' % \
            (table, ','.join(items), ' and '.join(['%s=%%s' % k for k in keys]))
        return self.executemany(sql, values)
    
    def insert(self, data, table, hold_duplicated=False, skip_duplicated_columns=[]):
        """Insert数据到数据库，返回lastrowid
        
        data可以是字典或者字典列表，若是字典列表，则批量Insert
        """
        if not data:
            return 0, 0
        if isinstance(data, dict):
            datas = (data,)
        else:
            datas = data
        columns = []
        values = []
        for k in datas[0].keys():
            columns.append(k)
        if not columns:
            return 0, 0
        for d in datas:
            values.append([d[k] for k in columns])
        sql = 'insert into %s (%s) values(%s)' % (table, 
            ','.join(columns), ','.join(['%s' for k in columns]))
        if hold_duplicated:
            on_duplicated = []
            if skip_duplicated_columns == "skip_all":
                on_duplicated.append("%s=%s" % (columns[0], columns[0]))
            else:
                for column in columns:
                    if not column in skip_duplicated_columns:
                        on_duplicated.append("%s=values(%s)" % (column, column))
            if not on_duplicated:
                on_duplicated.append("%s=%s" % (columns[0], columns[0]))
            sql = '%s ON DUPLICATE KEY update %s' % (sql, ','.join(on_duplicated))

        row_effected = self.executemany(sql, values)
        return self._cursor.lastrowid, row_effected

    def _to_dict(self, obj):
        if type(obj) == dict:
            return obj
        else:
            return obj.to_dict()

    def _db_result2obj(self, rs, columns, model_class):
        result = []
        columnCount = len(columns)
        for r in rs:
            obj = model_class()
            index = 0
            while index < columnCount:
                setattr(obj, columns[index], r[index])
                index += 1
            result.append(obj)
        return result
    
    def _check_param(self, obj, table):
        model_class = None
        t = table
        is_dict = (type(obj) == dict)
        if not is_dict:
            model_class = obj.table_schema.model_class
            if not table:
                t = obj.table_schema.table_name
        elif isinstance(table, basestring):
            table_schema = DatabaseSchema.tables[table]
            model_class = table_schema.model_class
        else:
            msg = 'table is required while obj is dict'
            raise MySQLdb.ProgrammingError(msg)
        return model_class, t
    
    def _add2pool(self, pool):
        self._pool = pool
        self._conn = self._conn()
    
    def __getattr__(self, name):
        return getattr(self._cursor, name)
    