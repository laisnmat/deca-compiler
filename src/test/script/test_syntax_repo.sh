#!/bin/bash

### PARSING ARGUMENTS
recompile=0 # whether to recompile the project or not
dest=""     # the folder to test

for arg in "$@"; do
    if [[ "${arg}" == "-r" ]]; then
        recompile=1
    else
        dest=$arg
    fi
done

### SETTING UP THE ENVIRONMENT
cd "$(dirname "$0")/../deca"
root="$(pwd)"

decac="$(which decac)"
if [ "${decac}" == "" ] ; then
    echo "Error, executable \"decac\" not found in"
    echo "${PATH}"
    exit 1
fi

if [ "${dest}" == "" ]; then
    tests_repo="valid"
    path_repo="**"
else
    tests_repo="$(basename ${dest})"
    path_repo="${tests_repo}/**"
fi

nbvalidtests=0
nbvalidpassed=0

nbinvalidtests=0
nbinvalidpassed=0

PASSED="\e[32mPASSED\e[0m"
FAILED="\e[31mFAILED\e[0m"

# COMPILING
if [[ ${recompile} -eq 1 ]]; then
    echo -e "\e[34m### INFO: recompiling java files for $decac ###\e[0m" 
    maven_recompile.sh || exit 1
fi

# CLEANING OLD RESULT FILES
echo
echo -e "\e[34m### Cleaning 'res' 'ass' 'debug' files ###\e[0m"
clean.sh syntax/valid/${path_repo} || exit 1

# STARTING TESTS
echo
echo -e "\e[34m### STARTING VALID TEST_SYNT VERIFICATION ###\e[0m"
shopt -s globstar
for f in ${root}/syntax/valid/${path_repo}/**/*.deca; do
    file_abs="${f%.deca}"
    # get the path to the file, starting from $tests_repo
    file_print=$(echo "$f" | sed "s/.*\(${tests_repo}\)/\1/g")

    ((nbvalidtests++))

    # if there is an error during test_synt, 
    # then the test is FAILED 
    if test_synt "${f}" &> "${file_abs}.res" ; then
        # if there is an expected file, compare with the result,
        # else the test is PASSED
        if [ -f "${file_abs}.expected" ]; then 
            if diff -q "${file_abs}.res" "${file_abs}.expected" > /dev/null ; then
                echo -e "--- ${file_print}: ${PASSED} ---"
                ((nbvalidpassed++))
            else
                echo -e "--- ${file_print}: ${FAILED} ---"
                diff "${file_abs}.expected" "${file_abs}.res" 
            fi
        else
            echo -e "--- ${file_print}: ${PASSED} (without '.expected' file) ---"
            ((nbvalidpassed++))
        fi
    else
	    echo -e "--- ${file_print}: ${FAILED} ---"
        cat "${file_abs}.res"
    fi
    echo
done

# RESULTS
if [ ${nbvalidpassed} -eq ${nbvalidtests} ]; then
    SCORE_COLOR="\e[32m"
else
    SCORE_COLOR="\e[31m"
fi
echo -e "### VALID SCORE: ${SCORE_COLOR}${nbvalidpassed} PASSED / ${nbvalidtests} TESTS \e[0m###"
echo

if [[ "${tests_repo}" == "valid" ]]; then
    tests_repo="invalid"
fi

# CLEANING OLD RESULT FILES
echo
echo -e "\e[34m### Cleaning 'res' 'ass' 'debug' files ###\e[0m"
clean.sh syntax/invalid/${path_repo} || exit 1

# STARTING TESTS
echo
echo -e "\e[34m### STARTING INVALID TEST_SYNT VERIFICATION ###\e[0m"
shopt -s globstar
for f in ${root}/syntax/invalid/${path_repo}/**/*.deca; do
    file_abs="${f%.deca}"
    # get the path to the file, starting from $tests_repo
    file_print=$(echo "$f" | sed "s/.*\(${tests_repo}\)/\1/g")
    
    ((nbinvalidtests++))

    if test_synt "${f}" &> "${file_abs}.res" ; then
	    echo -e "--- ${file_print}: ${FAILED} ---"
        echo "    Test passed but shouldn't have as it is invalid."
    elif [[ ! -f "${file_abs}.expected" ]]; then
	    echo -e "--- ${file_print}: ${FAILED} ---"
        echo "    File not found: ${file_abs}.expected"
    elif grep $(cat "${file_abs}.expected") "${file_abs}.res" > /dev/null ; then
	    echo -e "--- ${file_print}: ${PASSED} ---"
	    ((nbinvalidpassed++))
    else
        echo -e "--- ${file_print}: ${FAILED} ---"
        echo "DID NOT FOUND STRING \"$(cat ${file_abs}.expected)\""
        echo "IN \"$(cat ${file_abs}.res)\""
    fi
    echo
done

# RESULTS
if [ ${nbinvalidpassed} -eq ${nbinvalidtests} ]; then
    SCORE_COLOR="\e[32m"
else
    SCORE_COLOR="\e[31m"
fi
echo -e "### INVALID SCORE: ${SCORE_COLOR}${nbinvalidpassed} PASSED / ${nbinvalidtests} TESTS \e[0m###"
echo

# PRINT FINAL SCORE
nbpassed=$(($nbinvalidpassed+$nbvalidpassed))
nbtests=$(($nbinvalidtests+$nbvalidtests))

if [ ${nbpassed} -eq ${nbtests} ]; then
    SCORE_COLOR="\e[32m"
else
    SCORE_COLOR="\e[31m"
fi
export SYNTAX_NB_PASSED="${nbpassed}"

echo -e "### TOTAL SCORE: ${SCORE_COLOR}${nbpassed} PASSED / ${nbtests} TESTS \e[0m###"

mkdir -p ./tmp/
echo $nbpassed > ./tmp/syntax_passed.tmp
echo $nbtests > ./tmp/syntax_total.tmp