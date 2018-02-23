#!/bin/sh
taskset -c 0 nohup java -Xms2048m -Xmx2048m -XX:+PrintGCDateStamps -XX:+PrintGCDetails -Xloggc:gc/gc.log -jar es-search.jar > /dev/null 2>&1 &