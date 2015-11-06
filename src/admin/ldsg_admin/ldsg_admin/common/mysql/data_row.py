#-*- coding:utf-8 -*-

class DataRow(object):
    """Wrapper for db result(tuple). Support for d[index] or d['column'] access"""
    def __init__(self, t, column_map):
        self._data = t
        self._column_map = column_map

    def __getitem__(self, name):
        if isinstance(name, basestring):
            name = self._column_map[name]
        return self._data[name]

    def __getslice__(self, i, j):
        return self._data[i: j]
        
    def __iter__(self):
        for k in self._data:
            yield k
    
    def columns(self):
        return self._column_map.keys()
    
    def values(self):
        return self._data

    def items(self):
        for k in self._column_map:
            yield k, self[k]

    def has_column(self, column):
        return self._column_map.has_key(column)

    def to_dict(self):
        """return a dict"""
        d = {}
        for k, v in self.items():
            d[k] = v
        return d
    
    def __contains__(self, value):
        return value in self._data
    
    def __len__(self):
        return len(self._data)
    
    def __str__(self):
        return "type:DataRow\n" + str(self.to_dict())

class DataRowCollection(object):
    def __init__(self, rows, column_map):
        self._rows = rows
        self._column_map = column_map

    def __getitem__(self, i):
        return DataRow(self._rows[i], self._column_map)

    def __getslice__(self, i, j):
        slice_rows = self._rows[i: j]
        result = []
        for r in slice_rows:
            data_row = DataRow(r, self._column_map)
            result.append(data_row)
        return result
    
    def __iter__(self):
        for row in self._rows:
            yield DataRow(row, self._column_map)
                
    def __len__(self):
        return len(self._rows)
    
    def __contains__(self, value):
        if isinstance(value, DataRow):
            value = value.values()
        return value in self._rows
    
    def to_list(self):
        """return a list, and DataRow will be a dict"""
        return [r.to_dict() for r in self]
            

    