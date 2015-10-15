#!/bin/sh
T_SRC_DIR=/cygdrive/e/github/smsg-game/smsg-server/src/20141123/
SRC_DIR=/cygdrive/e/github/smsg-game/smsg-server/src/build/
DEPLOY_DIR=/data/deploy_dir
CONFIG_DIR=/data/deploy_dir/config
BATTLE_DIR=/data/deploy_dir_battle
BATTLE_CONFIG_DIR=/data/deploy_battle/config

rm -rf $SRC_DIR/*
rm -rf $BATTLE_DIR/*
cp -rf $T_SRC_DIR/* $SRC_DIR


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
cp $SRC_DIR/ldsg-battle/target/ldsg-battle.jar $BATTLE_DIR/

cd $BATTLE_DIR
tar -zcvf battle\.tar\.gz ./