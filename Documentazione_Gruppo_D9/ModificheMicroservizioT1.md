# Modifiche al microservizio T1

---
Di seguito sono presentate tutte le modifiche al codice effettuate dal gruppo D9 per la task R2. 


## File di configurazione

| File | Descrizione | Creato o Modificato |
|------|-------------|---------------------|
| .env | Sostituiti username e password MONGODB_ROOT con equivalenti per MYSQL (MYSQL_ROOT_PASSWORD, MYSQL_DATABASE, MYSQL_USER, MYSQL_PASSWORD) | Modificato |
| docker-compose.yml | Sono state sostituite le variabili di ambiente per la connessione MongoDB (MONGODB_ROOT_PASSWORD e MONGODB_ROOT_USERNAME) con quelle per Spring Data e MySQL (SPRING_DATASOURCE_URL, SPRING_DATASOURCE_USERNAME, SPRING_DATASOURCE_PASSWORD).<br><br>Modificato il campo depends_on da “mongo_db” a “mysql_db”.<br><br>Sostituito il servizio “mongo_db” con “mysql_db”, con la configurazione necessaria (variabili d’ambiente per password e user, porta esposta 3306, volume “/var/lib/mysql”) | Modificato |
| pom.xml | aggiornamento di Spring Boot da 2.7.0 a 3.2.0 per poter effettuare la migrazione Jakarta e conseguente aggiornamento delle versioni di altre dipendenze spring<br><br>sostituzione di spring-boot-starter-data-mongodb in spring-boot-starter-data-jpa per la migrazione a jakarta<br><br>aggiunto mysql-connetor-java per la connessione a mysql<br><br>aggiunto spring-boot-starter-validation per abilitare le operazioni di validazione dei vari campi<br><br>aggiunta la dipendenza a MapStruct per generare i Mapper tra DTO e Model in maniera automatizzata<br><br>aggiunta la dipendenza a Lombok per generare getter, setter, costruttori e altro in maniera automatizzata<br><br>aggiunto h2 come database in-memory per i test<br><br>aggiunto spring-boot-starter-test e mockito-core per i test<br><br>aggiunto Apache Tika per validare i file ricevuti come immagini | Modificato |
| application.properties | Aggiunto l’import del file .env.<br><br>Sostituite le configurazioni di mongodb con quelle di mysql, jpa e hibernate.<br><br>Creata la variabile images.storage-path per il path delle immagini nel volume T0 | Modificato |
| message_it.properties<br>message_en.properties<br>message.properties | Aggiunte nuove coppie chiave-valore per consentire internazionalizzazione dell’applicazione riguardo alle nuove funzionalità (suggerimenti e linee guida) e ai messaggi di errore | Modificato |

---

## Frontend

