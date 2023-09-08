#!/bin/bash
cont=0
PERCORSO="$(cd "$(dirname "$0")" && pwd)"
NOME_CLASSE=$(head -n 1 "output.txt")
NOME_PACKAGE=$(tail -n 1 "output.txt")
echo "nome classe :$NOME_CLASSE"
echo "nome package :$NOME_PACKAGE"
export EVOSUITE="java -jar $(pwd)/evosuite-1.0.6.jar"
filename="/opt/Prototipo2.0/Prototipo2.0/invio.txt"

##############################

# Itera su tutti gli argomenti passati allo script
for var in "$@"; do
	if [ $var -eq -1 ]; then
		echo "trovato -1"
		break
	fi

	echo "Variabile $cont: $var" >> "$filename"

echo "Il file $filename è stato creato con successo."
	((cont++))
	export CLASSPATH=target/classes:evosuite-standalone-runtime-1.0.6.jar:I${var}/evosuite-tests:target/dependency/junit-4.13.1.jar:target/dependency/hamcrest-core-1.3.jar
	javac I${var}/evosuite-tests/$NOME_PACKAGE/*.java

	$EVOSUITE -measureCoverage -class $NOME_PACKAGE.$NOME_CLASSE -Djunit=$NOME_PACKAGE.${NOME_CLASSE}_ESTest -projectCP $PERCORSO/target/classes/:I${var}/evosuite-tests -Dcriterion=LINE
	echo "coverage 1  finita" >> "$filename"
	$EVOSUITE -measureCoverage -class $NOME_PACKAGE.$NOME_CLASSE -Djunit=$NOME_PACKAGE.${NOME_CLASSE}_ESTest -projectCP $PERCORSO/target/classes/:I${var}/evosuite-tests -Dcriterion=BRANCH
	echo "coverage 2 finita" >> "$filename"
	$EVOSUITE -measureCoverage -class $NOME_PACKAGE.$NOME_CLASSE -Djunit=$NOME_PACKAGE.${NOME_CLASSE}_ESTest -projectCP $PERCORSO/target/classes/:I${var}/evosuite-tests -Dcriterion=EXCEPTION
	echo "coverage 3 finita" >> "$filename"
	$EVOSUITE -measureCoverage -class $NOME_PACKAGE.$NOME_CLASSE -Djunit=$NOME_PACKAGE.${NOME_CLASSE}_ESTest -projectCP $PERCORSO/target/classes/:I${var}/evosuite-tests -Dcriterion=WEAKMUTATION
	echo "coverage 4  finita" >> "$filename"
	$EVOSUITE -measureCoverage -class $NOME_PACKAGE.$NOME_CLASSE -Djunit=$NOME_PACKAGE.${NOME_CLASSE}_ESTest -projectCP $PERCORSO/target/classes/:I${var}/evosuite-tests -Dcriterion=OUTPUT
	echo "coverage 5  finita" >> "$filename"
	$EVOSUITE -measureCoverage -class $NOME_PACKAGE.$NOME_CLASSE -Djunit=$NOME_PACKAGE.${NOME_CLASSE}_ESTest -projectCP $PERCORSO/target/classes/:I${var}/evosuite-tests -Dcriterion=METHOD
	echo "coverage 6  finita" >> "$filename"
	$EVOSUITE -measureCoverage -class $NOME_PACKAGE.$NOME_CLASSE -Djunit=$NOME_PACKAGE.${NOME_CLASSE}_ESTest -projectCP $PERCORSO/target/classes/:I${var}/evosuite-tests -Dcriterion=METHODNOEXCEPTION
	echo "coverage 7  finita" >> "$filename"
	$EVOSUITE -measureCoverage -class $NOME_PACKAGE.$NOME_CLASSE -Djunit=$NOME_PACKAGE.${NOME_CLASSE}_ESTest -projectCP $PERCORSO/target/classes/:I${var}/evosuite-tests -Dcriterion=CBRANCH
	echo "coverage 8  finita" >> "$filename"
	mv evosuite-report/statistics.csv /data/$NOME_CLASSE/RobotTest/EvoSuiteTest/0${cont}Level/TestReport

	mv I${var}/evosuite-tests/$NOME_PACKAGE /data/$NOME_CLASSE/RobotTest/EvoSuiteTest/0${cont}Level/TestSourceCode


done

rm -r $PERCORSO/src/main/java/*
rm -r $PERCORSO/target
rm -r $PERCORSO/evosuite-report
rm $PERCORSO/pom.xml
rm $PERCORSO/livelli.txt
rm $PERCORSO/output.txt
rm $PERCORSO/invio.txt
for (( i=0; i<=12; i++ ));
do
    rm -r $PERCORSO/I${i}/*
done


