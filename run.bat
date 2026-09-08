@echo off
mvn clean package
if errorlevel 1 pause & exit /b 1
java -jar target\shivray-international-air-cargo-1.0.0.jar
pause