| File | Descrizione | Creato o Modificato |
|------|-------------|---------------------|
| opponents_main.js | Aggiunta la sezione per la visualizzazione delle linee guida nella navigation bar. Small fix per mostrare l’errore internazionale. Modificati i nomi difficoltà degli opponents da Beginner/Intermediate/Advanced a Easy/Medium/Hard per essere coerente con i nomi presenti nelle classi. | Modificato |
| api.js | Aggiunte le funzioni che si appoggiano al backend per i servizi legati ai suggerimenti e alle linee guida:<br><br>callDeleteSuggestion<br>callDeleteSuggestionImage<br>callUploadSuggestions<br>callUploadSuggestionImage<br>callGetSuggestions<br>callDeleteGuideline<br>callDeleteGuidelineImage<br>callUploadGuidelines<br>callUploadGuidelineImage<br>callGetGuidelines<br>callDownloadSuggestions<br>callDownloadGuidelines | Modificato |
| endpoints.js | Aggiunti gli endpoints che rappresentano i path del backend relative alle nuove funzionalità aggiunte in api.js:<br><br>UPLOAD_GUIDELINES<br>UPLOAD_GUIDELINE_IMAGE<br>DOWNLOAD_GUIDELINE<br>GET_GUIDELINES<br>DELETE_GUIDELINE<br>DELETE_GUIDELINE_IMAGE<br>UPLOAD_SUGGESTIONS<br>UPLOAD_SUGGESTION_IMAGE<br>DOWNLOAD_SUGGESTION<br>GET_SUGGESTIONS<br>DELETE_SUGGESTION<br>DELETE_SUGGESTION_IMAGE<br><br>Aggiunta costante IMAGE_BASE_URL corrispondente al path utilizzato per accedere alle immagini nel VolumeT0 (opportunamente mappata in ResourceConfig.java nel backend)<br><br>Modificati i nomi difficoltà degli opponents da Beginner/Intermediate/Advanced a Easy/Medium/Hard per essere coerente con i nomi presenti nelle classi. | Modificato |
| opponents_edit.html | Modificata senza particolari aggiunte | Modificato |
| opponents_main.html | Aggiunta la sezione sotto ogni classe per mostrare la lista dei suggerimenti, insieme a:<br><br>- Bottone per scaricare il file json contenente i suggerimenti di una classe<br>- Bottone per effettuare l’upload dei suggerimenti ad una classe<br>- Bottone per effettuare la cancellazione di un singolo suggerimento<br>- Bottone per effettuare l’upload dell’immagine di un singolo suggerimento<br>- Riquadro `<img>` per mostrare l’eventuale immagine associata ad un suggerimento, con annesso bottone per cancellare<br>- Aggiunto il “badge” per la visualizzazione del livello di aiuto di un suggerimento (LOW, MEDIUM, HIGH)<br><br>Refactoring di codice già presente | Modificato |
| opponents_upload.html | Risolto un bug che non permetteva di mostrare correttamente i nomi delle difficoltà degli opponents in lingua inglese, con la sostituzione dei nomi dell’enum da Beginner/Intermediate/Advanced a EASY/MEDIUM/HARD. | Modificato |
| guidelines_main.html | Nuova vista per mostrare la lista delle linee guida, insieme a:<br><br>- Bottone per scaricare il file json contenente le linee guida<br>- Bottone per effettuare l’upload delle linee guida<br>- Bottone per effettuare la cancellazione di una singola linea guida<br>- Bottone per effettuare l’upload dell’immagine di una singola linea guida<br>- Riquadro `<img>` per mostrare l’eventuale immagine associata ad una linea guida, con annesso bottone per cancellare | Creato |
| guidelines.js | Aggiunte le funzioni:<br><br>fetchAndDisplayGuidelines per ottenere e visualizzare la lista delle linee guida<br>deleteGuideline per effettuare la cancellazione di una linea guida<br>handleGuidelinesUpload per effettuare l’upload delle linee guida<br>uploadGuidelineImage per effettuare l’upload dell’immagine di una linea guida<br>downloadGuidelines per effettuare il download del file json contenente le linee guida<br>deleteGuidelineImage per effettuare la cancellazione delle immagini di una linea guida | Creato |
| suggestions.js | Aggiunte le funzioni:<br><br>fetchAndDisplaySuggestions per ottenere e visualizzare la lista dei suggerimenti<br>deleteSuggestion per effettuare la cancellazione di un suggerimento<br>handleSuggestionUpload per effettuare l’upload dei suggerimenti<br>uploadSuggestionImage per effettuare l’upload dell’immagine di un suggerimento<br>downloadSuggestions per effettuare il download del file json contenente i suggerimenti di una classe<br>deleteSuggestionImage per effettuare la cancellazione delle immagini di un suggerimento | Creato |

---

## Backend

Le modifiche del backend sono state raggruppate per package.

---

### Package api

