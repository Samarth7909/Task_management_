@echo off
set "JAVA_HOME=C:\Program Files\Java\jdk-21.0.10"
set "PATH=%JAVA_HOME%\bin;C:\maven\apache-maven-3.9.15\bin;%PATH%"
cd /d "e:\E_Download\task-manager-scaffold\backend"
mvn.cmd test
