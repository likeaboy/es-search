#!/bin/bash

jps | grep "ConsumerOffsetChecker" | awk '{print $1}' > killer-records.txt
echo "killer start,please wait..."
while read record
do
kill -9 $record
done < killer-records.txt
echo "kill all offset-checker done."
