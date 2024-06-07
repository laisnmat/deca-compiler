#!/bin/bash

cd "$(dirname "$0")/../deca"
root="$(pwd)"

decac="$(which decac)"
if [ "${decac}" == "" ] ; then
    echo "Error, executable \"decac\" not found in"
    echo "${PATH}"
    exit 1
fi

echo "### INFO: recompiling java files for $decac ###" 
cd "$(dirname "${decac}")/../../.."
mvn compile || exit 1


syntax_passed=0
syntax_total=0
context_passed=0
context_total=0
codegen_passed=0
codegen_total=0
nbtests=0
nbpassed=0


cd "${root}/context/invalid/00.renduInitial"
echo "### TEST: $(pwd) ###"
rm -f *.res *.ass || exit 1
for f in *.deca ; do
    file="${f%.deca}"
    ((context_total++))
    if decac "${f}" 2> "${file}.res" ; then
	echo "--- ${file}: KO ---"
    elif grep $(cat "${file}.expected") "${file}.res" > /dev/null ; then
	echo "--- ${file}: PASSED ---"
	((context_passed++))
    else
	echo "--- ${file}: FAILED ---"
        echo "DID NOT FOUND STRING \"$(cat ${file}.expected)\""
	echo "IN \"$(cat ${file}.res)\""
    fi
    echo
done

cd "${root}/codegen/valid/00.renduInitial"
echo "### TEST: $(pwd) ###"
rm -f *.res *.ass || exit 1
for f in *.deca ; do
    file="${f%.deca}"
    ((codegen_total++))
    decac "${f}" && (ima "${file}.ass" > "${file}.res")
    if [ -f "${file}.res" ]; then 
	if diff -q "${file}.res" "${file}.expected" > /dev/null ; then
	    echo "--- ${file}: PASSED ---"
	    ((codegen_passed++))
	else
	    echo "--- ${file}: FAILED ---"
	    diff "${file}.expected" "${file}.res" 
	fi
    else
	echo "--- ${file}: KO ---"
    fi
    echo
done


nbpassed=$(($syntax_passed+$context_passed+$codegen_passed))
nbtests=$(($syntax_total+$context_total+$codegen_total))


echo -e "############### SUMMARY ###############"
echo -e "\t\tPASSED\tFAILED"
echo -e "SYNTAX: \t${syntax_passed}\t${syntax_total}"
echo -e "CONTEXT: \t${context_passed}\t${context_total}"
echo -e "CODEGEN: \t${codegen_passed}\t${codegen_total}"
echo
echo -e "TOTAL: \t\t${nbpassed}\t${nbtests}"
echo -e "#######################################"



tmpDir="${root}/tmp/renduInitial"
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