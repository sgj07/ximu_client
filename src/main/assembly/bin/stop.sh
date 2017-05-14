#!/bin/bash

PID=`ps aux | grep com.ximucredit.client.server.MainServer | grep java | awk '{print $2}'`

if [ -n "$PID" ]; then
        echo "Try to kill $PID"
        kill -9 $PID
else echo "No Process Found "
fi