| File | Descrizione | Creato o Modificato |
|------|-------------|---------------------|
| ApiGatewayClient.java | Modificati gli import relativi ai dto e javax aggiornato a jakarta per adeguarsi alla migrazione a Spring Boot 3 / Jakarta EE.<br><br>Al fine di non propagare il jwt token dal Controller al Service fino all’ApiGatewayClient (per le richieste da mandare all’esterno, per esempio verso T23), il token jwt viene ora recuperato da JwtRequestContex laddove necessario, centralizzando in questo modo la gestione del token. Quindi è stato reso necessario un refactoring riguardo la gestione del jwt, il quale non viene più passato come parametro esplicito ai metodi di ApiGatewayClient, ma viene:<br><br>- recuperato automaticamente dal metodo JwtRequstContext.getJwtToken()<br>- inserito nell’header HTTP come COOKIE prima di ogni chiamata<br><br>In caso di token mancante viene sollevata un’eccezione.<br><br>Small fix di nomi di alcune variabili per rendere il codice più leggibile e meno ambiguo (esempio: classUT rinominato in className, perché non è un’istanza di ClassUT ma solo il nome).<br><br>Sostituita l’autenticazione Bearer con quella tramite cookie JWT, come richiesto da T23. | Modificato |

---

### Package config

| File | Descrizione | Creato o Modificato |
|------|-------------|---------------------|
| ResourceConfig.java | È stata aggiunta questa classe per configurare Spring MVC in modo da esporre le immagini relative a suggerimenti e linee guida, presenti sul file system, come risorse http accessibili dal frontend. Per farlo:<br><br>- utilizza annotazione @Configuration, che indica a Spring che la classe è un elemento di configurazione del contesto applicativo<br>- implementa WebMvcConfigurer per permettere di personalizzare il comportamento di default d SpringMVC, di cui effettua l’override del metodo addResourceHandlers<br>- nel metodo @Override addResourceHandlers per registrare un “resource handler”, ovvero una regola che comunica a Spring di mappare tutte le richieste intercettate nel path di tipo “/t1/images/” al path location = “file:/VolumeT0/FolderTree/Images/”, corrispondente al percorso delle immagini nel file system presente nel volume T0. Ad esempio la richiesta “GET /t1/images/Calcolatrice_1.png” viene mappata in “/VolumeT0/FolderTree/Images/Calcolatrice_1.png”.<br><br>In particolare, il percorso del file system “/VolumeT0/FolderTree/Images/” viene prelevato dal file application.properties con l’annotazione di spring @Value. | Creato |

---

### Package validation

Questo package è stato aggiunto per gestire la logica di validazione personalizzata implementata, non direttamente fornita dalle anntoazioni di jakarta.validation. In particolare attualmente contiene la classe di validazione per gestire la sequenzialità degli ordini in una lista di SuggestionDTO e GuidelineDTO, insieme alla definizione dell’annotazione apposita.

| File | Descrizione | Creato o Modificato |
|------|-------------|---------------------|
| ValidOrder.java | Definisce l’annotazione @ValidOrder che può essere utilizzata su un qualunque parametro di una funzione o campo di una classe (indicata con @Target({ ElementType.PARAMETER, ElementType.FIELD })) per indicare a Spring di effettuare la validazione a runtime (@Retention(RetentionPolicy.RUNTIME)) specificata dalla classe OrderValidator (impostata con @Constraint(validatedBy = OrderValidator.class)). | Creato |
| OrderValidator.java | Implementa ConstraintValidator<ValidOrder, List<? extends GuidelineDTO>> per specificare la logica di validazione di un List<T> con T che può essere GuidelineDTO o una qualunque classe che la estende (in questo caso solo SuggestionDTO), in modo da presentare il campo “order”. In particolare, effettua l’override del metodo “isValid”, il quale restituisce true se la lista ricevuta supera la validazione, false altrimenti. Nel caso in esame è richiesto che, in corrispondenza del campo “order”, il primo elemento della lista presenti valore 1 e che gli elementi successivi presentino valori interi progressivi 2, 3, 4, …. Il presente validatore interpreta una lista null e una lista vuota come valide dal punto di vista dell’ordinamento. | Creato |

---

### Package util

