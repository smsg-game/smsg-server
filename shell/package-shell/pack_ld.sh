#!/bin/sh

SRC_DIR=/data/src/$1
DEPLOY_DIR=/data/deploy_dir
CONFIG_DIR=/data/deploy_dir/config
BATTLE_DIR=/data/deploy_dir_battle
BATTLE_CONFIG_DIR=/data/deploy_battle/config

cd $SRC_DIR/

svn up

mvn clean deploy -U -Dmaven.test.skip=true -pl game-server,game-utils,partner-sdk,game-model,game-dao,game-service

rm $DEPLOY_DIR -rf
mkdir $DEPLOY_DIR
mkdir $CONFIG_DIR

rm $CONFIG_DIR -rf

mkdir $CONFIG_DIR

mkdir $CONFIG_DIR/main-server
mkdir $CONFIG_DIR/main-server/server
mkdir $CONFIG_DIR/main-server/logic
cp $SRC_DIR/main-server/src/main/resources/*.xml $CONFIG_DIR/main-server/ -rf
cp $SRC_DIR/main-server/src/main/resources/*.properties $CONFIG_DIR/main-server/ -rf
cp $SRC_DIR/main-server/src/main/resources/server/*.xml $CONFIG_DIR/main-server/server/ -rf
cp $SRC_DIR/main-server/src/main/resources/logic/*.xml  $CONFIG_DIR/main-server/logic/ -rf
cp $SRC_DIR/main-server/src/main/resources/*.cfg $CONFIG_DIR/main-server/ -rf
rm $SRC_DIR/main-server/src/main/resources/*.xml -rf
rm $SRC_DIR/main-server/src/main/resources/server -rf
rm $SRC_DIR/main-server/src/main/resources/logic -rf
rm $SRC_DIR/main-server/src/main/resources/*.properties -rf
rm $SRC_DIR/main-server/src/main/resources/*.cfg -rf

cd $SRC_DIR/main-server/
mvn clean package shade:shade -U -Dmaven.test.skip=true
cp $SRC_DIR/main-server/target/main-server.jar $DEPLOY_DIR/

#/data/shell/main-server/run.sh

mkdir $CONFIG_DIR/logic-server
mkdir $CONFIG_DIR/logic-server/server
mkdir $CONFIG_DIR/logic-server/logic
cp $SRC_DIR/logic-server/src/main/resources/*.xml $CONFIG_DIR/logic-server/ -rf
cp $SRC_DIR/logic-server/src/main/resources/*.properties $CONFIG_DIR/logic-server/ -rf
cp $SRC_DIR/logic-server/src/main/resources/server/*.xml $CONFIG_DIR/logic-server/server/ -rf
cp $SRC_DIR/logic-server/src/main/resources/logic/*.xml  $CONFIG_DIR/logic-server/logic/ -rf
rm $SRC_DIR/logic-server/src/main/resources/*.xml -rf
rm $SRC_DIR/logic-server/src/main/resources/server -rf
rm $SRC_DIR/logic-server/src/main/resources/logic -rf
rm $SRC_DIR/logic-server/src/main/resources/*.properties -rf

cd $SRC_DIR/logic-server/
mvn clean package shade:shade -U -Dmaven.test.skip=true
cp $SRC_DIR/logic-server/target/logic-server.jar $DEPLOY_DIR/

#/data/shell/logic-server/run.sh  BATTLE_DIR
mkdir $BATTLE_DIR/ldsg-battle
cp $SRC_DIR/ldsg-battle/src/main/resources/*.xml $BATTLE_DIR/ldsg-battle/ -rf
cp $SRC_DIR/ldsg-battle/src/main/resources/*.properties $BATTLE_DIR/ldsg-battle/ -rf
cp $SRC_DIR/ldsg-battle/src/main/resources/server/*.xml $BATTLE_DIR/ldsg-battle/ -rf
cp $SRC_DIR/ldsg-battle/src/main/resources/logic/*.xml  $BATTLE_DIR/ldsg-battle/ -rf
rm $SRC_DIR/ldsg-battle/src/main/resources/*.xml -rf
rm $SRC_DIR/ldsg-battle/src/main/resources/server -rf
rm $SRC_DIR/ldsg-battle/src/main/resources/logic -rf
rm $SRC_DIR/ldsg-battle/src/main/resources/*.properties -rf

cd $SRC_DIR/ldsg-battle/
mvn clean package shade:shade -U -Dmaven.test.skip=true
cp $SRC_DIR/ldsg-battle/target/logic-battle.jar $BATTLE_DIR/


#/data/shell/logic-server/run.sh

cd $SRC_DIR/game-web/

mkdir $CONFIG_DIR/game-web/
mkdir $CONFIG_DIR/game-web/config
mkdir $CONFIG_DIR/game-web/config/web

cd $SRC_DIR/game-web/
cp $SRC_DIR/game-web/src/main/resources/*.xml $CONFIG_DIR/game-web/ -rf
cp $SRC_DIR/game-web/src/main/resources/*.properties $CONFIG_DIR/game-web/ -rf
cp $SRC_DIR/game-web/src/main/resources/config/*.xml $CONFIG_DIR/game-web/config/ -rf
cp $SRC_DIR/game-web/src/main/resources/config/web/*.xml  $CONFIG_DIR/game-web/config/web/ -rf
rm $SRC_DIR/game-web/src/main/resources/*.xml
rm $SRC_DIR/game-web/src/main/resources/*.properties
rm $SRC_DIR/game-web/src/main/resources/config/*.xml
rm $SRC_DIR/game-web/src/main/resources/config/web/*.xml

mvn clean package shade:shade -U -Dmaven.test.skip=true

cd $SRC_DIR/game-api/

mkdir $CONFIG_DIR/game-api/
mkdir $CONFIG_DIR/game-api/config
mkdir $CONFIG_DIR/game-api/config/web

cp $SRC_DIR/game-api/src/main/resources/*.xml $CONFIG_DIR/game-api/ -rf
cp $SRC_DIR/game-api/src/main/resources/*.properties $CONFIG_DIR/game-api/ -rf
cp $SRC_DIR/game-api/src/main/resources/config/*.xml $CONFIG_DIR/game-api/config/ -rf
cp $SRC_DIR/game-api/src/main/resources/config/web/*.xml  $CONFIG_DIR/game-api/config/web/ -rf
rm $SRC_DIR/game-api/src/main/resources/*.xml
rm $SRC_DIR/game-api/src/main/resources/*.properties
rm $SRC_DIR/game-api/src/main/resources/config/*.xml
rm $SRC_DIR/game-api/src/main/resources/config/web/*.xml

mvn clean package shade:shade -U -Dmaven.test.skip=true

cp $SRC_DIR/game-web/target/webapp.war $DEPLOY_DIR/game-web.war
cp $SRC_DIR/game-api/target/webapp.war $DEPLOY_DIR/game-api.war

cd $DEPLOY_DIR

tar -zcvf $1\.tar\.gz ./



/usr/bin/ftp -n 183.232.129.35 <<EOF
user ldsg_upload easou8888
binary
prompt
cd deploy
delete $1.tar.gz
EOF

# 替换 ftp 的登录名和密码，以及 ip
wput $DEPLOY_DIR/$1.tar.gz ftp://ldsg_upload:easou8888@183.232.129.35/deploy/$1.tar.gz
