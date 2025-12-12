# REST API - Progetto A13

## Gateway Configuration
**File:** `apiGateway/src/main/resources/application.yml`

```yaml
# T1 - AdminService
- id: T1-route
  uri: http://t1-controller:8081
  predicates:
    - Path=/adminService/**
  filters:
    - RewritePath=/adminService/(?<segment>.*), /${segment}

# T4 - GameRepo
- id: gamerepo-route
  uri: http://t4-controller:8084
  predicates:
    - Path=/gamerepo/**
  filters:
    - RewritePath=/gamerepo/(?<segment>.*), /${segment}
```

---

## Endpoint T1 - Gestione Scalate

### 1. Lista Scalate

**Microservizio:** T1 (AdminService)  
**Metodo:** GET  
**Endpoint (Interno):** `/scalata/scalate_list`  
**Endpoint (Gateway):** `/adminService/scalata/scalate_list`  

**Request:**
- Parametri: Nessuno

**Response (200 OK):**
```json
[
  {
    "scalataName": "Scalata Facile",
    "numberOfLevels": 3,
    "scalataDescription": "Scalata introduttiva",
    "username": "admin",
    "levels": [101, 102, 103]
  }
]
```

**Test:**
```bash
curl -X GET "http://localhost:8090/adminService/scalata/scalate_list"
```

---

### 2. Recupero Livello Scalata

**Microservizio:** T1 (AdminService)  
**Metodo:** GET  
**Endpoint (Interno):** `/scalata/{scalataName}/level/{currentLevel}`  
**Endpoint (Gateway):** `/adminService/scalata/{scalataName}/level/{currentLevel}`  

**Path Parameters:**
- `scalataName` (String): Nome della scalata
- `currentLevel` (Integer): Posizione livello (1-based)

**Response (200 OK):**
```json
{
  "idLevel": 102,
  "scalataName": "Scalata Facile",
  "className": "FTPFile",
  "tempoMax": 600,
  "opponentName": "EvoSuite"
}
```

**Errori:**
- `404`: Scalata non trovata
- `400`: Livello non valido
- `500`: Dati corrotti o errore interno

**Test:**
```bash
curl -X GET "http://localhost:8090/adminService/scalata/Scalata%20Facile/level/2"
```

---

## Endpoint T4 - Gestione Progressione Scalata

### 3. Creazione Partita Scalata

**Descrizione Funzionale:**  
Crea una nuova partita in modalità Scalata associata a un nome specifico di scalata. Questa API viene invocata da T5 all'inizio di una nuova partita Scalata per registrare il game in T4.

**Microservizio:** T4 (GameRepo)  
**Consumer:** T5 (GameEngine)  
**Metodo:** POST  
**Endpoint (Interno):** `/games`  
**Endpoint (Gateway):** `/gamerepo/games`  

**Request:**

**Body (application/json):**
```json
{
  "gameMode": "SCALATA",
  "players": [42],
  "scalataName": "Scalata Facile"
}
```

**Parametri Body:**

| Campo | Tipo | Obbligatorio | Descrizione |
|-------|------|--------------|-------------|
| `gameMode` | String (Enum) | ✅ Sì | Modalità di gioco (deve essere `"SCALATA"`) |
| `players` | Array[Long] | ✅ Sì | Array con ID dei giocatori (singolo per Scalata) |
| `scalataName` | String | ⚠️ Opzionale | Nome della scalata (salvato solo se `gameMode = SCALATA`) |

**Response (201 CREATED):**
```json
{
  "id": 12345,
  "gameMode": "SCALATA",
  "scalataName": "Scalata Facile",
  "currentLevel": null,
  "status": "CREATED",
  "players": [42],
  "createdAt": "2025-12-12T10:30:00",
  "closedAt": null
}
```

**Errori:**
- `400`: Giocatori duplicati nell'array `players`
  ```json
  {
    "error": "Duplicated players in game"
  }
  ```

**Note:**
- `currentLevel` viene inizializzato a `null` alla creazione
- Lo status iniziale è `CREATED`, diventa `STARTED` al primo round
- Il campo `scalataName` viene salvato solo se `gameMode = SCALATA`

**Test:**
```bash
curl -X POST "http://localhost:8090/gamerepo/games" \
  -H "Content-Type: application/json" \
  -d '{
    "gameMode": "SCALATA",
    "players": [42],
    "scalataName": "Scalata Facile"
  }'
```

---

### 4. Incremento Livello Corrente

**Descrizione Funzionale:**  
Incrementa il contatore `current_level` di una partita Scalata quando il giocatore completa con successo un livello e avanza al successivo. Viene invocata da T5 al termine di ogni livello superato.

**Microservizio:** T4 (GameRepo)  
**Consumer:** T5 (GameEngine)  
**Metodo:** PUT  
**Endpoint (Interno):** `/games/{gameId}/current-level-increment`  
**Endpoint (Gateway):** `/gamerepo/games/{gameId}/current-level-increment`  

**Path Parameters:**
- `gameId` (Long): ID univoco della partita Scalata

**Request:**
- Body: Nessuno (richiesta vuota)

**Response (200 OK):**
```json
{
  "id": 12345,
  "gameMode": "SCALATA",
  "currentLevel": 3,
  "scalataName": "Scalata Facile",
  "status": "IN_PROGRESS",
  "players": [42],
  "createdAt": "2025-12-12T10:30:00",
  "closedAt": null
}
```

**Errori:**
- `404`: Partita non trovata

**Note:**
- Se `current_level` è `null`, viene inizializzato a `1` prima dell'incremento
- Il livello viene incrementato di `1`: `currentLevel = currentLevel + 1`

**Test:**
```bash
curl -X PUT "http://localhost:8090/gamerepo/games/12345/current-level-increment"
```

---

### 4. Incremento Tentativo Round

**Descrizione Funzionale:**  
Incrementa il contatore di tentativi (`round_number`) dell'ultimo Round attivo quando il giocatore fallisce un livello in modalità Scalata e decide di riprovare. Questo permette di tracciare quanti tentativi sono stati effettuati sullo stesso livello.

**Microservizio:** T4 (GameRepo)  
**Consumer:** T5 (GameEngine)  
**Metodo:** PUT  
**Endpoint (Interno):** `/games/{gameId}/rounds/last/increment-attempt`  
**Endpoint (Gateway):** `/gamerepo/games/{gameId}/rounds/last/increment-attempt`  

**Path Parameters:**
- `gameId` (Long): ID univoco della partita Scalata

**Request:**
- Body: Nessuno (richiesta vuota)

**Response (200 OK):**
```json
{
  "id": 789,
  "roundNumber": 2,
  "classUT": "FTPFile",
  "type": "EvoSuite",
  "difficulty": "EASY",
  "createdAt": "2025-12-12T10:35:00",
  "closedAt": null
}
```

**Errori:**
- `404`: Partita non trovata o nessun Round attivo
- `400`: Partita non in stato `IN_PROGRESS`

**Note:**
- Cerca l'ultimo Round **non chiuso** (`closedAt == null`)
- Incrementa `round_number` di `1`: `roundNumber = roundNumber + 1`
- La partita deve essere nello stato `IN_PROGRESS`

**Test:**
```bash
curl -X PUT "http://localhost:8090/gamerepo/games/12345/rounds/last/increment-attempt"
```
