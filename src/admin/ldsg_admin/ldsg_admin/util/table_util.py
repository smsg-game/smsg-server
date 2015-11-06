#!/usr/bin/python
# -*- coding: utf-8 -*-

def get_table_index(s):
    h = get_hash_code(s)
    return h % 128

def get_hash_code(st):
    h = 0;
    for s in st:
        h = 31 + h + ord(s)
    return abs(long(h))
