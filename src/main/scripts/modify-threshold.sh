#!/bin/bash

FILE=$1
ALARM_THRESHOLD=$2
CLEAR_THRESHOLD=$3

run(){
echo "start modify es_monitor threshold"
sed -i 's/alarm_threshold=.*/alarm_threshold='"$ALARM_THRESHOLD"'/g' $FILE
sed -i 's/clear_threshold=.*/clear_threshold='"$CLEAR_THRESHOLD"'/g' $FILE
echo "end modify es_monitor threshold"
}
run
