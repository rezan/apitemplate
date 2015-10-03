#!/bin/bash

ENV=$1

if [ "$ENV" != "dev" -a "$ENV" != "prod" ]
then
    echo "Please pass in dev or prod"
    exit 1
fi

mvn tomcat7:deploy -P $ENV
