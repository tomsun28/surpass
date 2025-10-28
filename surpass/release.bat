call setEnvVars.bat

set START_TIME="%date:~0,10% %time:~0,2%:%time:~3,5%"
echo start time %START_TIME%

call %GRADLE_HOME%/bin/gradle -q projects

echo start clean . . .

call %GRADLE_HOME%/bin/gradle clean

echo clean complete . 

rem run task build
call %GRADLE_HOME%/bin/gradle build -x test

set END_TIME="%date:~0,10% %time:~0,2%:%time:~3,5%"

echo Build Release start at %START_TIME%  complete at %END_TIME%.

pause