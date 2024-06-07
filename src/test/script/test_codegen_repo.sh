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
    path_repo="**"
    tests_repo="valid"
else
    tests_repo="$(basename ${dest})"
    path_repo="${tests_repo}/**"
fi

nbvalidtests=0
nbvalidpassed=0

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
clean.sh codegen/valid/${path_repo} || exit 1

# STARTING TESTS
echo 
echo -e "\e[34m### STARTING VALID CODEGEN VERIFICATION ###\e[0m"
shopt -s globstar
for f in ${root}/codegen/valid/${path_repo}/*.deca; do
    file_abs="${f%.deca}"
    # get the path to the file, starting from $tests_repo
    file_print=$(echo "$f" | sed "s/.*\(${tests_repo}\)/\1/g")

    ((nbvalidtests++))

    # if there is a '.input' file, feed its content to the execution of the program
    if [ -f "${file_abs}.input" ]; then
        decac -d -d "${f}" &> "${file_abs}.debug" && (ima "${file_abs}.ass" > "${file_abs}.res") << EOF
$(cat "${file_abs}.input")
EOF
    else
        decac -d -d "${f}" &> "${file_abs}.debug" && (ima "${file_abs}.ass" > "${file_abs}.res")
    fi

    if [ -f "${file_abs}.res" ]; then
        if diff -q "${file_abs}.res" "${file_abs}.expected" > /dev/null ; then
            echo -e "--- ${file_print}: ${PASSED} ---"
            ((nbvalidpassed++))
        else
            echo -e "--- ${file_print}: ${FAILED} ---"
            diff "${file_abs}.expected" "${file_abs}.res"
            echo
            echo "DECAC DEBUG:"
            cat "${file_abs}.debug"
        fi
    else
	    echo -e "--- ${file_print}: ${FAILED} ---"
        echo "File not found: ${file_abs}.res"
        echo
        echo "DECAC DEBUG:"
        cat "${file_abs}.debug"
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

tmpDir=$root/../../../tmp
mkdir -p ${tmpDir}
echo $nbvalidpassed > $tmpDir/codegen_passed.tmp
echo $nbvalidtests > $tmpDir/codegen_total.tmp