#!/bin/bash

NETTY=/app/netty
INSTALL_NETTY=es-search-bin.zip
ES_SEARCH_SERVICE=es-search
ES_SEARCH_SERVICE_HOME=$NETTY/$ES_SEARCH_SERVICE

if [ ! -d "$NETTY" ]; then
    mkdir -p "$NETTY"
fi

unzip $INSTALL_NETTY -d $NETTY

echo "install $ES_SEARCH_SERVICE finished, path : $ES_SEARCH_SERVICE_HOME"
