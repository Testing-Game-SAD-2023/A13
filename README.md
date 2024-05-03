# Gruppo A13-2024
Componente:
- Caterina Maria Accetto - M63/1117

Il lavoro che andremo ad esporre all'interno della documentazione, muove i suoi primi passi a partire dal progetto: A10-2024,
e vuole essere solamente una versione migliorativa dei requisiti sviluppati in corrispondenza dei serivizi T1 e T23, 
di conseguenza rimane invariata: l'architettura complessiva dell'applicazione, diagrammi
di deployment, dei componenti, ... .

Il progetto è stato ampliato integrando il lavoro svolto dal gruppo A7-2024 che si è occupato: (a) di revisionare la struttura del FileSystem condiviso tra i volumi T8 e T9 optando per utilizzarne uno soltanto (il VolumeT8), (b) del proporre un metodo per il calcolo del punteggio di una partita e (c) di introdurre una nuova modalità di gioco denominata: "Allenamento" nata dalla necessità di offrire ai giocatori la possibilità di migliorare le proprie abilità nella scrittura dei test senza che i punteggi ottenuti influenzino le statistiche dell'account.

### REQUISITI SVILUPPATI (1)
1. Il sistema, per ogni sessione di autenticazione correttamente effettuata, deve assegnare all'amministratore un token di autenticazione
2. Il sistema deve implementare un meccanismo che permetta di distinguere gli amministratori, aventi particolari privilegi, dai semplici giocatori
3. Il sistema deve consentire ai soli utenti aventi specifiche caratteristiche di potersi registrare inserendo: nome, cognome, username, e-mail e password
4. Il sistema deve prevedere la possibilità, in fase di registrazione, di ammettere utenti con doppi nomi o, più in generale, con più nomi separati da uno spazio, ad esempio: "Anna Maria"
5. Il sistema deve prevedere la possibilità, in fase di registrazione,di ammettere utenti con cognomi costituiti da più parole separate da spazi oppure da apostrofi, come nel caso:"Degl'Antoni" 
6. Il sistema deve, accettare, in fase di registrazione, solamente password lunghe tra gli 8 e i 16 caratteri e che contengano:almeno una lettera maiuscola, una minuscola, un numero ed un carattere speciale
7. Il sistema, in fase di registrazione, deve implementare una apposita misura di sicurezza che consenta di proteggere le password archiviate all'interno del database le quali dovranno venir crittate mediante un algoritmo di hashing 
8. Il sistema deve controllare che tutti i campi, negli appositi form di login e di registrazione, vengano compilati e che siano validi,in caso di errore, deve essere visualizzato a video un messaggio di errore
9. Il sistema deve permettere all'amministratore registrato di effettuare il logout 
10. Il sistema deve consentire all'amministratore correttamente registrato ed autenticato, di visualizzare, in una apposita sezione dell'area riservata, la lista di tutti gli admin del sistema
11. Il sistema deve permettere all'amministratore correttamente registrato, di impostare una nuova password nel caso in cui avesse dimenticato la propria
12. Il sistema deve consentire a tutti gli amministratori correttamente registrati ed autenticati, di invitarne di nuovi, e che non necessariamente rispettano i vincoli richiesti, tramite l'invio, per posta elettronica, di un apposito token
13. Il sistema deve permettere agli utenti che hanno ricevuto il token di invito per posta, di potersi registrare nel sistema come admin
14. Il sistema deve implementare un meccanismo che permetta di distinguere gli amministratori del dominio da quelli che sono stati invitati
15. Il sistema, in caso di avvenuta registrazione, deve mostrare a video un feedback, dopodichè, indirizzare l'utente alla pagina di login
16. Il sistema deve consentire agli utenti di decidere se registrarsi tramite il classico form (e-mail + password) oppure effettuare il social login tramite Facebook e completare in questo modo la procedura di registrazione
17. il sistema deve essere provvisto di un entry-point, una pagina dove poter smistare gli utenti dell' applicazione in base al ruolo ricoperto ed indirizzarli verso le sezioni dedicate

