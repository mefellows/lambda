@echo off 
rem Check JAVA_HOME and LAMBDA_HOME variables
if "%JAVA_HOME%" == "" (
  echo The JAVA_HOME environment variable is not defined.
  exit /B 1
)
if "%LAMBDA_HOME%" == "" (
  echo The LAMBDA_HOME environment variable is not defined.
  exit /B 1
)

rem Construct the CLASSPATH list from the Kernel lib directory.
for %%G in ("%LAMBDA_HOME%\lib\*.jar") do call :AppendToClasspath "%%G"
for %%G in ("%LAMBDA_HOME%\clilib\*.jar") do call :AppendToClasspath "%%G"


rem Check if there are JAR files in the lib directory.
if "%CLASSPATH%" == "" (
  echo No JAR files found in %LAMBDA_HOME%\lib or %LAMBDA_HOME%\clilib
  exit /B 1
)

rem Remove leading semi-colon if present
if "%CLASSPATH:~0,1%"==";" set CLASSPATH=%CLASSPATH:~1%
exit /B 0

:AppendToClasspath
  set CLASSPATH=%CLASSPATH%;%~1
  goto :eof
  
  
