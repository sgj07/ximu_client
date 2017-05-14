#!/bin/bash

if [ "$JAVA_HOME" = "" ]; then
  echo "Error: JAVA_HOME is not set."
  exit 1
fi

env_list="dev staging prod"
if [[ " $env_list " =~ " $CLIENT_ENV " ]]
then
    echo "client env: $CLIENT_ENV"
fi

bin=`dirname "$0"`

export MYJETTY_HOME=`cd $bin/../; pwd`

MYJETTY_CONF_DIR=$MYJETTY_HOME/conf
CLASSPATH="${MYJETTY_CONF_DIR}"
for f in ${MYJETTY_CONF_DIR}/*.xml; do
  CLASSPATH=${CLASSPATH}:$f;
done

for f in ${MYJETTY_CONF_DIR}/*.properties; do
  CLASSPATH=${CLASSPATH}:$f;
done

for f in $MYJETTY_HOME/lib/*.jar; do
  CLASSPATH=${CLASSPATH}:$f;
done
# echo $CLASSPATH

LOG_DIR=${MYJETTY_HOME}/logs
if [ ! -d "$LOG_DIR" ]; then
  mkdir "$LOG_DIR"
fi
export LOG_DIR

MAIN_CLASS=com.ximucredit.client.server.MainServer
nohup ${JAVA_HOME}/bin/java -classpath "$CLASSPATH"  $MAIN_CLASS >${LOG_DIR}/server.out 2>&1 &
# nohup ${JAVA_HOME}/bin/java -classpath "$CLASSPATH" $CLASS > ${LOG_DIR}/myjetty.out 2>&1 < /dev/null &