#!/bin/sh
sid=$1
usage="Usage: $0 sid {start|stop|restart|status}"

main_config=/data/server/smsg/game_zone/SG_$sid/RunTime/config/main-server
main_jar=/data/server/smsg/game_zone/SG_$sid/RunTime/main-server.jar
logic_config=/data/server/smsg/game_zone/SG_$sid/RunTime/config/logic-server
logic_jar=/data/server/smsg/game_zone/SG_$sid/RunTime/logic-server.jar
logic_pkg=com.lodogame.ldsg.server.LogicServer

redis=/app/redis/bin/redis-cli

checkLogicPid(){
	echo
        Lprocess=$(ps -ef | grep $logic_jar | grep -v grep | head -n1 | awk '{print $2}')
        if [ -z $Lprocess ];then
                echo "None Logic server process!"
        else
                echo "Logic server Pid[$Lprocess] is running..."
        fi
	echo
}
checkMainPid(){
	echo
        Mprocess=$(ps -ef | grep $main_jar | grep -v grep | head -n1 | awk '{print $2}')
        if [ -z $Mprocess ];then
                echo "None Main server process!"
        else
                echo "Main server Pid[$Mprocess] is running..."
        fi
	echo
}
checkBattlePid(){
	echo
        Bprocess=$(ps -ef | grep ldsg-battle.jar | grep -v grep | head -n1 | awk '{print $2}')
        if [ -z $Bprocess ];then
                echo "None Battle server process!"
        else
                echo "Battle server Pid[$Bprocess] is running..."
        fi
	echo
}


if [[ $# -ne "2" ]];then
	echo $usage
	exit 1
fi
echo "#########################################"
port=$(cat /data/server/smsg/game_zone/SG_$sid/RunTime/config/logic-server/jedis.properties | grep port | awk -F"=" '{print $2}')

case "$2" in
	"start" )
		$redis -p $port flushall > /dev/null 2>&1
		checkMainPid
		if [ -z $Mprocess ];then
			echo "Now start Main server..."
			sudo -u root java -Dfile.encoding=utf-8 -Duser.language=en -Duser.country=US -cp .:$main_config:$main_jar Start file:$main_config/applicationContext.xml $sid true> /dev/null&
			checkMainPid
		fi
		sleep 2
		checkLogicPid
		if [ -z $Lprocess ];then
			echo "Now start Logic server..."
			sudo -u root java -Dfile.encoding=utf-8 -Duser.language=en -Duser.country=US -cp .:$logic_config:$logic_jar:$logic_config/ $logic_pkg file:$logic_config/applicationContext.xml $sid > /dev/null& 
			checkLogicPid
		fi
		;;
	"stop" )
		checkMainPid
		if [ ! -z $Mprocess ];then
			echo "Now stop Main server..."
			ps -ef | grep $main_jar | grep -v grep | cut -c 9-15 | xargs kill -9 
			checkMainPid
		fi
		checkLogicPid
		if [ ! -z $Lprocess ];then
			echo "Now stop logic server..."
			ps -ef | grep $logic_jar | grep -v grep | cut -c 9-15 | xargs kill -9
			checkLogicPid
		fi
		;;
	"restart" )
		$0 $sid stop
		$0 $sid start
		;;
	"status" )
		checkLogicPid
		checkMainPid
		checkBattlePid
		;;
	* )
		echo $usage
esac
echo "#########################################"
