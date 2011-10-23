#!/bin/sh

# Setup Classpath

# make sure we have JAVA_HOME set 
if [ -z "$JAVA_HOME" ]
then
    echo The JAVA_HOME environment variable is not defined
    exit 1
fi

# Set LAMBDA_HOME
#LAMBDA_HOME=`dirname \`pwd\`../`
LAMBDA_HOME="$( cd "$( dirname "$0" )" && pwd )"
LAMBDA_HOME=`dirname $LAMBDA_HOME../`

#  Create the classpath for bootstrapping the Server from all the JARs in lib 
for file in $LAMBDA_HOME/lib/*
do
        if [[ $file == *.jar ]]
        then
        CLASSPATH=$CLASSPATH:$LAMBDA_HOME/lib/${file##*/}
        fi  
done
for file in $LAMBDA_HOME/clilib/*
do
        if [[ $file == *.jar ]]
        then
        CLASSPATH=$CLASSPATH:$LAMBDA_HOME/clilib/${file##*/}
        fi  
done


# Operating System detection - currently support 32/64bit Mac, 32/64bit Linux, 32bit Windows

OS=`uname`

# Detect OS and set Chrome browser driver path
if [ "$OS" == 'Linux' -o "$OS" == "FreeBSD" ]; then
	# 32 or 64bit?
	arch = `uname -m`
	if [[ "$arch" == "x86_64" ]]; then
		CHROME_DRIVER=-Dwebdriver.chrome.driver=$LAMBDA_HOME/lib/chromedriver-linux64
	else
		CHROME_DRIVER=-Dwebdriver.chrome.driver=$LAMBDA_HOME/lib/chromedriver-linux32
	fi
elif [[ "$OS" == "Darwin" ]]; then
	CHROME_DRIVER=-Dwebdriver.chrome.driver=$LAMBDA_HOME/lib/chromedriver-macosx
fi

# make sure we have CLASSPATH set 
if [ -z "$CLASSPATH" ]
then
    echo No JAR files found in $LAMBDA_HOME/lib
    exit 1
fi

# ClamShell keys
CLAMSHELL_PROPS="-Dkey.dir.conf=$LAMBDA_HOME/conf -Dkey.dir.plugins=$LAMBDA_HOME/plugins -Dkey.dir.lib=$LAMBDA_HOME/lib"

# Lambda bootstrap
java \
 -classpath $CLASSPATH \
 $CHROME_DRIVER \
 $CLAMSHELL_PROPS \
 cli.Run