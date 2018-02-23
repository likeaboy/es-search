#!/bin/bash
ROOT=$1
FILE=$2
NEWIP=$3

mod(){
node_file=$ROOT$1$FILE
echo $node_file
sed -i 's/network.host: .*/network.host: '"$NEWIP"',127.0.0.1/g' $node_file
}
run(){
echo "start modify es ip, newip is : $NEWIP"
echo "such files network.host will be modified to $NEWIP"
for filename in ES*;
    do
      array[$x]=$filename
      let "x+=1"
      mod $filename
 done
echo "end modify..."
}
run
