echo off
echo set env

set JBX_VERSION=1.0.1
set JBX_REPOSITORY=surpass

set JAVA_HOME=C:\IDE\jdk-17.0.9+9
set GRADLE_HOME=C:\ide\gradle-8.8

call %JAVA_HOME%/bin/java -version
call %GRADLE_HOME%/bin/gradle -version
