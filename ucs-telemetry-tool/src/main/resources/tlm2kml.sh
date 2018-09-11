#!/bin/sh

DIR=`dirname "$0"`
CP=$DIR:$DIR/*:$DIR/lib/*
JAVA=/Applications/UgCS/java/bin/java
if [ ! -e $JAVA ]; then
    JAVA=$JAVA_HOME/bin/java
fi
"$JAVA" -cp "$CP" com.ugcs.telemetrytool.Tlm2Kml $*
