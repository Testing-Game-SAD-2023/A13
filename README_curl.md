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
    "scalataName": "TestScalata_'$(date +%s)'",
    "username": "TestUser",
    "scalataDescription": "Scalata di test",
    "numberOfLevels": 3,
    "levels": [1, 2, 3]
  }'
```
NOTA --> verificare che il jwt sia quello dell'admin , altrimenti non si hanno i permessi per una richiesta di tipo POST

## 6. Per eliminare una scalata esistente

```bash
curl -s -X DELETE "http://localhost/scalata/delete_scalata/TestScalata_123456" \
  -H "Cookie: jwt=${JWT}"
```
NOTA --> Sostituire `TestScalata_123456` con il nome della scalata da eliminare. Richiede JWT dell'admin.

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
    "className": "UpdatedClass",
    "tempoMax": 600,
    "opponentName": "Robot2"
  }'
```

# 5. Elimina un livello
```bash
curl -X DELETE "http://localhost/level/1" \
  -H "Cookie: jwt=${JWT}"
```