### REQUISITI SVILUPPATI (2)
1. la struttura del FileSystem è stata modificata per garantire che ciascun player registrato ed autenticato, caratterizato da un ID univoco, sia dotato di una personale cartella di gioco per memorizzare le partite giocate ed i test ed i risultati prodotti dalla misurazione con il robot, e.g.:

```
VolumeT8/FolderTreeEvo/Calcolatrice/StudentLogin/Player1/GameXX/RoundXX/...
```
2. la funzionalità dello script: **"robot_misurazione_utente.sh"** (task T8) è stata modificata per evitare conflitti tra più giocatori: precedentemente, poichè lo script veniva eseguito sempre nella stessa cartella per tutti gli utilizzatori dell'applicazione, era possibile che i risultati prodotti dalla copertura potessero venir sovrascritti, impedendo la corretta memorizzazione dei risultati per tutti i players; con la nuova modifica, adesso, viene creata una cartella temporanea per EvoSuite (**evosuite-working-dir**) e che, al termine della procedura, verrà cancellata.

3. il template dell'editor di gioco (**editor.js** ed **editor.html**) del task T5 è stato modificato in maniera tale da memorizzare una serie di informazioni di utilità (data, nome della classe da testare ed username del player), le quali verranno "catturate" in automatico alla ricezione della CUT (Class Under Test), e di contatto: i giocatori hanno la possibilità di inserire manualmente il proprio nome ed il cognome all'interno dei campi prestabiliti.
```html
/*Compila i campi "Nome" e "Cognome" con le informazioni richieste
Nome: "inserire il proprio nome"
Cognome: "inserire il proprio cognome"
Username: username
UserID: userID
Date: date
*/
```
4. alla ricezione della CUT, all'interno dell'editor di gioco, non sarà più necessario inserire manualmente il nome della classe da testare preceduto da 'Test', adesso in automatico si andrà a convertire:
```html
public class TestClasse in public class Test<CUT ricevuta>
```
5. il file di installazione (**installer.bat**) è stato modificato garantendo che prima che i containers vengano creati, si vadano a compilare tutti i files necessari, una procedura di questo tipo è essenziale per assicurarsi che tutte le modifiche apportate all'applicazione vegano correttamente "visualizzate".
```bat
@echo off

GOTO :MAIN

REM Installer function for T1-G11
:function1
echo "Installing T1-G11"
cd "./T1-G11/applicazione/manvsclass"
call mvn package || ( echo "Error in T1-G11 installation during mvn package" &&  exit /b 1 )
docker compose up -d --build || ( echo "Error in T1-G11 installation during docker compose up" &&  exit /b 1 )
exit /b 0

REM Installer function for T23-G1
:function2
echo "Installing T23-G1"
cd "./T23-G1"
call mvn package || ( echo "Error in T23-G1 installation during mvn package" && exit /b 1 )
docker compose up -d --build || ( echo "Error in T23-G1 installation during docker compose up" && exit /b 1 )
exit /b 0

REM Installer function for T4-G18
:function3
echo "Installing T4-G18"
cd "./T4-G18"
docker compose up -d --build || ( echo "Error in T4-G18 installation during docker compose up"  && exit /b 1 )
exit /b 0

REM Installer function for T5-G2
:function4
echo "Installing T5-G2"
cd "./T5-G2/t5"
call mvn package || ( echo "Error in T5-G2 installation during mvn package" && exit /b 1 )
docker compose up -d --build || ( echo "Error in T5-G2 installation during docker compose up" && exit /b 1 )
exit /b 0

REM Installer function for T6-G12
:function5
echo "Installing T6-G12"
cd "./T6-G12/T6"
call mvn package || ( echo "Error in T6-G12 installation during mvn package" && exit /b 1 )
docker compose up -d --build || ( echo "Error in T6-G12 installation during docker compose up" && exit /b 1 )
exit /b 0

REM Installer function for T7-G31
:function6
echo "Installing T7-G31"
cd "./T7-G31/RemoteCCC"
call mvn package || ( echo "Error in T7-G31 installation during mvn package" && exit /b 1 )
docker compose up -d --build || ( echo "Error in T7-G31 installation during docker compose up" && exit /b 1 )
exit /b 0

REM Installer function for T8-G21
:function7
echo "Installing T8-G21"
cd "./T8-G21/Progetto_SAD_GRUPPO21_TASK8/Progetto_def/opt_livelli/Prototipo2.0"
cd "Serv"
call mvn package || ( echo "Error in T8-G21 installation during mvn package" && exit /b 1 )
cd ..
docker compose up -d --build || ( echo "Error in T8-G21 installation during docker compose up" && exit /b 1 )
exit /b 0

REM Installer function for T9-G19
:function8
echo "Installing T9-G19"
cd "./T9-G19\Progetto-SAD-G19-master"
call mvn package || ( echo "Error in T9-G19 installation during mvn package" && exit /b 1 )
docker compose up -d --build || ( echo "Error in T9-G19 installation during docker compose up" && exit /b 1 )
exit /b 0

REM Installer function for ui_gateway
:function9
echo "Installing ui_gateway"
cd "./ui_gateway"
docker compose up -d --build || ( echo "Error in ui_gateway installation during docker compose up" && exit /b 1 )
exit /b 0

REM Installer function for api_gateway
:function10
echo "Installing api_gateway"
cd "./api_gateway"
call mvn package || ( echo "Error in api_gateway installation during mvn package" && exit /b 1 )
docker compose up -d --build || ( echo "Error in ui_gateway installation during docker compose up" && exit /b 1 )
exit /b 0


:MAIN
REM Avvio dell'installazione
echo "Installation started"

REM Creazione del volume Docker 'VolumeT9'
docker volume create VolumeT9 || ( echo "Error in creating the volume T9" && exit /b 1 )

REM Creazione del volume Docker 'VolumeT8'
docker volume create VolumeT8 || ( echo "Error in creating the volume T8" && exit /b 1 )

REM Creazione della rete Docker 'global-network'
docker network create global-network || ( echo "Error in creating the network global-network" && exit /b 1 )

for /l %%i in (1,1,10) do (

   pushd .
   echo "Calling function # %%i:"
   call :function%%i
   if errorlevel 1 (
      echo "Error detected on function %%i, exiting script."
      exit /b 1
   )
   popd
   echo.
)


REM RunScript.bat
echo "Executing RunCommands.ps1"
powershell -ExecutionPolicy Bypass -File RunCommands.ps1

REM Messaggio di completamento dell'installazione
echo "Installation completed"
pause
```
6. Il workspace è stato ripulito eliminando da GitHub tutti i files generati dalla compilazione (**.gitignore**), in particolare non verranno più "pushati" tutti i files memorizzati all'interno delle cartelle target; in questo modo si semplifica il processo di integrazione e si velocizza la sincronizzazione, non dovendo controllare troppi files da dover "pushare" all'interno del repository condiviso.

