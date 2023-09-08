#!/bin/bash

# Titolo dello script Robot randoop
echo "Robot randoop script"

# Nome dell'applicazione da testare
name=$1

# Tempo limite per ogni esecuzione in secondi
timelimit=$2

# Impostazione della data del giorno
iter=$3

# Valore seed casuale
seedvalue=$4

# Timestamp dell'esecuzione precedente
vecchioiter=$5

# Percorso di Java 1.8 (modificare per il proprio sistema)
java1_8_path="/usr/lib/jvm/java-1.8-openjdk/bin/java"

# Percorso di javac (modificare per il proprio sistema)
javac="/usr/lib/jvm/java-1.8-openjdk/bin/javac"

# Percorso della cartella dove è ubicato il JAR di EMMA
emmapath="./emma-2.0.5312/lib/emma.jar"

$java1_8_path -version

# Percorso di RANDOOP
RANDOOP_PATH="."

# Radice del treefolder
radice="FolderTree/$name/RobotTest/RandoopTest/"

# Percorso della cartella di destinazione
outputdir="$radice$name-$iter-dati_di_copertura"
vecchiooutputdir="$radice$name-$vecchioiter-dati_di_copertura"

# Creazione della cartella "classes" se non esiste
echo "---CREAZIONE CARTELLA INSTRADAMENTO SE NON ESISTENTE---"
mkdir -p ./classes

# Percorso della classe da testare
ipvar="./classes"

# Percorso della classe da compilare
classe="classes/$name.java"

# Creare un file .class
echo "---COMPILAZIONE---"
$javac $classe

# Instrumentazione dell'archivio JAR e creazione della cartella per contenere gli output
echo "---INSTRUMENTAZIONE---"
$java1_8_path -noverify -classpath $emmapath emma instr -m fullcopy -d $outputdir -ip $ipvar -out $outputdir/coverage.em

echo $seedvalue
# Esegui la sessione e salva le coperture in un file .ec
echo "---ESEGUI LA SESSIONE---"
$java1_8_path -noverify -classpath $outputdir/classes:$emmapath:randoop-all-3.0.6.jar randoop.main.Main gentests --randomseed=$seedvalue --testclass=$name --timelimit=$timelimit --junit-output-dir=$outputdir/${name}_Test

# Copia del file ec nella stessa cartella del em ed eliminazione del precedente
echo "---COPIA FILE NELLA REPOSITORY---"
cp coverage.ec $outputdir/coverage.ec
rm coverage.ec

# Generazione .es con il merge di em ed ec
$java1_8_path -classpath $emmapath emma merge -in $outputdir/coverage.em,$outputdir/coverage.ec -out $outputdir/coverage.es

# Genera il report testuale effettuato il merge con i metadati del file .em
echo "---GENERAZIONE REPORT---"
$java1_8_path -classpath $emmapath emma report -r xml -Dreport.xml.out.file=$outputdir/coveragetot.xml -in $outputdir/coverage.es

if [ -f $vecchiooutputdir/coverage.es ]; then
    echo "---MERGE---"
    $java1_8_path -classpath $emmapath emma merge -in $outputdir/coverage.es,$vecchiooutputdir/coverage.es -out $outputdir/coverage.es
    $java1_8_path -classpath $emmapath emma report -r xml -Dreport.xml.out.file=$outputdir/coveragetot.xml -in $outputdir/coverage.es
fi

# Imposta il nome del file zip
zip_file=$outputdir/${name}_Test.zip
echo $zip_file

touch $zip_file

exit