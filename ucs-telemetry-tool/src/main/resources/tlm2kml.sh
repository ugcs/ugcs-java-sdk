#!/bin/sh

CLASSPATH=.:*:lib/*
shift
java -cp ${CLASSPATH} com.ugcs.telemetrytool.Tlm2Kml $