7. il DockerFile relativo il container manvsclass (task T1) è stato modificato fissando la versione di Ubuntu da utilizzare alla 22.04 onde evitare problemi legati l'installazione di una serie di librerie necessarie
```DockerFile
FROM ubuntu:22.04
# FROM openjdk:8-alpine as java8

# FROM openjdk:17-alpine
# COPY --from=java8 /usr/lib/jvm/java-1.8-openjdk /usr/lib/jvm/java-1.8-openjdk

RUN apt-get update && apt-get install -y openjdk-8-jdk bash
RUN apt-get install -y openssl libncurses5 libstdc++6

# RUN apk update && apk add bash
# RUN apk add --no-cache openssl ncurses-libs libstdc++

COPY target/manvsclass-0.0.1-SNAPSHOT.jar /app/manvsclass.jar
COPY installazione.sh /app
COPY evosuite-1.0.6.jar /app
COPY evosuite-standalone-runtime-1.0.6.jar /app

WORKDIR /app

RUN bash installazione.sh

#WORKDIR /app

EXPOSE 8080
CMD ["java","-jar","manvsclass.jar"]
```
8. Nel corso dell'installazione dello script **installer.bat** è possibile incappare nell'errore:
```bash
=> ERROR [controller 9/9] RUN bash installazione.sh                                                                           3.8s 
------
> [controller 9/9] RUN bash installazione.sh:
0.304
0.304 WARNING: apt does not have a stable CLI interface. Use with caution in scripts.
0.304
0.304 E: Invalid operation update
0.350 Reading package lists...
1.687 Building dependency tree...
1.955 Reading state information...
1.978 E: Unable to locate package software-properties-common
1.980 installazione.sh: line 4: apt-add-repository: command not found
1.983 E: Invalid operation update
2.031 Reading package lists...
3.291 Building dependency tree...
3.588 Reading state information...
3.610 E: Unable to locate package nodejs
3.612 installazione.sh: line 7: $'\r': command not found
3.613 sleep: invalid time interval '3\r'
3.613 Try 'sleep --help' for more information.
3.614 installazione.sh: line 9: $'\r': command not found
3.614 le versioni di java e javac sono le seguenti, è necessaria la versione 1.8:
3.614 installazione.sh: line 11: $'\r': command not found
for java not registered; not settingernative /usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java
for javac not registered; not settingrnative /usr/lib/jvm/java-8-openjdk-amd64/jre/bin/javac
3.623 installazione.sh: line 14: $'\r': command not found
3.629 Unrecognized option: -version
3.629 Error: Could not create the Java Virtual Machine.
3.629 Error: A fatal exception has occurred. Program will exit.
3.782 javac: invalid flag: -version
3.782 Usage: javac <options> <source files>
3.782 use -help for a list of possible options
3.786 installazione.sh: line 17: $'\r': command not found
3.787 sleep: invalid time interval '2\r'
3.787 Try 'sleep --help' for more information.
3.788 installazione.sh: line 19: $'\r': command not found
3.788 installazione.sh: line 20: $'\r': command not found
3.789 installazione.sh: line 21: wget: command not found
3.789 installazione.sh: line 22: $'\r': command not found
3.790 installazione.sh: line 23: wget: command not found
3.790 installazione.sh: line 24: $'\r': command not found
3.791 Error: Unable to access jarfile evosuite-1.0.6.jar
3.792 installazione.sh: line 26: $'\r': command not found
3.793 sleep: invalid time interval '2\r'
3.793 Try 'sleep --help' for more information.
3.794 installazione.sh: line 28: $'\r': command not found
3.795 installazione.sh: line 30: $'\r': command not found
3.796 Error: Unable to access jarfile /app/evosuite-1.0.6.jar
------
failed to solve: process "/bin/sh -c bash installazione.sh" did not complete successfully: exit code: 1
"Error in T1-G11 installation during docker compose up"
"Error detected on function 1, exiting script."
```
dovuto al modo in cui Windows e Linux memorizzano i caratteri di fine riga (CRLF Windows mentre LF Linux); per arginare definitivamente il problema è stato aggiunto i file: **.gitattributes**:
```bash
# Force all sh file to use lf eol  
*.sh eol=lf
```
Se il problema dovesse persistere, si rimanda alla lettura della sezioni successive dove viene illustrato una modalità risolutiva più artigianale.

