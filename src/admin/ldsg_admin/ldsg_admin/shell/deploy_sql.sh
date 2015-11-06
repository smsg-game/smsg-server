#!/bin/sh

input_dir="/data/deploy_sql/$(date +%Y%m%d)"

if [ ! -d "$input_dir" ];then
   mkdir -p "$input_dir"
fi

cd "$input_dir"

rm -f "$input_dir"/*

wget ftp://ftpchibi:xjK4kPKUusCO@115.146.121.248/public_html/3qchibi/deploy/sql_$2.zip

unzip sql_$2.zip 

if [ -f update.sql ];then
   cat update.sql | mysql -uroot ldsg_$1 -pallen_liu
fi

if [ -f skill.sql ];then
   cat skill.sql | mysql -uroot ldsg_battle -pallen_liu
fi

echo success
