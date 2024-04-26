#!/bin/sh

# Utilize of the shell 'sh' compliant with the POSIX standard;
# conversion of the characters '[[' and ']]' to be compliant
# with the standard

#Configuring the environment:
#PERCORSO = path of the script, the directory where the Bash script is located
#PERCORSO = /app/Serv
PERCORSO="$(cd "$(dirname "$0")" && pwd)"

#./robot_misurazione_utente.sh arg1 arg2 arg3 arg4 arg5
#e.g. arg1=sourcecode class path=PERCORSO_PACKAGE =/VolumeT8/FolderTreeEvo/Calcolatrice/CalcolatriceSourceCode
PERCORSO_PACKAGE=$1

#e.g. arg2=name of the directory containing the class under test=CalcolatriceSourceCode
NOME_PACKAGE=$2

#e.g. arg3=name of the class under test=Calcolatrice
NOME_CLASSE=$3

#e.g. arg4=path of the directory where the test class is located=PERCORSO_TEST=/VolumeT8/FolderTreeEvo/Calcolatrice/StudentLogin/Player1/Game92/Round92/Turn2/TestReport
PERCORSO_TEST=$4

#e.g. arg5=path of the csv file where the coverage data will be saved=PERCORSO_CSV=/app
PERCORSO_CSV=$5

#e.g. EVOSUITE_WORKING_DI=/VolumeT8/FolderTreeEvo/Calcolatrice/StudentLogin/Player1/Game92/Round92/Turn2/TestReport/evosuite-working-dir
EVOSUITE_WORKING_DIR=$PERCORSO_TEST/evosuite-working-dir

echo "Configuring the environment...\n\n"
echo "percorso :$PERCORSO \n"
echo "percorso pachage :$PERCORSO_PACKAGE \n"
echo "nome package :$NOME_PACKAGE \n"
echo "nome classe :$NOME_CLASSE \n"
echo "percorso test :$PERCORSO_TEST \n"
echo "percorso csv :$PERCORSO_CSV\n"
echo "EvoSuite working directory :$EVOSUITE_WORKING_DIR\n"

#EvoSuite needs to be executed in the same directory of test $PERCORSO_TEST


# PERCORSO_TEST is created by prova_esecuzione_parametri4.js
# file $PERCORSO_TEST/Test$NOME_CLASSE.java is written by prova_esecuzione_parametri4.js


mkdir -p $PERCORSO_TEST/evosuite-working-dir/src/test/java/Tests/
mkdir -p $PERCORSO_TEST/evosuite-working-dir/src/main/java/

cp -r $PERCORSO_PACKAGE $PERCORSO_TEST/evosuite-working-dir/src/main/java/
cp -r $PERCORSO_TEST/Test$NOME_CLASSE.java $PERCORSO_TEST/evosuite-working-dir/src/test/java/Tests/
cp -r $PERCORSO/evosuite-1.0.6.jar $PERCORSO_TEST/evosuite-working-dir/
cp -r $PERCORSO/evosuite-standalone-runtime-1.0.6.jar $PERCORSO_TEST/evosuite-working-dir/
cp -r $PERCORSO/pom.xml $PERCORSO_TEST/evosuite-working-dir/


cd $EVOSUITE_WORKING_DIR

export EVOSUITE="java -jar $(pwd)/evosuite-1.0.6.jar"

mvn clean install

echo "Let's execute the tests...\n\n"

mvn dependency:copy-dependencies

export CLASSPATH=target/classes:evosuite-standalone-runtime-1.0.6.jar:target/test-classes:target/dependency/junit-4.13.1.jar:target/dependency/hamcrest-core-1.3.jar

sleep 2

$EVOSUITE -measureCoverage -class $NOME_CLASSE -Djunit=Test${NOME_CLASSE} -projectCP $EVOSUITE_WORKING_DIR/target/classes:$EVOSUITE_WORKING_DIR/target/test-classes -Dcriterion=LINE

$EVOSUITE -measureCoverage -class $NOME_CLASSE -Djunit=Test${NOME_CLASSE} -projectCP $EVOSUITE_WORKING_DIR/target/classes:$EVOSUITE_WORKING_DIR/target/test-classes -Dcriterion=BRANCH

$EVOSUITE -measureCoverage -class $NOME_CLASSE -Djunit=Test${NOME_CLASSE} -projectCP $EVOSUITE_WORKING_DIR/target/classes:$EVOSUITE_WORKING_DIR/target/test-classes -Dcriterion=EXCEPTION

$EVOSUITE -measureCoverage -class $NOME_CLASSE -Djunit=Test${NOME_CLASSE} -projectCP $EVOSUITE_WORKING_DIR/target/classes:$EVOSUITE_WORKING_DIR/target/test-classes -Dcriterion=WEAKMUTATION

$EVOSUITE -measureCoverage -class $NOME_CLASSE -Djunit=Test${NOME_CLASSE} -projectCP $EVOSUITE_WORKING_DIR/target/classes:$EVOSUITE_WORKING_DIR/target/test-classes -Dcriterion=OUTPUT

$EVOSUITE -measureCoverage -class $NOME_CLASSE -Djunit=Test${NOME_CLASSE} -projectCP $EVOSUITE_WORKING_DIR/target/classes:$EVOSUITE_WORKING_DIR/target/test-classes -Dcriterion=METHOD

$EVOSUITE -measureCoverage -class $NOME_CLASSE -Djunit=Test${NOME_CLASSE} -projectCP $EVOSUITE_WORKING_DIR/target/classes:$EVOSUITE_WORKING_DIR/target/test-classes -Dcriterion=METHODNOEXCEPTION

$EVOSUITE -measureCoverage -class $NOME_CLASSE -Djunit=Test${NOME_CLASSE} -projectCP $EVOSUITE_WORKING_DIR/target/classes:$EVOSUITE_WORKING_DIR/target/test-classes -Dcriterion=CBRANCH


sleep 2

echo "Saving staistics to $PERCORSO_TEST/GameData.csv\n\n"
cp -f evosuite-report/statistics.csv $PERCORSO_TEST/GameData.csv

cd $PERCORSO_TEST
rm -r evosuite-working-dir
