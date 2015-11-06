#!/usr/bin/python
# -*- coding: utf-8 -*-

import xlrd

def parse_excel(path, index=0):
    
    workbook = xlrd.open_workbook(path)
    sheet = workbook.sheet_by_index(index)  
    
    datas = []
    column_names = []
    for row_index in range(sheet.nrows):  
        ds = {}
        for col_index in range(sheet.ncols):  
            value = sheet.cell(row_index,col_index).value
            if row_index == 0:
                column_name = get_column_name(value)
                column_names.append(column_name)        
            else:
                column_name = column_names[col_index]
                ds[column_name] = value
        
        if ds:
            datas.append(ds)
        
    return datas
    
def get_column_name(value):
    return value[value.find("(")+1:value.find(")")]


if __name__ == "__main__":
    datas = parse_excel("D:\\battle_app\\input\\npc.xls")
    for d in datas:
        print d