@echo off

set LAMBDA_HOME=C:\Documents and Settings\mfellows\Desktop\lambda
set JAVA_HOME=C:\Program files\Java\jre6

echo %LAMBDA_HOME%

echo %JAVA_HOME%

if "%LAMBDA_HOME%" == "" (
  echo The LAMBDA_HOME environment variable is not defined.
  rem exit /B 1
)

rem Check JAVA_HOME and LAMBDA_HOME variables
if "%JAVA_HOME%" == "" (
  echo The JAVA_HOME environment variable is not defined.
  rem exit /B 1
)

echo checking classpath...

rem ### Setup CLASSPATH
call "%LAMBDA_HOME%\bin\setupClasspath.bat"

rem Check if there are JAR files in the lib directory.
if "%CLASSPATH%" == "" (
  echo No JAR files found in %LAMBDA_HOME%\lib
  rem exit /B 1
)


echo Starting Lambda...

"%JAVA_HOME%\bin\java" -classpath "%CLASSPATH%" cli.Run

pause