Dalla classe Util.java sono stati estratti i metodi relativi alla classe Interaction, i quali sono stati spostati nell’apposito servizio InteractionService, al fine di migliorare la modularità.

---

### Package security

| File | Descrizione | Creato o Modificato |
|------|-------------|---------------------|
| AuthTokenFilter.java | Aggiunta una strategia di sincronizzazione dell’admin nel database locale a partire dal token jwt validato dal microservizio T23, in quanto l’admin è necessario per il database mysql perché associato a numerose classi tramite chiave esterna. | Modificato |
| FilterConfig.java | Ora il metodo authTokenFilterRegistration prende in input direttamente l’oggetto AuthTokenFilter anziché l’ApiGatewayClient. | Modificato |

---

### Package model

Questo package definisce le classi model che astraggono le tabelle del database. Hanno quindi subito una radicale modifica per poter effettuare la migrazione da MongoDB a jakarta JPA.

La maggior parte delle modifiche è comune a tutti i model. Per ogni model:

- sono stati sostituiti gli import relativi a mongodb con jakarta, in particolare sostituendo l’annotazione di mongodb @Document con quelle di jakarta @Entity e @Table (quest’ultima per specificare il nome della tabella corrispondente nel database)
- è stata utilizzata l’annotazione @Id per specificare il campo legato alla chiave primaria, eventualmente insieme @GeneratedValue per specificare la generazione automatica dell’id nei casi in cui la chiave primaria non sia una stringa.
- Sono state utilizzate le annotazioni necessarie per indicare le relazioni SQL nelle classi model:
  - @ManyToOne per indicare che un campo è legato ad una relazione molti ad uno (ovvero è un riferimento ad un oggetto della classe Model con cui è presente la relazione).
  - Utilizzata in congiunzione con @JoinColumn(name = “nomeFK”, referencesColumnName = “nomePKriferita”)  per specificare che il campo riferito è mappato nella tabella come chiave esterna ad un’altra tabella, permettendo di indicare il nome della colonna che costituisce la chiave esterna (nomeFK) e il nome della chiave primaria nella tabella riferita (nomePKriferita).
  - @OneToMany(mappedBy = “nomeCampo”) per indicare che un campo è legato ad una relazione uno a molti (ovvero è una lista di riferimenti a oggetti della classe Model con cui è presente la relazione), indicata con una associazione corrispondente 
  - @ManyToOne sul campo “nomeCampo” della classe Model associata. Eventualmente definisce il cascade type come REMOVE per indicare il trigger ON DELETE CASCADE sul campo del database (permettendo la rimozione automatica dal database di tutte le tuple delle tabelle che presentano una chiave esterna verso il campo in questione).
  -@ManyToMany per indicare che un campo è legato ad una relazione molti a molti (ovvero è una lista di riferimenti a oggetti della classe Model con cui è presente la relazione). Utilizzata in congiunzione con @JoinTable() per indicare la tabella associativa creata nel database per rappresentare la relazione molti a molti.
- Utilizzo delle annotazioni di lombok per generare automaticamente metodi getter, setter, costruttori senza parametri e con parametri, rimuovendo di conseguenza tutto il codice relativo ai metodi generati automaticamente da tali annotazioni.
- Utilizzo di @Column per indicare eventualmente il nome della colonna corrispondente al campo nella tabella (qualora sia diverso dal nome del campo) e anche per indicare ulteriori constraint sul campo come UNIQUE e NOT NULL.

