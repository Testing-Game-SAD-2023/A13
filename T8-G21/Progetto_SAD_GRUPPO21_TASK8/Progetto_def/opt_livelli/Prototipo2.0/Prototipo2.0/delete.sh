#!/bin/bash

PERCORSO="$(cd "$(dirname "$0")" && pwd)"

rm -r $PERCORSO/src/main/java/*
rm -r $PERCORSO/target
rm -r $PERCORSO/evosuite-report
rm $PERCORSO/pom.xml
rm $PERCORSO/livelli.txt
rm $PERCORSO/output.txt

for (( i=0; i<=12; i++ ));
do
    rm -r $PERCORSO/I${i}/*
done

