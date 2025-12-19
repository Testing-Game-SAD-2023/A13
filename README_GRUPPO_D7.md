# README_GRUPPO_D7
 
## Gruppo D7 - Task R8
**Componenti del gruppo:**
* Fabio Davide Auriemma (M63001848)
* Marco Canonico (N46007287)
* Luca Carraturo (DE9000145)
* Angela Dalia (DE9000055)
 
---
 
### 0) Descrizione del Task svolto e Id del Task
**ID Task:** R8 - API Gateway (Rate Limit, Circuit Breaker e Logging)
 
Il task assegnato prevedeva l'ottimizzazione dell'API Gateway attraverso l'implementazione e la configurazione corretta di meccanismi di resilienza, specificamente Rate Limit e Circuit Breaker. L'obiettivo principale era proteggere i microservizi critici (in particolare T7 e T8, identificati come colli di bottiglia) e definire un formato di risposta uniforme per la gestione degli errori e dei messaggi di fallback.Inoltre, il lavoro ha incluso il potenziamento dell'infrastruttura di osservabilità per monitorare in maniera centralizzata il comportamento del sistema.
 
### 1) Implementazione del task
 
* **Resilienza:** È stato utilizzato **Spring Cloud Gateway** integrato con la libreria **Resilience4j**.
    * **Circuit Breaker:** Configurato su T7 e T8 con una macchina a stati che gestisce le transizioni (Closed, Open, Half-Open) in base al tasso di errore, fornendo risposte di fallback rapide.
    * **Rate Limiter:** Implementato tramite **Redis** (algoritmo Token Bucket) e personalizzato con un `PrincipalNameKeyResolver` per applicare limiti basati sull'identità dell'utente (estratta dal token JWT) anziché solo sull'IP.
    * **Gestione Errori:** Implementato un *Global Filter* per intercettare le eccezioni e restituire risposte JSON standardizzate, mascherando i dettagli interni.
 
* **Osservabilità:** È stato implementato uno stack LGTM (Loki, Grafana, Tempo, Prometheus) orchestrato tramite Docker Compose. La strumentazione adotta un approccio ibrido: Micrometer è utilizzato per la raccolta delle metriche applicative, mentre le librerie OpenTelemetry operano in concorrenza per la gestione del tracciamento distribuito.
 
* **Ottimizzazione T8:** Il servizio T8 (EvoSuite) è stato sottoposto a refactoring per ottimizzare l'uso della memoria (32GB disponibili sul server) e ridurre i tempi di build ripulendo il codice da calcoli metrici superflui.
#### Elenco delle principali modifiche ai microservizi
 
| Microservizio Modificato | Tipo di Modifica | Eventuali Nuove Tecnologie usate |
| :--- | :--- | :--- |
| **API Gateway** | Aggiunta logica di identificazione client (KeyResolver), configurazione centralizzata gestione errori (GlobalErrorAttributes), logica custom filtro autenticazione. Integrazione Circuit Breaker e Rate Limiter. | Resilience4j, Redis (Token Bucket) |
| **Infrastruttura (Docker/Config)** | Creazione pipeline OpenTelemetry (Collector, Prometheus, Tempo, Loki, Grafana). Configurazione dashboard e orchestrazione container. | OpenTelemetry, Prometheus, Tempo, Loki, Grafana |
| **T8 (EvoSuite)** | Refactoring logica di business (`CoverageService`, `BuildResponse`) per ottimizzazione memoria e tempi di risposta. Strumentazione per tracciamento. | Micrometer |
| **T1, T4, T5** | Strumentazione per tracciamento distribuito (aggiunta dipendenze `pom.xml`) e configurazione sicurezza per scraping metriche. | Micrometer |
| **T23** | Aggiornamento script inizializzazione DB (`init.sql`) e configurazione sicurezza per scraping metriche. | MySQL (init script) |
 
### 2) Integrazioni effettuate con altri gruppi
Per garantire il corretto funzionamento dell'infrastruttura di osservabilità distribuita, sono stati integrati e modificati i microservizi gestiti originariamente da altri gruppi (T1, T4, T5, T23).
Nello specifico, sono state aggiornate le classi `AuthTokenFilter` e `WebSecurityConfig` nei servizi T1, T5 e T23 per permettere a Prometheus di effettuare lo scraping delle metriche senza essere bloccato dai filtri di sicurezza.
 
### 3) Errori/problematiche non risolte nel progetto consegnato
* **Vulnerabilità Endpoint Actuator:** Gli endpoint di gestione `/actuator` (utilizzati per health check e metriche) non richiedono attualmente la validazione tramite token JWT. Questo espone potenzialmente metriche sensibili e lo stato del sistema ad accessi non autorizzati. Non è stato risolto in questa iterazione per priorità data alla stabilità funzionale, ma si raccomanda di estendere la configurazione di sicurezza.
* **Visualizzazione Grafana:** Sono state riscontrate alcune imprecisioni nella visualizzazione degli errori nelle dashboard di Grafana. Alcune eccezioni sollevate dai microservizi potrebbero non essere intercettate correttamente dalle query attuali, causando potenziali falsi negativi nella rappresentazione grafica.
* **Scalabilità Orizzontale (T7/T8):** Nonostante l'ottimizzazione del codice, per gestire carichi molto elevati sarebbe necessaria la scalabilità orizzontale dei servizi T7 e T8. Attualmente non è implementata poiché richiederebbe la disponibilità di più macchine server o di verificare l'effettiva capacità dell'attuale computer utilizzato.


