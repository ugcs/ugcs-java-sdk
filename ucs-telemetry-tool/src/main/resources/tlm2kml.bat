@echo off
set CLASSPATH=.;*;lib\*
shift
"%JAVA_HOME%\bin\java" -cp %CLASSPATH% com.ugcs.telemetrytool.Tlm2Kml %*