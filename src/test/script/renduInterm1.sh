#!/bin/bash

cd "$(dirname "$0")/../deca"
root="$(pwd)"

decac="$(which decac)"
if [ "${decac}" == "" ] ; then
    echo "Error, executable \"decac\" not found in"
    echo "${PATH}"
    exit 1
fi

syntax_passed=0
syntax_total=0
context_passed=0
context_total=0
codegen_passed=0
codegen_total=0

# COMPILING
echo -e "\e[34m### INFO: recompiling java files for $decac ###\e[0m" 
maven_recompile.sh || exit 1

syntax=$( test_syntax_repo.sh 01.renduInterm1 | tee /dev/tty)
context=$( test_context_repo.sh 01.renduInterm1 | tee /dev/tty)
codegen=$( test_codegen_repo.sh 01.renduInterm1 | tee /dev/tty)

syntax_passed=$(echo "$syntax" | grep -oP '\d+(?= PASSED)' | awk '{s+=$1} END {print s}')
syntax_total=$(echo "$syntax" | grep -oP '\d+(?= TESTS)' | awk '{s+=$1} END {print s}')

context_passed=$(echo "$context" | grep -oP '\d+(?= PASSED)' | awk '{s+=$1} END {print s}')
context_total=$(echo "$context" | grep -oP '\d+(?= TESTS)' | awk '{s+=$1} END {print s}')

codegen_passed=$(echo "$codegen" | grep -oP '\d+(?= PASSED)' | awk '{s+=$1} END {print s}')
codegen_total=$(echo "$codegen" | grep -oP '\d+(?= TESTS)' | awk '{s+=$1} END {print s}')

echo -e "############### SUMMARY ###############"
echo -e "\t\tPASSED\tFAILED"
echo -e "SYNTAX: \t${syntax_passed}\t${syntax_total}"
echo -e "CONTEXT: \t${context_passed}\t${context_total}"
echo -e "CODEGEN: \t${codegen_passed}\t${codegen_total}"
echo
echo -e "TOTAL: \t\t$(($syntax_passed+$context_passed+$codegen_passed))\t$(($syntax_total+$context_total+$codegen_total))"
echo -e "#######################################"

nbpassed=$(($syntax_passed+$context_passed+$codegen_passed))
nbtests=$(($syntax_total+$context_total+$codegen_total))
if [ "$nbpassed" -lt "$nbtests" ]; then
    exit 1
else
    exit 0
fi