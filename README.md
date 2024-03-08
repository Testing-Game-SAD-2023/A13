# Gruppo A13-2024
Componente:
- Caterina Maria Accetto - M63/1117

Il lavoro che andremo ad esporre all'interno della documentazione, muove i suoi primi passi a partire dal progetto: A10-2024,
e vuole essere solamente una versione migliorativa dei requisiti sviluppati in corrispondenza dei serivizi T1 e T23, 
di conseguenza rimangono invariate le modalità di installazione così come l'architettura complessiva dell'applicazione, diagrammi
di deployment, dei componenti, ...

### REQUISITI SVILUPPATI
1)Il sistema, per ogni sessione di autenticazione correttamente effettuata, deve assegnare all'amministratore un token di autenticazione
2)Il sistema deve implementare un meccanismo che permetta di distinguere gli amministratori, aventi particolari privilegi, dai semplici giocatori
3)Il sistema deve consentire ai soli utenti aventi specifiche caratteristiche di potersi registrare inserendo: nome, cognome, username, e-mail e password
4)Il sistema deve prevedere la possibilità, in fase di registrazione, di ammettere utenti con doppi nomi o, più in generale, con più nomi separati da uno spazio, ad esempio: "Anna Maria"
5)Il sistema deve prevedere la possibilità, in fase di registrazione,di ammettere utenti con cognomi costituiti da più parole separate da spazi oppure da apostrofi, come nel caso:"Degl'Antoni" 
6)Il sistema deve, accettare, in fase di registrazione, solamente password lunghe tra gli 8 e i 16 caratteri e che contengano:almeno una lettera maiuscola, una minuscola, un numero ed un carattere speciale
7)Il sistema, in fase di registrazione, deve implementare una apposita misura di sicurezza che consenta di proteggere le password archiviate all'interno del database le quali dovranno venir crittate mediante un algoritmo di hashing 
8)Il sistema deve controllare che tutti i campi, negli appositi form di login e di registrazione, vengano compilati e che siano validi,in caso di errore, deve essere visualizzato a video un messaggio di errore
9)Il sistema deve permettere all'amministratore registrato di effettuare il logout 
10)Il sistema deve consentire all'amministratore correttamente registrato ed autenticato, di visualizzare, in una apposita sezione dell'area riservata, la lista di tutti gli admin del sistema
11)Il sistema deve permettere all'amministratore correttamente registrato, di impostare una nuova password nel caso in cui avesse dimenticato la propria
12)Il sistema deve consentire a tutti gli amministratori correttamente registrati ed autenticati, di invitarne di nuovi, e che non necessariamente rispettano i vincoli richiesti, tramite l'invio, per posta elettronica, di un apposito token
13)Il sistema deve permettere agli utenti che hanno ricevuto il token di invito per posta, di potersi registrare nel sistema come admin
14)Il sistema deve implementare un meccanismo che permetta di distinguere gli amministratori del dominio da quelli che sono stati invitati
15)Il sistema, in caso di avvenuta registrazione, deve mostrare a video un feedback, dopodichè, indirizzare l'utente alla pagina di login
16)Il sistema deve consentire agli utenti di decidere se registrarsi tramite il classico form (e-mail + password) oppure effettuare il social login tramite Facebook e completare in questo modo la procedura di registrazione
17)l sistema deve essere provvisto di un entry-point, una pagina dove poter smistare gli utenti dell' applicazione in base al ruolo ricoperto ed indirizzarli verso le sezioni dedicate
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

