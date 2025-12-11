# Guida Test API Scalate

## 1. Aprire browser dal lato amministratore

```bash
open http://localhost/admin/login
```

## 2. Nella console del browser (F12)

```javascript
document.cookie.split('; ').find(row => row.startsWith('jwt=')).split('=')[1]
```

## 3. Settare nel terminale dell'IDE dentro la cartella del progetto

```bash
JWT="risultato_del_punto_2"
```

## 4. Per accedere alla lista delle scalate presenti

```bash
curl -s -X GET "http://localhost/scalata/scalate_list" \
  -H "Cookie: jwt=${JWT}" | jq '.'
```
NOTA --> per richieste di tipo GET basterebbe un JWT anche dello studente

## 5. Per inserire una nuova scalata

```bash
curl -s -X POST "http://localhost/scalata/configureScalata" \
  -H "Content-Type: application/json" \
  -H "Cookie: jwt=${JWT}" \
  -d '{
    "scalataName": "ScalataDiProva",
    "username": "TestUser",
    "scalataDescription": "Scalata di test",
    "numberOfLevels": 2,
    "levels": [1, 2]
  }'
```
NOTA --> verificare che il jwt sia quello dell'admin , altrimenti non si hanno i permessi per una richiesta di tipo POST

## 6. Per eliminare una scalata esistente

```bash
curl -s -X DELETE "http://localhost/scalata/delete_scalata/ScalataDiProva" \
  -H "Cookie: jwt=${JWT}"
```
NOTA --> Sostituire `TestScalata_123456` con il nome della scalata da eliminare. Richiede JWT dell'admin.

**IMPORTANTE**: Se il nome della scalata contiene spazi, usare URL encoding (`%20`):
```bash
# Esempio: eliminare "Scalata Prova"
curl -s -X DELETE "http://localhost/scalata/delete_scalata/ScalataDiProva" \
  -H "Cookie: jwt=${JWT}"
```

## 7. Per recuperare il livello i-esimo di una scalata

```bash
# Esempio: recupera il livello 2 della scalata "ScalataDiProva"
curl -s -X GET "http://localhost/scalata/ScalataDiProva/level/2" \
  -H "Cookie: jwt=${JWT}" | jq '.'
```

**Con spazi nel nome (URL encoding):**
```bash
# Esempio: livello 1 della scalata "Scalata Facile"
curl -s -X GET "http://localhost/scalata/Scalata%20Facile/level/1" \
  -H "Cookie: jwt=${JWT}" | jq '.'
```

# Guida test API Level

# 1. Crea un nuovo livello
```bash
curl -X POST "http://localhost/level" \
  -H "Cookie: jwt=${JWT}" \
  -H "Content-Type: application/json" \
  -d '{
    "idLevel": 1,
    "scalataName": "ScalataDiProva",
    "className": "MyTestClass",
    "tempoMax": 300,
    "opponentName": "Robot1"
  }'
```

# 2. Lista livelli di una scalata
```bash
curl -X GET "http://localhost/level/scalata/ScalataDiProva" \
  -H "Cookie: jwt=${JWT}"
```

# 3. Dettagli di un livello
```bash
curl -X GET "http://localhost/level/1" \
  -H "Cookie: jwt=${JWT}"
```

# 4. Aggiorna un livello
```bash
curl -X PUT "http://localhost/level/1" \
  -H "Cookie: jwt=${JWT}" \
  -H "Content-Type: application/json" \
  -d '{
    "idLevel": 1,
    "scalataName": "ScalataDiProva",
    "className": "FTPFile",
    "tempoMax": 600,
    "opponentName": "Robot2"
  }'
```

# 5. Elimina un livello
```bash
curl -X DELETE "http://localhost/level/1" \
  -H "Cookie: jwt=${JWT}"
```

---

# Guida test API Game (T4)

## 0. Trova gameId di una partita

### Opzione A: Via API (richiede JWT)
```bash
# Lista tutti i game del player 2
curl -s -X GET "http://localhost/api/gamerepo/games/player/2" \
  -H "Cookie: jwt=${JWT}" | jq '.[] | {id, gameMode, status, currentLevel}'

# Prendi solo l'ID dell'ultima partita
curl -s -X GET "http://localhost/api/gamerepo/games/player/2" \
  -H "Cookie: jwt=${JWT}" | jq '.[-1].id'
```

### Opzione B: Query diretta al database
```bash
docker exec t4-postgres_db psql -U t4_service -d t4_database -c "SELECT id, game_mode, status, current_level, scalata_name, started_at FROM games ORDER BY id DESC LIMIT 5;"
```
### Query diretta per visualizzare i round
```bash
docker exec t4-postgres_db psql -U t4_service -d t4_database -c "SELECT id, round_number, classut, type, difficulty, started_at, closed_at FROM rounds ORDER BY id DESC LIMIT 10;"
```

### Query diretta per i turni
```bash
docker exec t4-postgres_db psql -U t4_service -d t4_database -c "SELECT t.id, t.round_id, t.turn_number, t.player_id, t.started_at, t.closed_at, r.round_number, r.classut FROM turns t JOIN rounds r ON t.round_id = r.id WHERE r.game_id = 1203 ORDER BY t.id DESC LIMIT 10;"
```

## 1. Leggi dettagli di un Game
```bash
# Sostituisci 202 con un gameId valido
curl -s -X GET "http://localhost/api/gamerepo/games/202" \
  -H "Cookie: jwt=${JWT}" | jq '{id, gameMode, status, currentLevel}'
```

## 2. Incrementa currentLevel di una partita Scalata
```bash
# Incrementa di +1 il livello corrente
curl -s -X PATCH "http://localhost/api/gamerepo/games/202/current-level-increment" \
  -H "Cookie: jwt=${JWT}" | jq '.currentLevel'
```

## 3. Verifica dopo incremento
```bash
# Verifica che currentLevel sia aumentato
curl -s -X GET "http://localhost/api/gamerepo/games/202" \
  -H "Cookie: jwt=${JWT}" | jq '{id, currentLevel}'
```

**NOTA**: 
- Gli endpoint di T4 richiedono il prefisso `/api/gamerepo/`
- **Il base path del controller è `/games` (plurale, non singolare!)**
- `currentLevel` è nullable (può essere null per partite non-Scalata)
- L'incremento è atomico e thread-safe
- Esempio path completo: `/api/gamerepo/games/202/current-level-increment`

---

## Utilizzo da T5 (ServiceManager)

In T5, puoi usare il `ServiceManager` per chiamare l'incremento:

```java
// Esempio in un controller o service di T5
Long gameId = 202L;

String result = (String) ServiceManager.getInstance()
    .executeService("T4Service", "IncrementCurrentLevel", gameId);

// Il metodo ritorna il JSON completo del GameDTO aggiornato
// con il nuovo currentLevel incrementato
```

**File modificati in T5:**
- `T5-G2/t5/src/main/java/com/g2/interfaces/BaseService.java` - Aggiunto metodo `callRestPatch()`
- `T5-G2/t5/src/main/java/com/g2/interfaces/T4Service.java` - Aggiunto metodo `IncrementCurrentLevel(long gameId)`