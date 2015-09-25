#!/bin/sh
sid=$1
usage="Usage: $0 [sid] [version] [game-web/game]"
pkg_url=http://deploy.xworldgame.com/$2.tar.gz

update_dir=/data/server/smsg/game_zone/SG_$sid/Update
runtime_dir=/data/server/smsg/game_zone/SG_$sid/RunTime

main_config=/data/server/smsg/game_zone/SG_$sid/RunTime/config/main-server
main_jar=/data/server/smsg/game_zone/SG_$sid/RunTime/main-server.jar
logic_config=/data/server/smsg/game_zone/SG_$sid/RunTime/config/logic-server
logic_jar=/data/server/smsg/game_zone/SG_$sid/RunTime/logic-server.jar
logic_pkg=com.lodogame.ldsg.server.LogicServer

if [[ $# -ne "3" ]];then
        echo $usage
        exit 1
fi
mkdir -p $update_dir/$2
cd $update_dir/$2 && rm -rf *
wget $pkg_url
tar -xzvf $2.tar.gz
if [[ $3 == "game" ]];then
	cp -rf ldsg-battle.jar $runtime_dir/
	cp -rf main-server.jar $runtime_dir/
	cp -rf logic-server.jar $runtime_dir/
else
	echo "else"
fi
