#!/bin/sh
cd /data/deploy_battle
rm /data/deploy_battle/* -rf

wget ftp://ftpchibi:xjK4kPKUusCO@115.146.121.248/public_html/3qchibi/deploy/ldsg-battle_$2.tar.gz

tar -zxvf ldsg-battle_$2.tar.gz

cp config/ldsg-battle/*.xml /data/config-$1/ldsg-battle/ -rf


echo success
