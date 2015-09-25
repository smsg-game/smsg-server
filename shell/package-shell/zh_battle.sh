#!/bin/sh

SRC_DIR=/data/build/src/$1
DEPLOY_DIR=/data/build/output
CONFIG_DIR=/data/build/output/config
BATTLE_DIR=/data/build/output/battle
BATTLE_CONFIG_DIR=/data/build/output/battle/config

cd $SRC_DIR/

svn up

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



# 替换 ftp 的登录名和密码，以及 ip
#wput $DEPLOY_DIR/$1.tar.gz ftp://test:123456@54.179.135.34/deploy/$1.tar.gz