| File | Descrizione | Creato o Modificato |
|------|-------------|---------------------|
| Achievement.java | Classe model associata alla tabella ACHIEVEMENTS | Modificato |
| Admin.java | Classe model associata alla tabella ADMINS | Modificato |
| Assignment.java | Classe model associata alla tabella ASSIGNMENTS | Modificato |
| Category.java | Classe model associata alla tabella CATEGORIES | Creato |
| ClassUT.java | Classe model associata alla tabella CLASSES_UT | Modificato |
| ClassUTScalata.java | Classe model associata alla tabella CLASSES_SCALATE. È stata creata come classe model che astrae la tabella associativa tra CLASSES_UT e SCALATE, in modo da poter specificare anche i campi dell’associazione (level e timeLimit). Quindi presenta due campi “ClassUT classUT” e “Scalata scalata” con annotazione @ManyToOne e @JoinColumn per configurare una relazione molti ad uno con le classi Model associate alle tabelle riferite. Utilizza l’annotazione @EmbeddedId per specificare che la chiave primaria è la classe @Embeddable ClassUtScalataId (necessaria per indicare una chiave primaria composta dalle due chiavi esterne) e quindi l’annotazione @MapsId sui rispettivi campi delle chiavi esterne classUT e scalata per associarli al campo dell’oggetto ClassUTScalataId corrispondente (rispettivamente “className” e “scalataName”). | Creato |
| ClassUTScalataId.java | È una classe @Embeddable che implementa Serializable, utilizzata da ClassUTScalata come chiave primaria composta. | Creato |
| Guideline.java | Classe model associata alla tabella GUIDELINES | Creato |
| Interaction.java | Classe model associata alla tabella INTERACTIONS | Modificato |
| InteractionType.java | Classe enum per definire le possibili tipologie di interazione (REPORT e LIKE), frutto di un refactoring che migliora la manutenibilità e modificabilità del sistema (in quanto prima era presenta un semplice intero 0 per report e 1 per like; quindi, richiedeva una conoscenza di questi “numeri magici”). | Creato |
| Operation.java | Classe model associata alla tabella OPERATIONS | Modificato |
| OperationType.java | Classe enum per definire le possibili tipologie di operazioni (UPLOAD, DELETE, UPDATE), frutto di un refactoring che migliora la manutenibilità e modificabilità del sistema (in quanto prima era presenta un semplice intero 0 per upload, 1 per delete, 2 per update; quindi, richiedeva una conoscenza di questi “numeri magici”). | Creato |
| Opponent.java | Classe model associata alla tabella OPPONENTS | Modificato |
| Scalata.java | Classe model associata alla tabella SCALATA | Modificato |
| Suggestion.java | Classe model associata alla tabella SUGGESTIONS | Creato |
| SuggestionLevel.java | Classe enum per definire le possibili tipologie di livelli di aiuto di un suggerimento | Creato |
| Team.java | Classe model associata alla tabella TEAMS. Data la non esistenza della classe model Student (in quanto il microservizio T1 si è utilizzato solo dagli admin, mentre la tabella degli Student è memorizzata nel database presente in T23), sono state utilizzate alcune annotazioni per specificare il comportamento del campo “List<String> sutdentIds” della classe Team, che dovrebbe in linea teorica rappresentare una lista di chiavi esterne alla tabella degli studenti. Non avendo a disposizione la classe Model Student, utilizza utilizza le seguenti annotazioni per specificare un comportamento di relazione uno a molti con i vari studenti senza avere a disposizione la tabella students:<br><br>@ElementCollection<br>@CollectionTable | Modificato |
| TeamAdmin.java | Classe associativa tra Team e Admin non necessaria perché generata attraverso le associazioni di jakarta. | Eliminato |

---

A seguito di un refactoring, sono stati spostati i package repository e dto in com.groom.manvsclass, mentre prima erano contenuti nel package com.groom.manvsclass.model.

---

### Package repository

Questo package definisce le interfacce che estendono JpaRepository<TModel, TId> al posto di MongoRepository, utilizzate dal layer Service per effettuare le operazioni CRUD sul database e anche per definire query personalizzate in linguaggio JPQL, utilizzando l’annotazione @Query. Questo package è stato estratto da com.groom.manvsclass.model e spostato in com.groom.manvsclass, in modo da rispecchiare il diagramma dei package.

