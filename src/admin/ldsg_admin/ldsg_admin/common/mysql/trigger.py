#-*- coding:utf-8 -*-

TRIGGERS = {}
REGISTRIED = False
def registy_trigger(table, operator, func):
    funcs = TRIGGERS.get(table)
    if not funcs:
        funcs = []
        TRIGGERS[table] = funcs
    funcs.append((operator, func))

def get_triggers(table, operator):
    funcs = TRIGGERS.get(table, [])
    result = []
    for func in funcs:
        if func[0] == operator:
            result.append(func[1])
    return result