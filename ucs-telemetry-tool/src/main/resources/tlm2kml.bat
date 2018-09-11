@echo off
set CP=%~dp0;%~dp0\*;%~dp0\lib\*
set JAVA="C:\Program Files (x86)\UgCS\java\bin\java.exe"
if not exist %JAVA% (
   set JAVA="%JAVA_HOME%\bin\java.exe"
)
shift
%JAVA% -cp "%CP%" com.ugcs.telemetrytool.Tlm2Kml %*