| File | Descrizione | Creato o Modificato |
|------|-------------|---------------------|
| AchievementRepository.java | Interfaccia Repository associata alla classe Achievement. | Creato |
| AdminRepository.java | Interfaccia Repository associata alla classe Admin. | Modificato |
| AssignmentRepository.java | Interfaccia Repository associata alla classe Assignment. | Modificato |
| ClassUTRepository.java | Interfaccia Repository associata alla classe ClassUT. Rinominato da “ClassRepository.java” per chiarezza | Modificato |
| GuidelineRepository.java | Interfaccia Repository associata alla classe Guideline. | Creato |
| InteractionRepository.java | Interfaccia Repository associata alla classe Interaction. | Modificato |
| MongoRepository.java | Classe vuota non utilizzata. | Eliminato |
| OperationRepository.java | Interfaccia Repository associata alla classe Operation. | Modificato |
| OpponentRepository.java | Interfaccia Repository associata alla classe Opponent. | Modificato |
| OpponentRepositoryImpl.java | Classe eliminata, che definita query personalizzate effettuate in MongoDB per gli Opponent. | Eliminato |
| ScalataRepository.java | Interfaccia Repository associata alla classe Scalata. | Creato |
| SearchRepository.java | Interfaccia eliminata, che veniva utilizzata per dichiarare query personalizzate in MongoDB, implementata in SearchRepositoryImpl. | Eliminato |
| SearchRepositoryImpl.java | Classe eliminata, che definiva query personalizzate effettuate in MongoDB di utilizzo generale, che è stata eliminata in quanto le query corrispondenti sono state migrate nelle apposite interfacce Repository, implementate in linguaggio JPQL. | Eliminato |
| TeamAdminRepository.java | Interfaccia eliminata, che veniva utilizzata per dichiarare query in MongoDB relative alla tabella associativa TeamAdmin. | Eliminato |
| TeamRepository.java | Interfaccia Repository associata alla classe Team. | Creato |

---

### Package service

Il package service definisce i servizi invocati dai Controller per realizzare la logica di business, interagendo con il Repository per effettuare oprerazioni con il database. Per quanto riguarda le classi Service già presenti, le modifiche riguardano i seguenti aspetti:

- Refactoring legato alla separazione delle responsabilità, in quanto alcuni metodi restituivano direttamente risposte HTTP di tipo ResposeEntity<?>  
- Refactoring legato all’autenticazione con il token jwt, che è stata rimossa in vista della presenza del filtro AuthTokenFilter  
- Refactoring necessario ad uniformarsi ai nuovi repository JPA in seguito alla migrazione del database  

