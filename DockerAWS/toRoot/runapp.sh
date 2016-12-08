#!/bin/bash

APP=app.jar
JAVA_PARAMS="-Dlogback.configurationFile=./config_override/logback.xml -Xms64m -Xmx512m"

date +" --- RUNNING $(basename $0) %Y-%m-%d_%H:%M:%S --- "
set -x

if [ "$AWS_CLOUDWATCH_LOGGING_ENABLED" = "true" ]; then
    # Enables aws cloudwatch plugin in aws cli
    aws configure set plugins.cwlogs cwlogs
    # Sets container id used by as part of log-stream-name
    CONTAINER_ID="$(cat /proc/self/cgroup | grep docker | grep -o -E '[0-9a-f]{64}' | head -n 1 | cut -c1-12)"
    #replaces log-group and log-stream name in aws-cloudwatch.conf
    sed -i -e "s/{CONTAINER_ID}/$CONTAINER_ID/g" aws-cloudwatch.conf
    sed -i -e "s/{AWS_LOG_GROUP}/$AWS_LOG_GROUP/g" aws-cloudwatch.conf
    if [ -z "$LOGBACK_CANTARA_LEVEL" ]; then
        sed -i -e "s/{LOGBACK_CANTARA_LEVEL}/info/g" config_override/logback.xml
    else
        sed -i -e "s/{LOGBACK_CANTARA_LEVEL}/$LOGBACK_CANTARA_LEVEL/g" config_override/logback.xml
    fi


    java_pid=()
    awslogs_pid=()
    gotsigchld=false
    #Using trap on CHLD and EXIT to run clean exit of java-process.
    trap '
    if ! "$gotsigchld"; then
        gotsigchld=true
        kill "${java_pid[@]}"
        #When java process dies, give awslogs time to send last batch.
        wait "${java_pid[@]}"
        sleep 10
        kill "${awslogs_pid[@]}"
    fi
    ' CHLD EXIT

    /usr/bin/java $JAVA_PARAMS $JAVA_PARAMS_OVERRIDE -jar $APP & java_pid+=$!
    aws logs push --region=eu-west-1 --config-file aws-cloudwatch.conf & awslogs_pid+=$!
    set -m
    wait
    set +m
else
    #If not running with cloudwatch enabled, just execute jar.
    java $JAVA_PARAMS $JAVA_PARAMS_OVERRIDE -jar $APP
fi
