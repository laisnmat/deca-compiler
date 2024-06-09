#!/bin/bash

cd "$(dirname "$0")/../deca"
root="$(pwd)"

decac="$(which decac)"
if [ "${decac}" == "" ] ; then
    echo "Error, executable \"decac\" not found in"
    echo "${PATH}"
    exit 1
fi

time="$(which time)"

FAILED=0

echo -e "\e[34m### INFO: Tests of decac options ###\e[0m"

# we will test the option -P with a file that does not exist
echo -e "\e[34m### INFO: Testing option -P ###\e[0m"

listOfFiles="$root/options/test1.deca $root/options/test2.deca"

time1=$($time -f "%e" decac $listOfFiles 2>&1)
time2=$($time -f "%e" decac -P $listOfFiles 2>&1)

echo

echo "Time without -P option: $time1 seconds"
echo "Time with -P option: $time2 seconds"

echo

# Comparing times using bc for floating point arithmetic
if (( $(echo "$time2 > $time1" | bc -l) )); then
    echo -e "Options -P : \e[31m FAILED\e[0m"
    FAILED=1
else
    echo -e "Options -P : \e[32m PASSED\e[0m"
fi

exit $FAILED