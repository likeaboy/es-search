#!/bin/bash

RST=$(cat /opt/work/search/es/root/ES1/config/elasticsearch.yml | grep "network.host")
echo $RST
