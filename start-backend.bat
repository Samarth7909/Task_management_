@echo off
echo =========================================
echo  Task Manager Backend - Starting...
echo =========================================

echo Checking for process on port 8080...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8080 ^| findstr LISTENING') do (
    echo Killing PID %%a on port 8080...
    taskkill /PID %%a /F >nul 2>&1
)

set "JAVA_HOME=C:\Program Files\Java\jdk-21.0.10"
set "PATH=%JAVA_HOME%\bin;C:\maven\apache-maven-3.9.15\bin;%PATH%"
cd /d "e:\E_Download\task-manager-scaffold\backend"

echo Starting Spring Boot on http://localhost:8080 ...
mvn.cmd spring-boot:run
pause
