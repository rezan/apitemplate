#!/bin/bash

export JAVA_HOME=/opt/jdk1.8.0_65
#export CATALINA_OPTS="-Xmx1000m"
export CATALINA_OPTS="-Xmx1000m -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9988 -Dcom.sun.management.jmxremote.rmi.port=9988 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.rmi.server.hostname=127.0.0.1"
