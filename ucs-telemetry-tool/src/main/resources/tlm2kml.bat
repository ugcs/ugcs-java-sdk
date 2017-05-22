@echo off
set CLASSPATH=%~dp0;%~dp0\*;%~dp0\lib\*
set JAVAPATH="C:\Program Files (x86)\UgCS\java\bin\java.exe"
if not exist %JAVAPATH% (
   set JAVAPATH="%JAVA_HOME%\bin\java.exe"
)
shift
%JAVAPATH% -cp %CLASSPATH% com.ugcs.telemetrytool.Tlm2Kml %*