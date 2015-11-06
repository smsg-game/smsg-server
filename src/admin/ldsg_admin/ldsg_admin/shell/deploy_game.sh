#!/bin/sh
cd /data/deploy
rm /data/deploy/* -rf

wget ftp://ftpchibi:xjK4kPKUusCO@115.146.121.248/public_html/3qchibi/deploy/$2.tar.gz
tar -zxvf $2.tar.gz

cp config/main-server/*.cfg /data/config-$1/main-server/ -rf
cp config/main-server/*.xml /data/config-$1/main-server/ -rf
cp config/main-server/logic /data/config-$1/main-server/ -rf

cp config/logic-server/*.xml /data/config-$1/logic-server/ -rf
cp config/logic-server/message*.properties /data/config-$1/logic-server/ -rf
cp config/logic-server/logic /data/config-$1/logic-server/ -rf

cp config/game-api/config /data/config-$1/game-api/ -rf

cp config/logic-server/*.xml /data/config-$1/command/ -rf
cp config/logic-server/server /data/config-$1/command/ -rf
cp config/logic-server/logic /data/config-$1/command/ -rf


echo success
