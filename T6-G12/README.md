# GUIDA ALL'INSTALLAZIONE (SENZA DOCKER)
## 1)Scaricare uno tra i due software proposti per il funzionamento di un WebServer:
[MAMP](https://www.mamp.info/en/downloads/)
[XAMPP](https://www.apachefriends.org/it/download.html)
## 2)Scaricare l'intera cartella GitHub o le seguenti cartelle:
    Icone
    T6
## 3)Scaricare e scompattare l'archivio " codemirror.zip "  
## 4)Scaricare il file " index.html "
## 5)Dopo aver installato MAMP o XAMPP compiere i seguenti passi:
MAMP: Applications/MAMP/htdocs --> copiare all'interno di questa cartella i seguenti file:
  1. Icone
  2. T6
  3. Codemirror
  4. index.html

XAMPP: Directory_in_cui_è_installato_XAMPP/XAMPP/htdocs --> copiare all'interno di questa cartella i seguenti file:
  1. Icone
  2. T6
  3. Codemirror
  4. index.html
 ## 6)Avviare Server Apache tramite MAMP o XAMPP
 ## 7)Navigare tramite un browser ai seguenti indirizzi:
    MAMP: localhost/8080
    XAMPP: localhost/8080
## 8)Dopo aver scaricato la cartella T6 eseguire il file MainController.java (CONTROLLER SPRING) il cui percorso è mostrato di seguito:
    MAMP/htdocs/T6/src/main/java/com/example/T6/MainController.java
    XAMPP/htdocs/T6/src/main/java/com/example/T6/MainController.java
  Che consentirà l'avvio del Framework SPRING.
## ---------------------------------------------------------------
# GUIDA ALL'INSTALLAZIONE (IN DOCKER)
## 1)Scaricare Docker Desktop:
[Docker Desktop](https://www.docker.com/products/docker-desktop/)
## 2)Scarica Java JDK 17:
[JDK_17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
## 3)Installare Maven
## 4)Scaricare uno tra i due software proposti per il funzionamento di un WebServer:
[MAMP](https://www.mamp.info/en/downloads/)
[XAMPP](https://www.apachefriends.org/it/download.html)
## 5)Scaricare l'intera cartella GitHub o i seguenti file:
    Icone
    T6
    Dockerfile
## 6)Scaricare e scompattare l'archivio " codemirror.zip "  
## 7)Scaricare il file " index.html "
## 8)Creare una Directory con i file scaricati (esempio: editor)
## 9)Dopo aver installato MAMP o XAMPP compiere i seguenti passi:
MAMP: Applications/MAMP/htdocs --> copiare all'interno di questa cartella i seguenti file:
  1. Icone
  2. T6
  3. Codemirror
  4. index.html
  5. Dockerfile

XAMPP: Directory_in_cui_è_installato_XAMPP/XAMPP/htdocs --> copiare all'interno di questa cartella i seguenti file:
  1. Icone
  2. T6
  3. Codemirror
  4. index.html
  5. Dockerfile
## 10)Avviare Server Apache tramite MAMP o XAMPP sul porto 8080
## 11)Avviare l'applicazione Docker Desktop
## 12)Avviare il Prompt dei Comandi
## 13)Posizionarsi nella directory in cui è presente il file pom.xml e digitare il seguente comando (il file pom.xml si trova nella cartella T6 della directory creata, esempio: editor/T6/pom.xml:
  1. mvn clean package
## 14)Posizionarsi nella directory creata (esempio: editor) e digitare i seguenti comandi:
  1. docker build -t editor .
  2. docker run -p 80:80 editor
## Su Docker verrà creata l'immagine ed il container relativi al progetto.
# NB: Per cambiare numero di porta fare come segue:
      T6/src/main/resources/application.properties
      Una volta all'interno del file inserirela seguente riga di codice:server.port="NUMERO_PORTA_DESIDERATA"
  
  
  