9. il path di compilazione (task T7) è stato modificato in maniera tale che ogni volta che la classe Config (in **Config.java**) viene instanziata, in corrispondenza della chiamata all'API /compile-and-codecoverage, un nuovo timestamp viene generato ed usato per creare dei path unici; al termine della procedura, tali cartelle temporanee verranno rimosse.
e.g.:
```java
//(NEW) pathCompiler = usr_path/ClientProject/timestamp/

//(NEW) testingClassPath = usr_path/ClientProject/timestamp/src/test/java/ClientProject/
   
// (NEW) underTestClassPath = usr_path/ClientProject/timestamp/src/main/java/ClientProject/
    
// (NEW) coverageFolder = usr_path/ClientProject/timestamp/target/site/jacoco/jacoco.xml
```
# PREREQUISITI
1. Assicurarsi di aver correttamente clonato sul proprio workspace, il repository A13 di riferimento; si consiglia, innanzitutto, di 
scaricare, al seguente indirizzo https://git-scm.com/downloads, ed installare, sul proprio computer, Git (per una guida completa all'installazione fare riferimento al seguente indirizzo: https://github.com/git-guides/install-git).
Una volta aver completato l'installazione, sarà possibile clonare i repository GitHub sulla propria macchina:
<pre>
1) Aprire Git Bash (shell di Git)
2) Posizionarsi nella cartella all'interno della quale si desidera aggiungere il repository clonato
3) Andare alla pagina del repository che si intende clonare -> https://github.com/Testing-Game-SAD-2023/A13
4) Cliccare sul bottone: "< > Code" e copiare l' URL
5) Utilizzare il comando:
     git clone URL COPIATO
