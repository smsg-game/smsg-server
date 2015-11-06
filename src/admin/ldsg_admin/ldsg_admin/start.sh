#!/bin/bash

#rdate -s time.iastate.edu

#./memcached -d -m 1024 -p 11212 -u root

# Replace these three settings.
PROJDIR="/data/apps/ldsg_admin/ldsg_admin"
PIDFILE="$PROJDIR/fastcgi.pid"

python2.7 $PROJDIR/web/manage.py runfcgi method=prefork maxrequests=1000 \
	maxspare=50 minspare=10 host=127.0.0.1 port=8090 pidfile=$PIDFILE

echo "python manage.py"
ps -eLf | grep python2.7 |grep manage.py |wc -l
ps -eLf | grep nginx
