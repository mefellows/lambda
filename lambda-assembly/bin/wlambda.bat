@echo off

rem Get LAMBDA directory
set current=%cd%
pushd ..
set LAMBDA_HOME=%cd%
popd

rem echo %LAMBDA_HOME%
rem set JAVA_HOME=C:\Program files\Java\jre6
rem echo %JAVA_HOME%

if "%LAMBDA_HOME%" == "" (
  echo The LAMBDA_HOME environment variable is not defined.
  rem exit /B 1
)

echo checking classpath...

rem ### Setup CLASSPATH
call "%LAMBDA_HOME%\bin\setupClasspath.bat"

set CHROME_DRIVER=-Dwebdriver.chrome.driver=%LAMBDA_HOME%\lib\chromedriver.exe

rem Check if there are JAR files in the lib directory.
if "%CLASSPATH%" == "" (
  echo No JAR files found in %LAMBDA_HOME%\lib
  rem exit /B 1
)

rem "%JAVA_HOME%\bin\java" "%CHROME_DRIVER%" -classpath "%CLASSPATH%" "%CLAMSHELL_PROPS%" cli.Run
java "%CHROME_DRIVER%" "-Dkey.dir.conf=%LAMBDA_HOME%\conf" "-Dkey.dir.plugins=%LAMBDA_HOME%\plugins" "-Dkey.dir.lib=%LAMBDA_HOME%\lib" -classpath "%CLASSPATH%" cli.Run

pause