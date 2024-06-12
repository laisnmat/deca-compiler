#!/bin/bash


cd "$(dirname "$0")/../deca"
root="$(pwd)"


echo "###############################"
echo "######   SUMMARY TESTS   ######"
echo -e "###############################\n\n"

tmpDir=$root/../../../tmp/renduInitial
syntax_passed_initial=$(cat $tmpDir/syntax_passed.tmp)
syntax_total_initial=$(cat $tmpDir/syntax_total.tmp)
context_passed_initial=$(cat $tmpDir/context_passed.tmp)
context_total_initial=$(cat $tmpDir/context_total.tmp)
codegen_passed_initial=$(cat $tmpDir/codegen_passed.tmp)
codegen_total_initial=$(cat $tmpDir/codegen_total.tmp)
nbpassed_initial=$(cat $tmpDir/nbpassed.tmp)
nbtests_initial=$(cat $tmpDir/nbtests.tmp)


tmpDir=$root/../../../tmp/renduInterm1
syntax_passed_interm1=$(cat $tmpDir/syntax_passed.tmp)
syntax_total_interm1=$(cat $tmpDir/syntax_total.tmp)
context_passed_interm1=$(cat $tmpDir/context_passed.tmp)
context_total_interm1=$(cat $tmpDir/context_total.tmp)
codegen_passed_interm1=$(cat $tmpDir/codegen_passed.tmp)
codegen_total_interm1=$(cat $tmpDir/codegen_total.tmp)
nbpassed_interm1=$(cat $tmpDir/nbpassed.tmp)
nbtests_interm1=$(cat $tmpDir/nbtests.tmp)

tmpDir=$root/../../../tmp/renduInterm2
syntax_passed_interm2=$(cat $tmpDir/syntax_passed.tmp)
syntax_total_interm2=$(cat $tmpDir/syntax_total.tmp)
context_passed_interm2=$(cat $tmpDir/context_passed.tmp)
context_total_interm2=$(cat $tmpDir/context_total.tmp)
codegen_passed_interm2=$(cat $tmpDir/codegen_passed.tmp)
codegen_total_interm2=$(cat $tmpDir/codegen_total.tmp)
nbpassed_interm2=$(cat $tmpDir/nbpassed.tmp)
nbtests_interm2=$(cat $tmpDir/nbtests.tmp)

tmpDir=$root/../../../tmp/renduFinal1
syntax_passed_final1=$(cat $tmpDir/syntax_passed.tmp)
syntax_total_final1=$(cat $tmpDir/syntax_total.tmp)
context_passed_final1=$(cat $tmpDir/context_passed.tmp)
context_total_final1=$(cat $tmpDir/context_total.tmp)
codegen_passed_final1=$(cat $tmpDir/codegen_passed.tmp)
codegen_total_final1=$(cat $tmpDir/codegen_total.tmp)
nbpassed_final1=$(cat $tmpDir/nbpassed.tmp)
nbtests_final1=$(cat $tmpDir/nbtests.tmp)

syntax_passed=$(($syntax_passed_initial+$syntax_passed_interm1+$syntax_passed_interm2+$syntax_passed_final1))
syntax_total=$(($syntax_total_initial+$syntax_total_interm1+$syntax_total_interm2+$syntax_total_final1))
context_passed=$(($context_passed_initial+$context_passed_interm1+$context_passed_interm2+$context_passed_final1))
context_total=$(($context_total_initial+$context_total_interm1+$context_total_interm2+$context_total_final1))
codegen_passed=$(($codegen_passed_initial+$codegen_passed_interm1+$codegen_passed_interm2+$codegen_passed_final1))
codegen_total=$(($codegen_total_initial+$codegen_total_interm1+$codegen_total_interm2+$codegen_total_final1))

nbpassed=$(($nbpassed_initial+$nbpassed_interm1+$nbpassed_interm2+$nbpassed_final1))
nbtests=$(($nbtests_initial+$nbtests_interm1+$nbtests_interm2+$nbtests_final1))

if [ "$nbpassed_initial" -lt "$nbtests_initial" ]; then
    echo -e "renduInitial : \033[31mFAILED\033[0m"
else
    echo -e "renduInitial : \033[32mPASSED\033[0m"
fi

echo

if [ "$nbpassed_interm1" -lt "$nbtests_interm1" ]; then
    echo -e "RenduInterm1 : \033[31mFAILED\033[0m"
else
    echo -e "RenduInterm1 : \033[32mPASSED\033[0m"
fi

echo

if [ "$nbpassed_interm2" -lt "$nbtests_interm2" ]; then
    echo -e "RenduInterm2 : \033[31mFAILED\033[0m"
else
    echo -e "RenduInterm2 : \033[32mPASSED\033[0m"
fi

echo

if [ "$nbpassed_final1" -lt "$nbtests_final1" ]; then
    echo -e "RenduFinal1 : \033[31mFAILED\033[0m"
else
    echo -e "RenduFinal1 : \033[32mPASSED\033[0m"
fi

echo

echo -e "############### SUMMARY ###############"
echo -e "\t\tPASSED\tTOTAL"
echo -e "SYNTAX: \t${syntax_passed}\t${syntax_total}"
echo -e "CONTEXT: \t${context_passed}\t${context_total}"
echo -e "CODEGEN: \t${codegen_passed}\t${codegen_total}"
echo
echo -e "TOTAL: \t\t${nbpassed}\t${nbtests}"
echo -e "#######################################"

if [ "$nbpassed" -lt "$nbtests" ]; then
    exit 1
else
    exit 0
fi


