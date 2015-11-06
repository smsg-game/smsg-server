sid=h13
java -Dfile.encoding=utf-8 -Duser.language=en -Duser.country=US -cp .:/data/config-$sid/command:/data/deploy/logic-server.jar com.lodogame.ldsg.command.RankStatCommand file:/data/config-$sid/command/applicationContext.xml > /log/log-$sid/stat/rank.log&
