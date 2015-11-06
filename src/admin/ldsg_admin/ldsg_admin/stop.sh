#!/bin/bash

# Replace these three settings.
PROJDIR="/data/apps/ldsg_admin/ldsg_admin"
PIDFILE="$PROJDIR/fastcgi.pid"

cd $PROJDIR
if [ -f $PIDFILE ]; then
    kill -9 `cat -- $PIDFILE`
    rm -f -- $PIDFILE
fi

python "$PROJDIR/kill_web_process.py"

echo "python manage.py"
ps -eLf | grep python |grep manage.py |wc -l
ps -eLf | grep nginx