6) Premere invio
</pre>
![How to clone repository by URL](https://github.com/Testing-Game-SAD-2023/A13/blob/main/Immagini_installazione/git_clone.png)

2. Assicurarsi di avere Java e Maven installati correttamente sulla propria macchina e di aver settato le variabili d'ambiente.

# SPECIFICHE E VERSIONI UTILIZZATE
```
Specifiche dispositivo
----------------------
Processore      Intel(R) Core(TM) i5-8265U CPU @ 1.60GHz   1.80 GHz
RAM installata  8,00 GB (7,88 GB utilizzabile)
----------------------
Specifiche Windows
----------------------
Edizione    Windows 10 Home
Versione    10.0.19045.4291
----------------------
Docker Desktop
----------------------
Versione    4.29.0
----------------------
Windows Subsystem for Linux
----------------------
Versione WSL: 2.0.14.0
Versione kernel: 5.15.133.1-1
Versione WSLg: 1.0.59
Versione MSRDC: 1.2.4677
Versione Direct3D: 1.611.1-81528511
Versione DXCore: 10.0.25131.1002-220531-1700.rs-onecore-base2-hyp
-----------------------
Maven
-----------------------
Versione    3.9.6
-----------------------   
```

# GUIDA ALL'INSTALLAZIONE

## PASSO 1
Scaricare e installare Docker Desktop per il proprio sistema operativo: se già installato correttamente sulla macchina passare al Passo 2.

NOTA: sebbene il Windows Subsystem for Linux wsl2, una funzionalità che consente di eseguire un ambiente Linux all'interno del sistema operativo Windows garantendo la compatibilità tra Docker Desktop e Windows, normalmente venga installato e aggiornato durante l'installazione di Docker Desktop, vi sono casi in cui questo step non venga effettuato correttamente in maniera automatica (all'apertura di Docker è presente un messaggio di errore "WSL Error"), bisogna quindi installare manualmente wsl tramite i seguenti step:
<pre>
a) avviare il prompt dei comandi
b) digitare wsl --install e premere invio
c) digitare wsl --update e premere invio
d) riavviare la macchina
</pre>
1) Procedere all'installazione di Docker Desktop: https://www.docker.com/products/docker-desktop/
2) All'avvio di Docker Desktop nella sezione Settings -> General controllare che sia spuntata l'opzione "Use the WSL 2 based engine"

## PASSO 1B
Nel caso non sia la prima installazione, per la disinstallazione utilizzare "uninstaller.bat" mentre si ha in esecuzione Docker, in questo modo si elimina qualunque file presente su Docker.

## PASSO 2
Avviare lo script "installer.bat" se si sta usando una distribuzione Windows oppure "installermac.sh" nel caso si utilizzi macOS o una distro di Linux.
Per MacOS - eseguire nella cartella dove è presente il file ”installermac.sh” il comando "chmod +x installermac.sh" per renderlo eseguibile, e poi "./installermac.sh" per eseguirlo.
Tali script dovranno essere avviati unicamnete con Docker in esecuzione, altrimenti l'installazione non partirà. Saranno effettuate le seguenti operazioni:
1) Creazione della rete "global-network" comune a tutti i container.
2) Creazione del volume "VolumeT9" comune ai Task 1 e 9 e del volume "VolumeT8" comune ai Task 1 e 8.
3) Creazione dei singoli container in Docker desktop.
4) Esecuzione dei file di installazione nei container del task T8 e T7
5) Avvio dei container
6) Comandi di inizializzazione del database del task T1
   
NOTA: il container relativo al Task 9 ("Progetto-SAD-G19-master") si sospenderà autonomamente dopo l'avvio. Esso viene utilizzato solo per "popolare" il volume "VolumeT9" condiviso con il Task 1.

