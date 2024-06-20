#!/bin/bash

# deca -v doubleDefException.deca
# wait for input to continue 
# store the key pressed in $key

projetPath="$HOME/Ensimag/gl03"
soutenancePath=$projetPath/soutenance/demo/stepB

cd $soutenancePath

bat2="$soutenancePath/../bat --language java --pager=never"

index=1
currentFile=$(ls 0$index*)
options=""
com=""
coloredCom=""



header="Command : 
    Enter : execute the command
    n : pass to next file
    f : choose file
    a : display
    l : test_lex
    s : test_syntax
    c : test_context
    d : deca 
    o : add options
    r : recompile
    del : clear the terminal
    q : quit"


echo -e "$header" 
echo
echo -e "\033[1;91mCurrent file: \033[0;35m$currentFile\033[0m"
escape_char=$(printf "\u1b")
read -rsn1 key # get 1 character
if [[ $key == $escape_char ]]; then
    read -rsn2 key # read 2 more chars
fi
# while key not q

while [ "$key" != "q" ]
do
    if [ "$key" == "n" ]
    then
        clear
        index=$((index+1))
        if [ "$index" -lt 10 ]
        # currentFile=$(ls 0$index*) if empty echo "end" and not display error of ls
        then
            currentFile=$(ls 0$index* 2>/dev/null)
        else
            currentFile=$(ls $index* 2>/dev/null)
        fi
        if [ "$currentFile" == "" ]
        then
            echo -e "\033[1;91mEnd of files\033[0m"
            index=$((index-1))
            currentFile=$(ls 0$index*)
        else
            echo
            echo -e "\033[1;91mCurrent file: \033[0;35m$currentFile\033[0m"
        fi
    elif [ "$key" == "f" ]
    then
        clear
        i=0
        for file in $(ls [0-9]*.deca)
        do
            i=$((i+1))
            echo -e "\033[1;96m$i\033[0;33m : $file\033[0m"
        done
        echo -e -n "\033[1;91mChoose a file between \033[34m1 \033[91mand \033[34m$i\033[0m : "
        read index
        currentFile=$(ls 0$index*)
        echo
        echo -e "\033[1;91mCurrent file: \033[0;35m$currentFile\033[0m"
    elif [ "$key" == "l" ]
    then
        com="test_lex $currentFile"
        coloredCom="\033[1;96mtest_lex \033[0;33m$currentFile\033[0m"
        echo
        echo -e $coloredCom
    elif [ "$key" == "s" ]
    then
        com="test_synt $currentFile"
        coloredCom="\033[1;96mtest_synt \033[0;33m$currentFile\033[0m"
        echo
        echo -e $coloredCom
    elif [ "$key" == "c" ]
    then
        com="test_context $currentFile"
        coloredCom="\033[1;96mtest_context \033[0;33m$currentFile\033[0m"
        echo
        echo -e $coloredCom
    elif [ "$key" == "a" ]
    then
        $bat2 $currentFile
    elif [ "$key" == "h" ]
    then
        echo -e "$header"
    # enter key
    elif [ "$key" == "" ]
    then
        echo -e "> $coloredCom"
        $com
    elif [ "$key" == "" ]
    then
        clear
    elif [ "$key" == "r" ]
    then
        clear
        echo "Recompile"
        cd $projetPath
        mvn clean test-compile
        cd $soutenancePath
        echo
        echo -e "\033[1;91mCurrent file: \033[0;35m$currentFile\033[0m"
    elif [ "$key" == "d" ]
    then
        com="decac $options $currentFile"
        coloredCom="\033[1;96mdecac \033[0;92m$options \033[0;33m$currentFile\033[0m"
        echo -e $coloredCom
    elif [ "$key" == "o" ]
    then
        echo -n -e "\033[1;91mEnter options : \033[0m"
        read options
        echo -e "\033[1;96mOptions set to \033[0;92m$options\033[0m"
        com="decac $options $currentFile"
        coloredCom="\033[1;96mdecac \033[0;92m$options \033[0;33m$currentFile\033[0m"
        echo -e $coloredCom
    # arrow right index++
    elif [ $key = "[C" ]
    then
        index=$((index+1))
        if [ "$index" -lt 10 ]
        then
            currentFile=$(ls 0$index* 2>/dev/null)
        else
            currentFile=$(ls $index* 2>/dev/null)
        fi
        if [ "$currentFile" == "" ]
        then
            # echo -e "\033[1;91mEnd of files\033[0m"
            index=$((index-1))
            currentFile=$(ls 0$index*)
        else
            echo
            echo -e "\033[1;91mCurrent file: \033[0;35m$currentFile\033[0m"
        fi
    # arrow left index--
    elif [ $key = "[D" ]
    then
        index=$((index-1))
        if [ "$index" -lt 10 ]
        then
            currentFile=$(ls 0$index* 2>/dev/null)
        else
            currentFile=$(ls $index* 2>/dev/null)
        fi
        if [ "$currentFile" == "" ]
        then
            index=$((index+1))
            currentFile=$(ls 0$index*)
        else
            echo
            echo -e "\033[1;91mCurrent file: \033[0;35m$currentFile\033[0m"
        fi
    else
        echo -n ""
    fi
    read -rsn1 key # get 1 character
    if [[ $key == $escape_char ]]; then
        read -rsn2 key # read 2 more chars
    fi
done

exit 0