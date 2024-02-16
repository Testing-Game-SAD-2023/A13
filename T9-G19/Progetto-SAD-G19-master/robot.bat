@echo off

title Robot randoop script

REM nome dell'applicazione da testare
set name=%1

REM indica il tempo per ogni esecuzione in secondi
set timelimit=%2

REM Impostare data del giorno
set iter=%3

REM seed value casuale 
set seedvalue=%4

REM timestamp esecuzione precedente
set vecchioiter=%5

REM Percorso java1.8 da modificare per ogni computer
REM set java1.8_path= "C:\Program Files\Java\jre1.8.0_351\bin\java.exe"

REM Percorso java1.8 da modificare per ogni computer
REM set javac= "C:\Program Files\Java\jdk1.8.0_202\bin\javac.exe"

REM percorso della cartella dove è ubicato il JAR di EMMA
set emmapath=.\emma-2.0.5312\lib\emma.jar

REM percorso di RANDOOP
set RANDOOP_PATH=.

REM radice del treefolder
set radice=FolderTree\%name%\RobotTest\RandoopTest\

REM percorso della cartella di destinazione
set outputdir=%radice%%name%-%iter%-dati_di_copertura
set vecchiooutputdir=%radice%%name%-%vecchioiter%-dati_di_copertura

REM creo cartella classes
echo ---CREAZIONE CARTELLA INSTRADAMENTO SE NON ESISTENTE---
mkdir .\classes

REM percorso della classe da testare
set ipvar=.\classes

REM percorso della classe da compilare
set classe= classes\%name%.java

REM creare un .class
echo ---COMPILAZIONE---
javac %classe%

REM instrumentazione dell'archivio jar e creazione della cartella per contenere gli output
echo ---INSTRUMENTAZIONE---
java -noverify -classpath %emmapath% emma instr -m fullcopy -d %outputdir% -ip %ipvar% -out %outputdir%\coverage.em

echo %seedvalue%
REM esegui la sessione e salva le coperture in un file .ec
echo ---ESEGUI LA SESSIONE---
java -noverify -classpath %outputdir%\classes;%emmapath%;randoop-all-3.0.6.jar randoop.main.Main gentests --randomseed=%seedvalue% --testclass=%name% --timelimit=%timelimit%  --junit-output-dir=%outputdir%\%name%_Test

REM copia del file ec nella stessa cartella del em ed eliminazione del precedente
echo ---COPIA FILE NELLA REPOSITORY---
copy coverage.ec %outputdir%\coverage.ec
del coverage.ec

REM generazione .es con il merge di em ed ec
java -classpath %emmapath% emma merge -in %outputdir%\coverage.em,%outputdir%\coverage.ec -out %outputdir%\coverage.es

REM genera il report testuale effettuato il merge con i metadati del file .em
echo ---GENERAZIONE REPORT---
java -classpath %emmapath% emma report -r xml -Dreport.xml.out.file=%outputdir%\coveragetot.xml -in %outputdir%\coverage.es

if exist %vecchiooutputdir%\coverage.es (
	echo ---MERGE---
	java -classpath %emmapath% emma merge -in %outputdir%\coverage.es,%vecchiooutputdir%\coverage.es -out %outputdir%\coverage.es
	java -classpath %emmapath% emma report -r xml -Dreport.xml.out.file=%outputdir%\coveragetot.xml -in %outputdir%\coverage.es
)

REM setto cartella da zippare
set source_folder=%outputdir%\%name%_Test
echo %source_folder%

REM setto la cartella dei test zippati
set zip_file=%outputdir%\%name%_Test.zip
echo %zip_file%


REM creo il file.zip
powershell -nologo -noprofile -command "Compress-Archive -Path '%source_folder%\*' -DestinationPath '%zip_file%'"

echo ho fatto il zip

exit