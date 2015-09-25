#!/bin/sh

cd /data/src/$1/

svn up

mvn clean deploy -U -Dmaven.test.skip=true -pl game-server,game-utils,game-model,game-dao,game-service

cd /data/src/$1/main-server/

mvn clean package shade:shade -U -Dmaven.test.skip=true

cp /data/src/$1/main-server/target/classes/*.xml /data/config/main-server/ -rf
cp /data/src/$1/main-server/target/classes/server/* /data/config/main-server/server/ -rf
cp /data/src/$1/main-server/target/classes/logic/*  /data/config/main-server/logic/ -rf

/data/shell/main-server/run.sh

cd /data/src/$1/logic-server/

mvn clean package shade:shade -U -Dmaven.test.skip=true

cp /data/src/$1/logic-server/target/classes/*.xml /data/config/logic-server/ -rf
cp /data/src/$1/logic-server/target/classes/server/* /data/config/logic-server/server/ -rf
cp /data/src/$1/logic-server/target/classes/logic/*  /data/config/logic-server/logic/ -rf

/data/shell/logic-server/run.sh