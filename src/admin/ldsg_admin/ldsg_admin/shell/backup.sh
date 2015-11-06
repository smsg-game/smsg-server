#!/bin/sh
output="/data/backup/$(date +%Y%m%d%H).sql.gz"
mysqldump -uroot -pallen_liu ldsg_game_admin | gzip > $output
