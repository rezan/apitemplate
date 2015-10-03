#!/bin/bash

HOST=localhost
PORT=3306
USER=apiuser
PW=apipw

for DB in apidb apishard0 apishard1
do
    echo "Restoring $DB"
    cat backup/$DB.backup.sql.gz | gunzip | mysql -h $HOST -P $PORT -u $USER -p$PW $DB
done
