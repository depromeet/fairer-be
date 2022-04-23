# deploy.sh
#!/usr/bin/env bash

REPOSITORY=/home/ec2-user
cd $REPOSITORY

APP_NAME=fairer-dev
JAR_NAME=$(ls $REPOSITORY/ | grep '.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/$JAR_NAME

CURRENT_PID=$(pgrep -f $APP_NAME)

if [ -z $CURRENT_PID ]
then
  echo "> Nothing to end."
else
  echo "> kill -9 $CURRENT_PID"
  kill $CURRENT_PID
  sleep 5
fi

echo "> $JAR_PATH deploy"
nohup java -jar -Dspring.profiles.active=dev $JAR_PATH > /dev/null 2> /dev/null < /dev/null &