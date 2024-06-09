#!/bin/bash

cd "$(dirname "$0")/../deca"
root="$(pwd)"
tmpDir=$root/../../../tmp

decac="$(which decac)"
if [ "${decac}" == "" ] ; then
    echo "Error, executable \"decac\" not found in"
    echo "${PATH}"
    exit 1
fi

time="$(which time)"

nbpassed=0
nbtests=0

echo -e "\e[34m### INFO: Tests of decac options ###\e[0m"

# we will test the option -P with a file that does not exist
echo -e "\e[34m### INFO: Testing option -P ###\e[0m"

listOfFiles="$root/options/test1.deca $root/options/test2.deca"

# Function to measure time difference
measure_time() {
    start_time=$(date +%s%N)
    $1
    end_time=$(date +%s%N)
    duration=$(( (end_time - start_time) / 1000000 ))
    echo $duration
}

# Measure execution time without -P
time1=$(measure_time "decac $listOfFiles")

# Measure execution time with -P
time2=$(measure_time "decac -P $listOfFiles")

echo

echo "Time without -P option: $time1 milliseconds"
echo "Time with -P option: $time2 milliseconds"

echo

# Compare the times
if [ "$time2" -gt "$time1" ]; then
    echo -e "Options -P : \e[31m FAILED\e[0m"
else
    echo -e "Options -P : \e[32m PASSED\e[0m"
    nbpassed=$((nbpassed+1))
fi
nbtests=$((nbtests+1))

mkdir -p "$tmpDir/options"
echo $nbpassed > "$tmpDir/options/nbpassed.tmp"
echo $nbtests > "$tmpDir/options/nbtests.tmp"


echo

if [ "$nbpassed" -lt "$nbtests" ]; then
    echo -e "Options : \e[31mFAILED\e[0m"
    exit 1
else
    echo -e "Options : \e[32mPASSED\e[0m"
    exit 0
fi