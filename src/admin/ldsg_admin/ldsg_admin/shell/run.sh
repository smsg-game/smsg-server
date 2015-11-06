#!/bin/sh
sid=$1

ps -ef | grep /main-server.jar | grep config-$sid |  grep -v grep | cut -c 9-15 | xargs kill -9
ps -ef | grep /logic-server.jar | grep config-$sid | grep -v grep | cut -c 9-15 | xargs kill -9


sudo -u root java -Dfile.encoding=utf-8 -Duser.language=en -Duser.country=US -cp .:/data/config-$sid/main-server:/data/config-$sid/main-server:/data/deploy/main-server.jar Start file:/data/config-$sid/main-server/applicationContext.xml $sid > /dev/null& 


sleep 2

sudo -u root java -Dfile.encoding=utf-8 -Duser.language=en -Duser.country=US -cp .:/data/config-$sid/logic-server:/data/deploy/logic-server.jar:/data/config-$sid/logic-server/ com.lodogame.ldsg.server.LogicServer file:/data/config-$sid/logic-server/applicationContext.xml $sid > /dev/null&


if [ "$2" = "restart" ] ;then

    rm /data/www/game-api* -rf

    cp /data/deploy/game-api.war /data/www/


    chown content:content /data/www -R

    if [ -f /etc/init.d/resin ];then

       /etc/init.d/resin restart
    else
       /app/soft/resin-game/bin/init restart
    fi
fi

echo success

