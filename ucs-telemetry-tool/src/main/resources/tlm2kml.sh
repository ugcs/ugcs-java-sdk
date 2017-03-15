#!/bin/sh

CLASSPATH=$(pwd):$(pwd)/*:$(pwd)/lib/*
JAVAPATH=/Applications/UgCS/java/bin/java
if [ ! -e $JAVAPATH ]; then
    JAVAPATH=$JAVA_HOME
fi
${JAVAPATH} -cp ${CLASSPATH} com.ugcs.telemetrytool.Tlm2Kml $*