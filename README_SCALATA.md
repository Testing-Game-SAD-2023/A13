# Documentazione Implementazione Funzionalità Scalata

## Indice
1. [Panoramica](#panoramica)
2. [Modifiche a T1 (manvsclass)](#modifiche-a-t1-manvsclass)
3. [Modifiche a T5 (Game Engine)](#modifiche-a-t5-game-engine)
4. [Modifiche a UI Gateway (Nginx)](#modifiche-a-ui-gateway-nginx)
5. [Architettura e Flusso delle Richieste](#architettura-e-flusso-delle-richieste)
6. [Testing](#testing)

---

### Componenti Coinvolti
- **T1 (manvsclass)**: Backend service che gestisce le scalate, i livelli e le classi
- **T5 (Game Engine)**: Service che orchestra il gameplay e presenta l'interfaccia utente
- **UI Gateway (Nginx)**: Reverse proxy per il routing delle richieste HTTP

---

## Modifiche a T1 (manvsclass)

### 1. Nuovi File Creati

#### 1.1 Model - `Scalata.java`
**Path**: `src/main/java/com/groom/manvsclass/model/Scalata.java`

**Descrizione**: Classe entity che rappresenta una scalata nel database MongoDB.

**Campi principali**:
- `scalataName`: Nome univoco della scalata
- `username`: Utente proprietario della scalata
- `scalataDescription`: Descrizione testuale
- `numberOfLevels`: Numero totale di livelli
- `levels`: Lista di ID dei livelli che compongono la scalata

```java
@Document(collection = "scalate")
public class Scalata {
    @Id
    private String id;
    private String scalataName;
    private String username;
    private String scalataDescription;
    private int numberOfLevels;
    private List<Integer> levels;
    // ... getters, setters, constructors
}
```

#### 1.2 Repository - `ScalataRepository.java`
**Path**: `src/main/java/com/groom/manvsclass/model/repository/ScalataRepository.java`

**Descrizione**: Interfaccia repository per l'accesso ai dati delle scalate usando Spring Data MongoDB.

**Metodi custom**:
- `findByScalataName(String scalataName)`: Trova una scalata per nome
- `deleteByScalataName(String scalataName)`: Elimina una scalata per nome
- `existsByScalataName(String scalataName)`: Verifica esistenza per nome

```java
public interface ScalataRepository extends MongoRepository<Scalata, String> {
    Optional<Scalata> findByScalataName(String scalataName);
    void deleteByScalataName(String scalataName);
    boolean existsByScalataName(String scalataName);
}
```

#### 1.3 Service - `ScalataService.java`
**Path**: `src/main/java/com/groom/manvsclass/service/ScalataService.java`

**Descrizione**: Service layer che implementa la business logic per la gestione delle scalate.

**Funzionalità implementate**:
- `uploadScalata()`: Crea/aggiorna una scalata
- `listScalate()`: Recupera tutte le scalate disponibili
- `retrieveScalataByName()`: Recupera dettagli di una scalata specifica
- `deleteScalataByName()`: Elimina una scalata (solo per ADMIN)

**Validazioni**:
- Controllo esistenza scalata per update/delete
- Validazione permessi utente (username match per operazioni)
- Validazione JWT per operazioni protette

#### 1.4 Controller - `ScalataController.java`
**Path**: `src/main/java/com/groom/manvsclass/controller/ScalataController.java`

**Descrizione**: REST Controller che espone gli endpoint HTTP per la gestione delle scalate.

**Endpoints**:
```
POST   /scalata/configureScalata            - Crea/aggiorna una scalata
GET    /scalata/scalate_list                 - Lista tutte le scalate
GET    /scalata/retrieve_scalata/{name}      - Dettagli scalata
DELETE /scalata/delete_scalata/{name}        - Elimina scalata
GET    /scalata/{scalataName}/level/{currentLevel}  - Recupera livello i-esimo di una scalata (NUOVO)
```

**Annotazioni**:
- `@CrossOrigin`: Permette richieste CORS
- `@Controller`: Definisce come controller Spring
- `@RequestMapping("/scalata")`: Base path per tutti gli endpoint

**⚠️ IMPORTANTE - URL Encoding**:
Se il nome della scalata contiene **spazi**, è necessario usare **URL encoding** (`%20`):
```bash
# ❌ ERRATO (con spazi)
GET /scalata/Scalata Facile/level/1

# ✅ CORRETTO (con URL encoding)
GET /scalata/Scalata%20Facile/level/1
```

Questo vale per **tutti** gli endpoint che accettano `{scalataName}` come parametro di path.

#### 1.5 Nuovo Metodo - `ScalataService.getLevelByPosition()`
**Descrizione**: Recupera il livello i-esimo di una scalata specifica.

**Parametri**:
- `scalataName`: Nome della scalata
- `currentLevel`: Posizione del livello (1-based: 1=primo, 2=secondo, etc.)

**Logica**:
1. Trova la scalata per nome
2. Valida che `currentLevel` sia nel range valido (1 ≤ currentLevel ≤ numberOfLevels)
3. Estrae l'ID del livello dall'array `levels[currentLevel - 1]`
4. Carica i dati del Level dal repository usando l'ID
5. Restituisce i dati completi del livello

**Validazioni**:
- Scalata non trovata → HTTP 404
- Livello fuori range → HTTP 400
- ID livello non trovato nel DB → HTTP 500 (dati corrotti)

**Codice**:
```java
public ResponseEntity<?> getLevelByPosition(String scalataName, int currentLevel) {
    // 1. Trova scalata
    List<Scalata> scalate = scalata_repo.findByScalataNameContaining(scalataName);
    if (scalate.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(Map.of("error", "Scalata non trovata: " + scalataName));
    }
    
    Scalata scalata = scalate.get(0);
    
    // 2. Valida currentLevel
    if (currentLevel < 1 || currentLevel > scalata.getLevels().size()) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(Map.of("error", "Livello non valido"));
    }
    
    // 3. Estrai ID livello (array 0-based, currentLevel 1-based)
    Integer levelId = scalata.getLevels().get(currentLevel - 1);
    
    // 4. Carica Level
    Optional<Level> level = levelRepository.findById(levelId);
    if (level.isEmpty()) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Map.of("error", "Dati livello corrotti - ID non trovato"));
    }
    
    return ResponseEntity.ok(level.get());
}
```

### 2. File Modificati

#### 2.1 `AuthTokenFilter.java`
**Path**: `src/main/java/com/groom/manvsclass/security/AuthTokenFilter.java`

**Modifiche**: Aggiunto `/scalata/**` agli URI accessibili dai giocatori (ruolo PLAYER).

**Prima**:
```java
private static final List<String> PLAYER_ALLOWED_URIS = List.of(
    "/opponents/**",
    "/ottieniTeamByStudentId",
    "/ottieniDettagliTeamCompleto"
);
```

**Dopo**:
```java
private static final List<String> PLAYER_ALLOWED_URIS = List.of(
    "/opponents/**",
    "/ottieniTeamByStudentId",
    "/ottieniDettagliTeamCompleto",
    "/scalata/**"  // ← AGGIUNTO
);
```

**Nota importante**: I PLAYER possono fare solo richieste GET su `/scalata/**`. Le operazioni POST/DELETE richiedono ruolo ADMIN.

#### 2.2 `LevelService.java`
**Path**: `src/main/java/com/groom/manvsclass/service/LevelService.java`

**Modifiche**: Aggiunto metodo `getLevelsByIds()` per recuperare multipli livelli dato un array di ID.

**Nuovo metodo**:
```java
public List<Level> getLevelsByIds(List<Integer> ids, String jwt) {
    // Validazione JWT
    // Recupero livelli dal repository
    // Ritorno lista di livelli
}
```

**Utilizzo**: Permette a ScalataService di recuperare tutti i livelli di una scalata in una singola chiamata.

---

## Modifiche a T5 (Game Engine)

### 1. File Modificati

#### 1.1 `T1Service.java`
**Path**: `src/main/java/com/g2/interfaces/T1Service.java`

**Aggiunta action getScalateList**: Nuovo metodo per recuperare la lista delle scalate

```java
registerAction("getScalateList", new ServiceActionDefinition(
    params -> getScalateList()
));

private List<Map<String, Object>> getScalateList() {
    return callRestGET("/scalata/scalate_list", null, 
        new ParameterizedTypeReference<List<Map<String, Object>>>() {});
}
```

**Flusso completo della chiamata**:
```
T5 chiama: getScalateList()
    ↓
Costruisce URL: BASE_URL + "/" + SERVICE_PREFIX + "/scalata/scalate_list"
    ↓
Risultato: http://api_gateway-controller:8090/adminService/scalata/scalate_list
    ↓
API Gateway rewrites: /adminService/scalata/scalate_list → /scalata/scalata_list
    ↓
Inoltra a: http://t1-controller:8081/scalata/scalate_list
```

**Aggiunta action getLevelByScalataAndPosition**: Nuovo metodo per recuperare il livello i-esimo di una scalata

```java
registerAction("getLevelByScalataAndPosition", new ServiceActionDefinition(
    params -> getLevelByScalataAndPosition((String) params[0], (Integer) params[1]),
    String.class, Integer.class
));

@SuppressWarnings("unchecked")
private Map<String, Object> getLevelByScalataAndPosition(String scalataName, Integer currentLevel) {
    String path = "/scalata/" + scalataName + "/level/" + currentLevel;
    return callRestGET(path, null, Map.class);
}
```

**Parametri**:
- `scalataName`: Nome della scalata (String)
- `currentLevel`: Posizione del livello 1-based (Integer)

**Flusso completo della chiamata**:
```
T5 chiama: getLevelByScalataAndPosition("ScalataDiProva", 2)
    ↓
Costruisce URL: BASE_URL + "/scalata/ScalataDiProva/level/2"
    ↓
Risultato: http://api_gateway-controller:8090/adminService/scalata/ScalataDiProva/level/2
    ↓
API Gateway rewrites: /adminService/scalata/... → /scalata/...
    ↓
Inoltra a: http://t1-controller:8081/scalata/ScalataDiProva/level/2
    ↓
T1 processa: ScalataController.getLevelByPosition()
    ↓
Restituisce: Level object (className, opponentName, tempoMax, etc.)
```

**Uso nel codice**:
```java
// Da GameManager o ScalataGame
Map<String, Object> levelData = (Map<String, Object>) serviceManager
    .handleRequest("T1", "getLevelByScalataAndPosition", "ScalataDiProva", 2);

String className = (String) levelData.get("className");
String opponentName = (String) levelData.get("opponentName");
Integer tempoMax = (Integer) levelData.get("tempoMax");
```

#### 2.2 `GuiController.java`
**Path**: `src/main/java/com/g2/controllers/GuiController.java`

**Modifiche**: Aggiunto handling per la modalità "Scalata" nel metodo `chooseGameMode()`.

**Codice aggiunto**:
```java
if ("Scalata".equals(mode)) {
    PageBuilder gameModePage = new PageBuilder(serviceManager, "gamemode_scalata", model);
    
    // Recupera la lista delle scalate disponibili da T1
    try {
        Object scalateData = serviceManager.handleRequest("T1", "getScalateList");
        logger.info("[GuiController] Scalate ricevute da T1: {}", scalateData);
        
        // Converte in JSON string per Thymeleaf
        ObjectMapper objectMapper = new ObjectMapper();
        String scalateJson = objectMapper.writeValueAsString(scalateData);
        
        model.addAttribute("available_scalate_json", scalateJson);
        model.addAttribute("available_scalate", scalateData);
    } catch (Exception e) {
        logger.error("[GuiController] Errore nel recupero scalate: {}", e.getMessage(), e);
        model.addAttribute("available_scalate_json", "[]");
        model.addAttribute("available_scalate", new ArrayList<>());
    }
    
    return gameModePage.handlePageRequest();
}
```

**Funzionalità**:
- Chiama T1Service per ottenere le scalate disponibili
- Converte i dati in formato JSON per la compatibilità con Thymeleaf
- Gestisce errori con valori di fallback (lista vuota)
- Passa i dati al template `gamemode_scalata.html`

---

## Modifiche a UI Gateway (Nginx)

### File Modificato: `T1_route.conf`
**Path**: `ui_gateway/routes/T1_route.conf`

**Modifiche**: Aggiunta location block per le richieste alle scalate.

**Codice aggiunto**:
```nginx

### 📌 SCALATA
location ~ ^/scalata/(main|configureScalata|scalate_list|retrieve_scalata|delete_scalata) {
    include /etc/nginx/includes/proxy.conf;
    proxy_pass $backend_T1;
}

### 📌 LEVEL
location ~ ^/level {
    include /etc/nginx/includes/proxy.conf;
    proxy_pass $backend_T1;
}
```

**Spiegazione**:
- **Pattern matching**: `~ /scalata/(.*)` cattura tutti i path che iniziano con `/scalata/`
- **Proxy pass**: Inoltra le richieste a `t1-controller:8081` (servizio T1)
- **Headers**: Preserva informazioni sul client originale
- **Cookies**: Cruciale per passare il JWT token di autenticazione

---

## Architettura e Flusso delle Richieste

### Pattern di Routing Implementato

Il sistema utilizza **due percorsi diversi** per accedere agli endpoint di scalata, a seconda della fonte della richiesta:

#### 1. Richieste dal Browser (tramite Nginx)

```
Browser
  ↓ GET http://localhost/scalata/scalate_list
Nginx (UI Gateway)
  ↓ /scalata/scalate_list
T1 Controller (ScalataController)
  ↓ @RequestMapping("/scalata")
Risposta JSON
```

#### 2. Richieste Server-Side da T5 (tramite API Gateway)

```
T5 GuiController
  ↓ serviceManager.handleRequest("T1", "getScalateList")
T1Service.getScalateList()
  ↓ BASE_URL + "/" + SERVICE_PREFIX + "/scalata/scalate_list"
  ↓ http://api_gateway-controller:8090/adminService/scalata/scalate_list
API Gateway
  ↓ RewritePath: /adminService/(?<segment>.*) → /${segment}
  ↓ http://t1-controller:8081/scalata/scalate_list
T1 Controller (ScalataController)
  ↓ @RequestMapping("/scalata")
Risposta JSON → T5
```


### Schema Completo dell'Architettura

```
┌─────────────────────────────────────────────────────────────┐
│                        BROWSER                               │
└────────────────────┬────────────────────────────────────────┘
                     │ HTTP + Cookie (JWT)
                     ↓
┌─────────────────────────────────────────────────────────────┐
│                    NGINX (UI Gateway)                        │
│  - Port 80                                                   │
│  - Routes:                                                   │
│    • /scalata/** → t1-controller:8081                       │
│    • /opponents/** → t1-controller:8081                     │
└────────────────────┬────────────────────────────────────────┘
                     │
                     ↓
┌─────────────────────────────────────────────────────────────┐
│              T1 (manvsclass) - Port 8081                     │
│  Controllers:                                                │
│    • ScalataController (@RequestMapping("/scalata"))        │
│    • OpponentController (@RequestMapping("/opponents"))     │
│  Security:                                                   │
│    • AuthTokenFilter validates JWT                          │
│    • PLAYER: GET only on /scalata/**, /opponents/**        │
│    • ADMIN: Full access                                     │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│              T5 (Game Engine) - Port 8085                    │
│  Components:                                                 │
│    • GuiController (handles UI requests)                    │
│    • T1Service (calls T1 via API Gateway)                   │
└────────────────────┬────────────────────────────────────────┘
                     │ Server-to-Server
                     │ http://api_gateway-controller:8090/adminService/**
                     ↓
┌─────────────────────────────────────────────────────────────┐
│           API Gateway (Spring Cloud) - Port 8090             │
│  Route: /adminService/**                                     │
│  Rewrite: /adminService/(?<segment>.*) → /${segment}       │
│  Target: http://t1-controller:8081                          │
└────────────────────┬────────────────────────────────────────┘
                     │
                     ↓
                  (back to T1)
```

---

## Note Importanti

### Sicurezza
- ⚠️ Gli endpoint POST/DELETE richiedono ruolo **ADMIN**
- ⚠️ I PLAYER possono solo leggere (GET) le scalate
- ⚠️ Il JWT deve essere sempre incluso nelle richieste


## Troubleshooting

### Problema: 401 Unauthorized
**Causa**: JWT mancante o scaduto  
**Soluzione**: Rifare login e ottenere nuovo JWT

### Problema: 403 Forbidden su POST
**Causa**: Ruolo PLAYER che tenta operazioni ADMIN  
**Soluzione**: Usare JWT con ruolo ADMIN

### Problema: 404 Not Found
**Causa**: Path errato o servizio non avviato  
**Soluzione**: 
1. Verificare che T1 sia in esecuzione: `docker ps | grep t1-controller`
2. Controllare i logs: `docker logs t1-controller`
3. Verificare path corretto: `/scalata/scalate_list` (non `/adminService/scalata/...`)

### Problema: Lista vuota anche dopo creazione
**Causa**: Scalata creata con username diverso  
**Soluzione**: Verificare che username in POST corrisponda all'utente loggato

---

**Ultima modifica**: 30 Novembre 2025  
**Autori**: Team di sviluppo R1  
**Versione**: 1.0
