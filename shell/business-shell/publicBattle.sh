#!/bin/sh
usage="Usage: $0 {start|stop|restart|status}"
checkPid(){
        process=$(ps -ef | grep ldsg-battle | grep -v grep | head -n1 | awk '{print $2}')
	if [ -z $process ];then
		echo "None process running!"
	else
		echo "Pid[$process] is running..."
	fi
}
battle_config=/data/server/smsg/game_battle/config/ldsg-battle
battle_jar=/data/server/smsg/game_battle/ldsg-battle.jar
battle_pkg=com.ldsg.battle.App

if [ -z $1 ];then
	echo $usage
	exit 1
else
	case "$1" in
		"start" )
			checkPid
			if [ -z $process ];then
				echo "Now start battle server..."
				sudo -u root java -Dfile.encoding=utf-8 -Duser.language=en -Duser.country=US -cp .:$battle_config:$battle_jar $battle_pkg > /dev/null&
				sleep 2
				checkPid
			fi
			;;
		"stop" )
			checkPid
			if [ ! -z $process ];then
				echo "Now stop battle server..."
				ps -ef | grep ldsg-battle | grep -v grep | cut -c 9-15 | xargs kill -9
				checkPid
			fi
			;;
		"restart" )
			$0 stop
			$0 start
			;;
		"status" )
			checkPid
			;;
		* )
			echo $usage
	esac
fi
