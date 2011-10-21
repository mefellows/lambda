#!/bin/sh

# Setup Classpath

# make sure we have JAVA_HOME set 
if [ -z "$JAVA_HOME" ]
then
    echo The JAVA_HOME environment variable is not defined
    exit 1
fi

CLASSPATH=

LAMBDA_HOME=`pwd`

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

# make sure we have CLASSPATH set 
if [ -z "$CLASSPATH" ]
then
    echo No JAR files found in $LAMBDA_HOME/lib
    exit 1
fi

#echo $CLASSPATH

# Lambda bootstrap
java -classpath $CLASSPATH:./clilib/clamshell-launcher-0.1.jar cli.Run