| File | Descrizione | Creato o Modificato |
|------|-------------|---------------------|
| AdminService.java | Refactoring | Modificato |
| AssignmentService.java | Refactoring | Modificato |
| ClassUTService.java | Refactoring | Modificato |
| EmailService.java | Refactoring | Modificato |
| GuidelineService.java | Definisce la business logic relativi alle operazioni sulle linee guida:<br><br>uploadGuidelines per caricare le linee guida nel database, effettuando una update se già presenti<br><br>uploadGuidelineImage per caricare un’immagine associata ad una linea guida, tramite ImageService. Qualora sia presente già un’immagine, questa viene prima cancellata dal file system.<br><br>findGuidelines restituisce una lista contenente tutte le linee guida presenti nel database<br><br>deleteGuideline per cancellare una linea guida con un opportuno valore del campo “order”. Qualora sia associata ad un’immagine, questa viene prima cancellata dal file system.<br><br>deleteGuidelineImage per cancellare l’immagine associata ad una linea guida, tramite ImageService.<br><br>Tutti i metodi utilizzano all’occorrenza GuidelineMapper per convertire gli oggetti in input DTO negli oggetti corrispondenti Model / gli oggetti in output Model negli oggeti corrispondenti DTO | Creato |
| ImageService.java | Definisce i metodi per l’accesso al filesystem relativo alle operazioni per gestire le immagini di suggerimenti e linee guida:<br><br>storeImage per caricare un’immagine nel filesystem. Utilizza Tika per verificare che il file ricevuto (MultipartFile) sia sicuro, in quanto viene confrontato il formato reale del file con quelli previsti per le immagini.<br><br>deleteImage per cancellare un’immagine dal filesystem | Creato |
| InteractionService.java | Refactoring | Modificato |
| JwtService.java | Modificato per rispecchiare il fatto che la chiave primaria dell’Admin è email e non username | Modificato |
| NotificationService.java | Refactoring | Modificato |
| OpponentService.java | Refactoring | Modificato |
| ScalataService.java | Refactoring | Modificato |
| SecurityService.java | Wrapper di JwtRequestContext per poter facilitare il testing, permettendone la configurazione attraverso @MockBean, in quanto JwtRequestContext presenta solo metodi statici e non è mockabile. | Creato |
| StudentService.java | Refactoring | Modificato |
| SuggestionService.java | Definisce la business logic relativi alle operazioni sui suggerimenti:<br><br>uploadSuggestions per caricare i suggerimenti associati ad una specifica calsse nel database, effettuando una update se già presenti<br><br>uploadSuggestionImage per caricare un’immagine associata ad un suggerimento tramite ImageService. Qualora sia presente già un’immagine, questa viene prima cancellata dal file system.<br><br>findSuggestions restituisce una lista contenente tutti i suggerimenti associati ad una specifica classe presente nel database<br><br>deleteSuggestion per cancellare un suggerimento associato ad una specifica classe con un opportuno valore del campo “order”. Qualora sia associata ad un’immagine, questa viene prima cancellata dal file system.<br><br>deleteSuggestoinImage per cancellare l’immagine associata ad un suggerimento, tramite ImageService.<br><br>Tutti i metodi utilizzano all’occorrenza SuggestionMapper per convertire gli oggetti in input DTO negli oggetti corrispondenti Model / gli oggetti in output Model negli oggeti corrispondenti DTO | Creato |
| TeamService.java | Refactoring | Modificato |
| UploadOpponentService.java | Refactoring | Modificato |

---

TeamModificationRequest.java è stata spostata nel package DTO data la sua natura.

---

### Package controller

Il package controller mappa gli endpoint REST API agli opportuni metodi che gestiscono le funzionalità esportate del sistema, usufruendo dei servizi del Service. Per quanto riguarda le classi Controller già presenti, le modifiche riguardano i seguenti aspetti:

- Refactoring legato alla separazione delle responsabilità, in quanto alcuni metodi si aspettavano come valore di ritorno dal Service delle risposte HTTP di tipo ResposeEntity<?>. Ora le rispose HTTP sono create dal Controller  
- Refactoring legato all’autenticazione con il token jwt, che è stata rimossa in vista della presenza del filtro AuthTokenFilter  

| File | Descrizione | Creato o Modificato |
|------|-------------|---------------------|
| AdminController.java | Refactoring | Modificato |
| AssignmentController.java | Refactoring | Modificato |
| GuidelineController.java | Mappa gli endpoint relativi alle operazioni sulle linee guida:<br><br>uploadGuidelines POST per caricare le linee guida nel database<br><br>uploadGuidelineImage POST per caricare un’immagine associata ad una linea guida.<br><br>findGuidelines GET restituisce una lista contenente tutte le linee guida presenti nel database.<br><br>deleteGuideline DELETE per cancellare una linea guida con un opportuno valore del campo “order”.<br><br>deleteGuidelineImage DELETE per cancellare l’immagine associata ad una linea guida. | Creato |
| HomeController.java | Refactoring | Modificato |
| InteractionController.java | Refactoring | Modificato |
| OpponentController.java | Refactoring | Modificato |
| ScalataController.java | Refactoring | Modificato |
| StudentController.java | Refactoring | Modificato |
| SuggestionController.java | Mappa gli endpoint relativi alle operazioni sui suggerimenti:<br><br>uploadSuggestions POST per caricare i suggerimenti associati ad una classe nel database<br><br>uploadSuggestionImage POST per caricare un’immagine associata ad un suggerimento.<br><br>findSuggestions GET restituisce una lista contenente tutti i suggerimenti presenti nel database.<br><br>deleteSuggestion DELETE per cancellare un suggerimento con un opportuno valore del campo “order”.<br><br>deleteSuggestionImage DELETE per cancellare l’immagine associata ad un suggerimento. | Creato |
| TeamController.java | Refactoring | Modificato |

