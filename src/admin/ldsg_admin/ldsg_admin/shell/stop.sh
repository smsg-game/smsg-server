#!/bin/sh
sid=$1

ps -ef | grep /main-server.jar | grep config-$sid |  grep -v grep | cut -c 9-15 | xargs kill -9
ps -ef | grep /logic-server.jar | grep config-$sid | grep -v grep | cut -c 9-15 | xargs kill -9


echo success

