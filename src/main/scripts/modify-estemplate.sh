#!/bin/bash

FILE=$1
VALUE=$2
if [ $# -gt 0 ]; then
        echo "参数个数为$#个"
else
        echo "没有参数"
        exit 1;
fi
MATCH=$(sed -n 's/"enabled": false/"enabled": '"$VALUE"'/p' $FILE)
if  [ ! -n "$MATCH" ] ;then
    echo "match "enabled": true"
    sed -i 's/"enabled": true/"enabled": '"$VALUE"'/' $FILE
else
    echo "match "enabled": false"
    sed -i 's/"enabled": false/"enabled": '"$VALUE"'/' $FILE
fi

#sed -i 's/"enabled": false/"enabled": true/' $1

