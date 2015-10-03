#!/bin/bash

HOST=127.0.0.1
PORT=3306
USER=apiuser
PW=apipw

rm -rf backup
mkdir backup

for DB in apidb apishard0 apishard1
do
    echo "Dumping $DB"
    mysqldump -h $HOST -P $PORT -u $USER -p$PW $DB | gzip > backup/$DB.backup.sql.gz
done
