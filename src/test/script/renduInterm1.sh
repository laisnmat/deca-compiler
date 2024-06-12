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

if [ -z $COVERAGE] ; then
    # COMPILING
    echo -e "\e[34m### INFO: recompiling java files for $decac ###\e[0m"
    cd "$(dirname "${decac}")/../../.."
    mvn test-compile -Djacoco.skip=true || exit 1
fi

syntax_file=$(mktemp)
context_file=$(mktemp)
codegen_file=$(mktemp)

test_syntax_repo.sh 01.renduInterm1 | tee "$syntax_file"
test_context_repo.sh 01.renduInterm1 | tee "$context_file"
test_codegen_repo.sh 01.renduInterm1 | tee "$codegen_file"

syntax=$(cat "$syntax_file")
context=$(cat "$context_file")
codegen=$(cat "$codegen_file")

tmpDir=$root/../../../tmp

syntax_passed=$(cat $tmpDir/syntax_passed.tmp)
syntax_total=$(cat $tmpDir/syntax_total.tmp)

# get variables context_passed and context_total from file  ./tmp/
context_passed=$(cat $tmpDir/context_passed.tmp)
context_total=$(cat $tmpDir/context_total.tmp)

# get variables codegen_passed and codegen_total from file  ./tmp/
codegen_passed=$(cat $tmpDir/codegen_passed.tmp)
codegen_total=$(cat $tmpDir/codegen_total.tmp)

nbpassed=$(($syntax_passed+$context_passed+$codegen_passed))
nbtests=$(($syntax_total+$context_total+$codegen_total))

echo -e "############### SUMMARY ###############"
echo -e "\t\tPASSED\tTOTAL"
echo -e "SYNTAX: \t${syntax_passed}\t${syntax_total}"
echo -e "CONTEXT: \t${context_passed}\t${context_total}"
echo -e "CODEGEN: \t${codegen_passed}\t${codegen_total}"
echo
echo -e "TOTAL: \t\t${nbpassed}\t${nbtests}"
echo -e "#######################################"


rm "$syntax_file" "$context_file" "$codegen_file"


tmpDir=$root/../../../tmp/renduInterm1
mkdir -p "$tmpDir"
echo $syntax_passed > "$tmpDir/syntax_passed.tmp"
echo $syntax_total > "$tmpDir/syntax_total.tmp"
echo $context_passed > "$tmpDir/context_passed.tmp"
echo $context_total > "$tmpDir/context_total.tmp"
echo $codegen_passed > "$tmpDir/codegen_passed.tmp"
echo $codegen_total > "$tmpDir/codegen_total.tmp"

echo $nbpassed > "$tmpDir/nbpassed.tmp"
echo $nbtests > "$tmpDir/nbtests.tmp"


if [ "$nbpassed" -lt "$nbtests" ]; then
    exit 1
else
    exit 0
fi

