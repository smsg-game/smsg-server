#!/bin/sh
sid=$1
echo $sid
#cat /data/run-$sid/ldsg-battle.pid | xargs -i kill -9 {}
ps -ef | grep com.ldsg.battle.App | grep -v grep | cut -c 9-15 | xargs kill -9

rm /data/run-$sid/ldsg-battle.pid -rf

sudo -u root java -Dfile.encoding=utf-8 -Duser.language=en -Duser.country=US -cp .:/data/config-$sid/ldsg-battle:/data/deploy_battle/ldsg-battle.jar com.ldsg.battle.App > /dev/null&

echo success
