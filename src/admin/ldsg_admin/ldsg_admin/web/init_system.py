#!/usr/bin/env python

def init():
    import os
    import sys
    #add project path
    _local_folder = os.path.dirname(os.path.dirname(os.path.realpath(__file__)))
    sys.path.insert(0, os.path.dirname(_local_folder))
    project_module = __import__(os.path.basename(_local_folder), {}, {}, [''])
    sys.modules["ldsg_admin"] = project_module
    del sys.path[0]

    #set django path
    for k in [k for k in sys.modules if k.startswith('django')]:
        del sys.modules[k]
    import ldsg_admin.common.django as _local_django
    sys.modules["django"] = _local_django
    
    #set flup path
    for k in [k for k in sys.modules if k.startswith('flup')]:
        del sys.modules[k]
    from ldsg_admin.common import flup as _local_flup
    sys.modules["flup"] = _local_flup

if __name__ == '__main__':
    init()
    import django
    print django
    from ldsg_admin import web
    import flup
    print flup