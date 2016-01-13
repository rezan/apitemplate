#!/bin/bash

echo ssh -L 3366:localhost:3306 user@dev.company Code/apitemplate/scripts.sh 30

if [ "$1" != "" ]
then
    sleep $1
fi
