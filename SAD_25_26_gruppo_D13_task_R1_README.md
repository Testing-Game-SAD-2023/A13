# README_GRUPPO_D13.md

## 0) Descrizione del Task svolto e Id del Task

**Task ID:** R1

**Descrizione:** Questo progetto migliora la robustezza della funzionalità di upload delle classi Under Test e dei relativi unit test generati dai robot (Evosuite e Jacoco). Il task risolve problematiche critiche relative alla persistenza di file residui sul volume condiviso in caso di upload fallito e implementa un sistema di error reporting dettagliato per l'amministratore, seguendo il pattern WHAT + WHY + HOW.

## 1) Implementazione del task

### Descrizione ad alto livello

Il task è stato implementato principalmente attraverso il refactoring del microservizio **T1**, con modifiche minori ai microservizi **T7** e **T8**. L'approccio ha previsto:
- Ristrutturazione completa dei service layer per migliorare modularità e manutenibilità
- Implementazione di un sistema di rollback per la pulizia dei file in caso di errore
- Introduzione di un Global Exception Handler per gestione uniforme degli errori
- Validazione rigorosa dell'input sia lato front-end che back-end
- Standardizzazione del sistema di logging con SLF4J

### Principali modifiche apportate ai microservizi

| Microservizio | Tipo di Modifica | Eventuali Nuove Tecnologie |
|---------------|------------------|---------------------------|
| **T1** | Refactoring completo dei service: suddivisione di `OpponentService` e `UploadOpponentService` in classi più specifiche (`ClassUTUploadService`, `FileStorageService`, `JavaSourceFileService`, `OpponentTransformationService`, `CoverageReader`, `ScoreBuilder`) | SLF4J per logging uniforme |
| **T1** | Implementazione `GlobalExceptionHandler` per gestione centralizzata delle eccezioni con `ErrorResponseDTO` localizzata | - |
| **T1** | Aggiunta classe `FormValidation` per validazione input (lunghezza campi, formato nome classe, presenza file obbligatori) | - |
| **T1** | Implementazione meccanismo di rollback per pulizia file residui in caso di errore durante upload | - |
| **T1** | Aggiunta OpenAPI specification al controller di upload | OpenAPI/Swagger |
| **T1** | Correzione bug: validazione corrispondenza nome classe nel form con nome nel file Java | - |
| **T1** | Correzione bug: gestione corretta percorsi directory per file di coverage quando manca cartella TestReport/ | - |
| **T1** | Correzione bug: eliminazione duplici chiamate a T7 e T8 controllando `robotType` | - |
| **T1** | Aggiornamento DockerFile: sostituzione keyword `expose` con `ports` per compatibilità | - |
| **T7** | Aggiunta modalità debug attivabile tramite variabile d'ambiente per preservare file temporanei | - |
| **UI Gateway/FE** | Implementazione modal personalizzato per visualizzazione errori dettagliati | - |
| **UI Gateway/FE** | Reset automatico del form HTML dopo upload completato con successo | - |
| **UI Gateway/FE** | Validazione lato client con limiti dinamici (lunghezza campi passati da BE) | - |

### Dettaglio validazioni input implementate

| Campo Input | Validazione | Condizioni |
|-------------|-------------|------------|
| className | FE & BE | Non vuoto; lunghezza 1-30 caratteri; corrisponde al nome nel file .java (case-insensitive) |
| classUTFile | FE & BE | Presente; non vuoto; leggibile; contiene dichiarazione classe |
| robotTestsZip | FE & BE | Presente; non vuoto; estraibile; contiene cartelle robot-group |
| difficulty | BE | Uno tra {Beginner, Intermediate, Advanced} |
| description | FE & BE | Se presente: lunghezza ≤ 150 caratteri |
| category | FE & BE | Se presente: massimo 3 categorie; ogni categoria ≤ 30 caratteri |

## 2) Integrazioni effettuate con altri gruppi

Nessuna integrazione diretta con altri gruppi è stata necessaria per questo task. Il lavoro si è concentrato sul miglioramento della robustezza di funzionalità esistenti nel microservizio T1 e sulle sue interazioni con T7 e T8.

## 3) Errori/problematiche non risolte nel progetto consegnato

### Limitazioni nei microservizi T7 e T8

I microservizi **T7** (Evosuite) e **T8** (Jacoco) presentano ancora margini di miglioramento:
- **Log poco dettagliati:** I messaggi di log restituiti da T7 e T8 sono talvolta ambigui e non forniscono informazioni sufficienti per il debug
- **Codici HTTP inconsistenti:** In alcune situazioni di errore, T7 e T8 restituiscono codici HTTP 20x (successo) anche quando l'operazione non è andata a buon fine, rendendo difficile distinguere tra successo e fallimento

**Motivazione:** Questi microservizi sono esterni al perimetro principale del task R1 e richiederebbero un refactoring più approfondito che esula dagli obiettivi prefissati. Si raccomanda di affrontare queste problematiche in un task dedicato futuro.

---

**Repository del progetto:** https://github.com/sabinogb/R1-task/tree/D13_TaskR1R2_UploadRobot_CaricamentoSuggerimenti_MigrazioneMongoDB

**Membri del team:**
- Sabino Gabriele Breglia (s.breglia@studenti.unina.it)
- Marcello Esposito (marcello.esposito6@studenti.unina.it)