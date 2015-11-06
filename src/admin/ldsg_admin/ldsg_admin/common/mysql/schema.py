#coding=utf-8
class DatabaseSchema(object):
    database_name = "gateway"
    tables = {}

class TableSchema(object):
    def __init__(self, table_name, model_class):
        self._table_name = table_name
        self._fields = {}
        self._primary_key = []
        self._model_class = model_class
    
    @property
    def table_name(self):
        return self._table_name
    @property
    def fields(self):
        return self._fields
    @property
    def primary_key(self):
        return self._primary_key
    @property
    def model_class(self):
        return self._model_class

class NOT_PROVIDED:
    pass

class Field:
    def __init__(self, db_type = None, 
                 max_length = None, 
                 db_index = False, 
                 null = True, 
                 primary_key = False,
                 unique = False,
                 default = NOT_PROVIDED):
        
        self.db_type = db_type
        self.max_length = max_length
        self.primary_key = primary_key
        self.name = None
        self.db_index = db_index
        self.null = null
        self.default = NOT_PROVIDED

class BooleanField(Field):
    pass

class CharField(Field):
    pass

class DateField(Field):
    pass

class DateTimeField(Field):
    pass

class DecimalField(Field):
    pass

class FloatField(Field):
    pass

class IntegerField(Field):
    pass

class NullBooleanField(Field):
    pass

class PositiveIntegerField(Field):
    pass

class PositiveSmallIntegerField(Field):
    pass

class SmallIntegerField(Field):
    pass

class TextField(Field):
    pass

class TimeField(Field):
    pass

        