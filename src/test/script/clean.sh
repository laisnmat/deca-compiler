#!/bin/bash

cd "$(dirname "$0")/../deca"
root="$(pwd)"

if [ -z "$1" ]; then
    path_repo="**"
else
    path_repo="**/${1}/**"
fi

for extension in "res" "ass" "debug"; do
    shopt -s globstar
    for f in ${root}/${path_repo}/*.${extension}; do
        if [[ -f "${f}" ]]; then
            echo "Removed file: ${f}"
            rm "${f}"
        fi
    done
done