---

### Package controller.view

È stata aggiunta la sola classe GuidelineViewController per mappare la nuova vista relativa alle linee guida al metodo “showGuidelinesPage”.

---

### Package dto

Le clasi DTO sono utilizzate per mappare i dati contenuti nei JSON forniti in ingresso al sistema nelle chiamate REST.

Questo package è stato estratto da com.groom.manvsclass.model e spostato in com.groom.manvsclass, in modo da rispecchiare il diagramma dei package.

I dto già esistenti erano limitati e quindi sono stati aggiunti i dto necessari a ricevere le informazioni nei vari controller. Sono state utilizzate molte annotazioni di jakarta.validation per abilitare la validazione dei campi dei JSON forniti in ingresso, quali:

- @NotNull  
- @NotBlank  
- @Positive  
- @Size  
- @Min  
- @Max  
- @ValidOrder  

| File | Descrizione | Creato o Modificato |
|------|-------------|---------------------|
| AssignmentDTO.java | - | Creato |
| ClassUTDTO.java | - | Creato |
| ClassUTScalataDTO.java | - | Creato |
| ClassUTSuggestionDTO.java | - | Creato |
| GuidelineDTO.java | - | Creato |
| InteractionDTO.java | - | Creato |
| ScalataDTO.java | - | Creato |
| SuggestionDTO.java | - | Creato |
| TeamDTO.java | - | Creato |

---

### Package mapper

Le classi Mapper sono utilizzate dai Service per ottenere gli oggetti Model a partire dai DTO ricevuti in input / gli oggetti DTO da restituire in output a partire dai Model ricevuti dai Repository. Sono stati implementati tramite MapStruct per una generazione automatica dei metodi di conversione tra DTO e Model e viceversa. L’aggiunta dei Mapper consente ai Service di non avere la conoscenza sulla esatta dei DTO, ma solo dei Model, e analogamente ai Controller di non avere la conoscenza esatta sui Model, il che comporta una migliore separazione delle responsabilità.

| File | Descrizione | Creato o Modificato |
|------|-------------|---------------------|
| AssignmentMapper.java | Mapper tra DTO e Model relativi a Assignment | Creato |
| GuidelineMapper.java | Mapper tra DTO e Model relativi a Guideline | Creato |
| InteractionMapper.java | Mapper tra DTO e Model relativi a Interaction | Creato |
| ScalataMapper.java | Mapper tra DTO e Model relativi a Scalata | Creato |
| SuggestionMapper.java | Mapper tra DTO e Model relativi a Suggestion | Creato |
| TeamMapper.java | Mapper tra DTO e Model relativi a Team | Creato |

---

### Package exception

Questo package è stato estratto da com.groom.manvsclass.service e spostato in com.groom.manvsclass.

È stato definita la classe GlobalExceptionHandler per centralizzare la gestione delle eccezioni e alleggerire il lavoro ai Controller, i quali non devono più occuparsi dei casi limite di errore. La generazione delle risposte http in caso di errore è quindi delegata completamente a questa classe.

Sono inoltre state definite le seguenti eccezioni che estendono RuntimeException, lanciate dai Service:

| File | Descrizione | Creato o Modificato |
|------|-------------|---------------------|
| DuplicatedEntryException.java | - | Creato |
| ForbiddenException.java | - | Creato |
| InvalidImageException.java | - | Creato |
| NotFoundException.java | - | Creato |
| UnauthorizedException.java | - | Creato |
| GlobalExceptionHandler.java | Gestore globale delle eccezioni | Creato |
