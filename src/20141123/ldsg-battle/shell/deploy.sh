#!/bin/sh

SRC_DIR=/data/src/trunk
DEPLOY_DIR=/data/deploy_dir
CONFIG_DIR=/data/deploy_dir/config

cd $SRC_DIR/

svn up


rm $DEPLOY_DIR -rf
mkdir $DEPLOY_DIR

rm $CONFIG_DIR -rf
mkdir $CONFIG_DIR

mkdir $CONFIG_DIR/ldsg-battle
cp $SRC_DIR/ldsg-battle/src/main/resources/*.xml $CONFIG_DIR/ldsg-battle/ -rf
cp $SRC_DIR/ldsg-battle/src/main/resources/*.properties $CONFIG_DIR/ldsg-battle/ -rf
rm $SRC_DIR/ldsg-battle/src/main/resources/*.xml -rf
rm $SRC_DIR/ldsg-battle/src/main/resources/*.properties -rf

cd $SRC_DIR/ldsg-battle/
mvn clean package shade:shade -U -Dmaven.test.skip=true
cp $SRC_DIR/ldsg-battle/target/ldsg-battle.jar $DEPLOY_DIR/

cd $DEPLOY_DIR

tar -zcvf ldsg-battle\.tar\.gz ./

wput $DEPLOY_DIR/ldsg-battle.tar.gz ftp://ftpuser:ledou0328@113.107.72.243/deploy/ldsg-battle.tar.gz
