#!/bin/bash

# mvn clean and mvn compile

decac="$(which decac)"
if [ "${decac}" == "" ] ; then
    echo "Error, executable \"decac\" not found in"
    echo "${PATH}"
    exit 1
fi

cd "$(dirname "${decac}")/../../.."

# needs to wait a bit between executions, as not waiting in scripts leads to bugs
sleep 3s
mvn clean
sleep 3s
mvn compile || exit 1
sleep 3s
