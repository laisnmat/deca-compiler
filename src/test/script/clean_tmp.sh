#!/bin/bash

cd "$(dirname "$0")/../deca"
root="$(pwd)"



repo=$root/../../../tmp

for f in ${repo}/**; do
    if [[ -f "${f}" ]]; then
        echo "Removed file: ${f}"
        rm "${f}"
    fi
done;
for f in ${repo}/**/*; do
    if [[ -f "${f}" ]]; then
        echo "Removed file: ${f}"
        rm "${f}"
    fi
done;

echo "removing directory: ${repo}"
rm -rf "${repo}"

