# Gruppo A10-2024
Componenti:
- Vincenzo D'Angelo - M63/1595
- Giorgio Di Costanzo - M63/1579
- Aurelio Salvati - M63/1619
  
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

L'intera applicazione è adesso pienamente configurata e raggiungibile sulla porta :80. Per una guida all'installazione e all'utilizzo più completa consultare la documentazione al capitolo 7.
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
Inserire il comando sostituendo il __*token*__ e il __*dominio statico*__ fornito:

    docker run --net=host -it -e NGROK_AUTHTOKEN=TOKEN ngrok/ngrok:latest http --domain= DOMINIO 80
    
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

## Video installazione 

https://github.com/Testing-Game-SAD-2023/A10-2024/assets/148564450/b96c6831-d134-44af-aae7-5cb78e503090






# VIDEO DIMOSTRAZIONE
## Admin



https://github.com/Testing-Game-SAD-2023/A10-2024/assets/148564450/955ead78-04ed-4763-8777-4ebb13bfdd1a



## Player



https://github.com/Testing-Game-SAD-2023/A10-2024/assets/148564450/d92328cd-1863-48f4-abe1-a94c04963cf7



## Mobile Device



https://github.com/Testing-Game-SAD-2023/A10-2024/assets/24376231/18f1cbd2-3671-4012-9911-584ca4215690
