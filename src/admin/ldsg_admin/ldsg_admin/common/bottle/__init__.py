from ldsg_admin.common import memcache


caceh = {}

try:
    from ldsg_admin.common import cache
except:    
    cache = memcache.Client(["127.0.0.1:12000"])