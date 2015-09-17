@echo off��

call cd /d %~dp0

call java -version

call mvn clean

call mvn install

pause