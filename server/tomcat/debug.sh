#!/bin/bash

echo "ssh -L 9988:localhost:9988 user@dev.company Code/apitemplate/server/tomcat/debug.sh 30"
echo "jvisualvm, JMX to localhost:9988"

if [ "$1" != "" ]
then
    sleep $1
fi