# ATTENZIONE
Nel corso dell'installazione dello script: "installer.bat", è possibile incappare nell'errore:
<pre>
E: invalid operation update
</pre>
dovuto al modo in cui Windows e Linux memorizzano i caratteri di fine riga (CRLF Windows mentre LF Linux); per risolvere il problema è indispensabile switchare dalla codifica CRLF -> a quella LF.
Un modo rapido prevede di effettuare la modifica manualmente utilizzando come editor di testo Notepad++:
<pre>
Edit => EOL Conversion => Unix(LF) dopodichè salvare
</pre>
![Errore in fase di installazione](https://github.com/Testing-Game-SAD-2023/A13/blob/main/Immagini_installazione/errore.png)
(https://stackoverflow.com/questions/55258430/e-invalid-operation-update-error-while-running-shell-scripts-in-wsl)
# Passi opzionali per esporre l'applicazione su un indirizzo pubblico
__NB: Ogni lettera rappresenta una soluzione diversa__

# A: Installazione NGROK
Nel caso sia stato già installato basterà avviare il container e collegarsi all'indirizzo fornito in precedenza

 __PASSO A.1__:
Registrazione presso il sito: https://ngrok.com/

 __PASSO A.2__:
Accesso alla Dashboard: https://dashboard.ngrok.com/get-started

 __PASSO A.3__:
Scelta dell'agente (si consiglia Docker).

 __PASSO A.4__:
Inserire il comando nel prompt sostituendo il __*token*__ e il __*dominio statico*__ fornito:

    docker run --net=host -it -e NGROK_AUTHTOKEN=TOKEN ngrok/ngrok:latest http --domain= DOMINIO 80
![How to set ngrok](https://github.com/Testing-Game-SAD-2023/A13/blob/main/Immagini_installazione/ngrok.png)
A questo punto si avrà l'indirizzo pubblico come risposta nel prompt dei comandi.

*__NB__*: il comando può essere copiato direttamente dalla dashboard di Ngrok, si consiglia di utilizzare il dominio di tipo statico

## Video installazione 


https://github.com/Testing-Game-SAD-2023/A10-2024/assets/148564450/0292d839-321f-4172-a044-25648d291753


# B: Esposizione localhost tramite Pinggy

__ATTENZIONE__: 
Si consiglia di non diffondere il link generato da Pinggy poiché all'interno è presente il proprio indirizzo pubblic. 
UTILE SOLAMENTE IN CASO DI TEST IN AMBIENTE CONTROLLATO!

 __PASSO B.1__:
Mentre Docker è in esecuzione digitare il seguente comando sul prompt dei comandi:

    ssh -p 443 -R0:localhost:80 -L4300:localhost:4300 a.pinggy.io

 __PASSO B.2__: 
Per la richiesta della password, dare una stringa vuota.
Infine compariranno a schermo l'indirizzo pubblico.

# Guida alle modifiche al codice

Se è necessario modificare il codice dell'applicazione, seguire attentamente i seguenti passaggi:

1. Recarsi nell'applicazione Docker.
2. Aprire la sezione relativa ai containers.
3. Selezionare tutti i containers relativi ai file modificati.
4. Effettuare la delete di tali containers.
5. Aprire la sezione images.
6. Selezionare le immagini relative ai file modificati.
7. Effettuare l'eliminazione di tali immagini.
8. Recarsi sull'IDE utilizzato ed aprire il terminale integrato della cartella in cui è presente il file `pom.xml` relativo ai file modificati.
9. Eseguire il comando `mvn clean package`.
10. Fare clic sul tasto destro sul file `docker-compose.yml` e selezionare "compose up".
11. Riavviare i container.

---

# Problematiche di utilizzo

Di seguito sono elencate alcune delle problematiche riscontrate durante l'utilizzo dell'applicazione al fine di agevolarne l'utilizzo per i gruppi successivi.

## Cache del browser

Disattivando la cache del browser, è possibile garantire che ogni modifica apportata al codice sorgente o alle risorse dell'applicazione sia immediatamente visualizzata nel browser. La cache del browser può causare problemi di compatibilità e disattivarla semplifica il processo di debug, consentendo agli sviluppatori di identificare e risolvere rapidamente i bug senza dover preoccuparsi di eventuali caching persistenti che potrebbero mascherare il problema.

## Versione Docker

Durante lo sviluppo dell'applicazione, è emersa una problematica legata alla versione di Docker. È stato osservato che nelle versioni più recenti di Docker, dopo una serie di disinstallazioni e reinstallazioni dell'applicazione, potrebbero verificarsi problemi durante il download quando si avvia il file `installer.bat`. Nel caso in cui si riscontrino errori come quello mostrato nella figura seguente, potrebbe essere necessario disinstallare Docker e utilizzare una versione non successiva alla `4.25.1` evitando così errori nella costruzione dei container.

![Errore Installer.bat](Media/Foto/ErrorInstaller.png)


## Porte già in uso

Durante l'avvio dell'applicazione, potrebbe verificarsi un problema in cui alcuni container non si avviano correttamente, mostrando un messaggio di errore relativo alla porta già in uso. Un esempio comune potrebbe riguardare il container T4, poiché la porta 3000 è comunemente utilizzata dal processo `MySQL80`. In questo caso, è necessario aprire la schermata dei servizi attivi su Windows per individuare e terminare il processo che sta attualmente utilizzando la porta in questione. In alternativa, è possibile risolvere questo problema modificando la porta del container in questione per evitare conflitti.

## 502 Bad Gateway

Occasionalmente, anche dopo che tutti i container sono stati avviati con successo, la pagina web potrebbe visualizzare un messaggio di errore del tipo `502 Bad Gateway`. Una possibile ragione dietro questo tipo di errore potrebbe essere correlata alla rapidità con cui si tenta di accedere all'applicazione web subito dopo il riavvio dei container. In questi casi, la piattaforma potrebbe richiedere del tempo per completare il processo di avvio e stabilire la connessione corretta tra i vari componenti dell'applicazione. Attendere alcuni istanti prima di tentare di accedere all'applicazione può spesso risolvere il problema. Tuttavia, se l'errore persiste nonostante l'attesa, è consigliabile effettuare il riavvio di Docker.

# Video Dimostrativi

Di seguito riportiamo i video dimostrativi delle due modalità di gioco attualmente disponibili.

## Funzionamento Modalità "Sfida un Robot"

https://github.com/SimoneRinaldi02/A7-2024-ver2/assets/115701124/89f519e5-796b-4883-8826-681a0980f16e

## Funzionamento Modalità "Allenamento"

https://github.com/SimoneRinaldi02/A7-2024-ver2/assets/115701124/f99ea16d-caf9-451f-9604-99f5ff2b0960




# C: Installazione Server Esterno 



## PASSO C.1 
Il primo passo è quello di registrarsi presso il sito: https://www.kamatera.com/

## PASSO C.2
Creare e configurare il server con sistema linux, impostando le specifiche tecniche, si consigliano: 
CPU:2 
RAM: 8GB
Memoria: 40GB

## PASSO C.3
Scaricare puTTY e collegarsi tramite protocollo SSH sul IP fornito dal Server.

## PASSO C.4 
All'interno del terminale di puTTY, eseguire i seguenti comandi per clonare la cartella di github, installare docker e l'applicazione web su docker: 

    sudo apt update
    sudo apt install git
    git clone https://github.com/Testing-Game-SAD-2023/A10-2024
    cd ./A10-2024
    chmod +x installer_docker.sh
    chmod +x installer-linux-server.sh
    chmod +x uninstaller.sh
    ./installer_docker.sh
    ./installer-linux-server.sh

## PASSO C.5
L' installazione a questo punto è completata, da linea di comando è possibile vedere se tutti i container sono in stato di RUN con il seguente comando: 

    docker container ls -a
    
E farli ripartire tutti con:

    docker restart $(docker ps -q -a)

## PASSO C.6
Accedendo al indirizzo IP tramite browser sarà possibile utilizzare l'applicazione web tramite indirizzo pubblico

# MODALITA' DI UTILIZZO

## Social login tramite Facebook
Come prerequisito, è richiesto: di essere in possesso di un account Facebook funzionante. Dalla pagina di login dei players
(assicurarsi di essersi collegati presso un indirizzo https sicuro e di aver aperto la pagina in incognito),
cliccare sul pulsante: "Accedi con Facebook", dopodichè inserire le proprie credenziali (e-mail e password); una volta aver
completato la procedura si verrà automaticamente re-indirizzati all'arena privata di gioco.
### Video
https://github.com/Testing-Game-SAD-2023/A13/blob/main/Video_A13/Social_login.mp4

## Configurazione social login
Ovviamente, non avendo un dominio unico, bisognerà che ogni utilizzatore di questa versione dell'applicazione, configuri appropriatamente l'SDK di JavaScript per Facebook, in particolare:
1) dovrà essere in possesso di un account Facebook da sviluppatore (https://developers.facebook.com/) e creare una propria "app", la quale sarà dotata di un certo ID
![creazione app](https://github.com/Testing-Game-SAD-2023/A13/blob/main/Immagini_installazione/facebook_1.png)

2) configurare l'SDK ed inserire negli appositi campi il proprio indirizzo sicuro restituito da ngrok
![configurazione SDK](https://github.com/Testing-Game-SAD-2023/A13/blob/main/Immagini_installazione/facebook_2.png)

3) modificare opportunamente il file: "login.html" appartenenente alla cartella T23-G1 
<pre>
    percorso relativo: T23-G1\src\main\resources\templates\login.html 
</pre>

__3.1__ alla riga 61, modificare URL inserendo il proprio indirizzo lasciando il resto invariato
![aggiornamento login.html 1](https://github.com/Testing-Game-SAD-2023/A13/blob/main/Immagini_installazione/facebook_3.png)

__3.2__ modificare la funzione di inizializzazione dell'SDK inserendo l'ID della propria app (riga 119)
![aggiornamento login.html 2](https://github.com/Testing-Game-SAD-2023/A13/blob/main/Immagini_installazione/facebook_4.png)

__3.3__ modificare lo script alla riga 169 inserendo l'ID della propria app e lasciando il resto invariato (permette di caricare asincronamente il JSSDK)
![aggiornamento login.html 3](https://github.com/Testing-Game-SAD-2023/A13/blob/main/Immagini_installazione/facebook_5.png)

4) modificare opportunamente il file: "Controller.java" appartenente alla cartella T23-G1
<pre>
    percorso relativo: T23-G1\src\main\java\com\example\db_setup\Controller.java 
</pre>

__4.1__ alla riga 83 inserire il proprio "App Token" individuabile alla seguente pagina: https://developers.facebook.com/tools/accesstoken
![aggiornamento Controller.java 1](https://github.com/Testing-Game-SAD-2023/A13/blob/main/Immagini_installazione/facebook_6.png)
![aggiornamento Controller.java 2](https://github.com/Testing-Game-SAD-2023/A13/blob/main/Immagini_installazione/facebook_7.png)

5) salvare tutte le modifiche e compilare

## Invito di nuovi amministratori
Come prerequisito, bisogna essere correttamente registrati come amministratori. La preocedura di invito si articola nei seguenti step:
<pre>
1) dalla pagina di login degli admin (/loginAdmin), autenticarsi inserendo le proprie credenziali definite in fase di registrazione
2) una volta aver completato la procedura, si verrà re-indirizzati all'area riservata degli admins (/home_adm); cliccare sul bottone: "Invita"
3) si verrà re-indirizzati all'apposita pagina di invito (/invite_admins), inserire nel form la mail dell'utente che si desidera invitare
4) l'utente invitato, riceverà, all'indirizzo di posta specificato, un token di invito che dovrà copiare (fare molta attenzione a non copiare involontariamente gli spazi) ed inserire all'interno dell'apposita pagina di login (/login_with_invitation) completando i campi del form
5) una volta completata la procedura di registrazione sarà possibile loggarsi tranquillamente all'interno dell'applicazione (/loginAdmin)
</pre>

### Video
https://github.com/Testing-Game-SAD-2023/A13/blob/main/Video_A13/Invito_admins.mp4
