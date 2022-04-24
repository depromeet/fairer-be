# deploy.sh
#!/usr/bin/env bash

REPOSITORY=/home/ec2-user
cd $REPOSITORY

JAR_NAME=fairer-dev.jar
JAR_PATH=$REPOSITORY/$JAR_NAME

CURRENT_PID=$(pgrep -f $JAR_NAME)

if ! [ -z $CURRENT_PID ]
then
  kill $CURRENT_PID
  sleep 5
fi

nohup java -jar -Dspring.profiles.active=dev $JAR_PATH &