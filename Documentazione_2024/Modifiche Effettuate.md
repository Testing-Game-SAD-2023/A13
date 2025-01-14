# Modifiche Effettuate

## Modulo T1

### Cambiamenti a `docker-compose.yml`
- Aggiunto `./RobotConfig:/RobotConfig`
- Aggiunto `Volume:/Volume`

### Cambiamenti a `resources`
- Spostato il codice JavaScript di `uploadClasseAndTest.html` nel file `addClassAndTest.js`
- Modificato il codice JavaScript `class.js`
- Cambiamenti ad `application.properties`:
  - Aggiunte nuove proprietà legate al file system:
    - `filesystem.rootPath = /Volume/Root/`
    - `filesystem.classesPath = ${filesystem.rootPath}Classes/`
    - `filesystem.sourceFolder = SourceCode/`
    - `filesystem.testsFolder = Tests/`
    - `config.pathRobot = /RobotConfig/robots.txt`

### Cambiamenti a `model`
- Nuovo model: `Robot.java`
- Aggiunto a `ClassUT.java`:
  - Attributo:
    - `List<Robot> robots`
  - Metodi:
    - `getRobotNames()`
    - `getRobotPath()`

### Spostamenti di cartelle
- Spostata cartella `repository`
- Spostata cartella `util`

### Creazione nuove cartelle
- **`configuration`**:
  - Inserita la classe `PasswordEncoderAdmin.java`
- **`responses`**:
  - Creata la classe `Response.java`
  - Creata la classe `FileResponse.java`
  - Creata la classe `ApiResponse.java`
  - Inserita la classe `FileUploadResponse.java`

### Cambiamenti ai controller
- Creata la classe `ApiController.java`:
  - Creato metodo/route:
    - `getClasses()`
    - `getClass()`
    - `getRobots()`
    - `getRobot()`
    - `setRobot()`
    - `setClass()`
    - `deleteRobot()`
    - `deleteClass()`
    - `setFileSystem()`
    - `deleteFileSystem()`
    - `lock()`
    - `unlock()`
- Cambiamenti a `HomeController.java`:
  - Aggiunto metodo/route: `getRobots()`
  - Metodo/route `uploadTest()` chiama `classService.saveAll()`
  - Metodo/route `eliminaClasse()` chiama `classService.deleteClass()`

### Cambiamenti a `service`
- Creata la classe `ClassService.java`:
  - Creato metodo:
    - `saveAll()`
    - `deleteClass()`
- Creata la classe `FileSystemService.java`:
  - Creato metodo:
    - `saveClass()`
    - `saveTest()`
    - `deleteAll()`
    - `deleteTest()`
    - `createFolder()`
    - `saveFile()`
    - `unzip()`
    - `deleteDirectory()`
    - `rawSaveFile()`
    - `rawCreateFolder()`
    - `deleteRollback()`
    - `existsPath()`
    - `validatePath()`
    - `getOrCreateLock()`
    - `readLock()`
    - `readUnlock()`
    - `writeLock()`
    - `writeUnlock()`
- Creata la classe `ApiService.java`:
  - Creato metodo:
    - `getClasses()`
    - `getClass()`
    - `getRobots()`
    - `getRobot()`
    - `setRobot()`
    - `setClass()`
    - `deleteRobot()`
    - `deleteClass()`
    - `setFileSystem()`
    - `deleteFileSystem()`
    - `lock()`
    - `unlock()`
    - `runThread()`
- Modificata la classe `AdminService.java`:
  - Creato metodo: `getRobots()`

### Cambiamenti a `UI Gateway`
- Modifica al file di configurazione dell’UI Gateway:
  - Aggiunta route `/classes`
  - Aggiunta route `/listofrobots`
  - Aggiunta route `/fileSystem`
  - Aggiunta route `/lock`
  - Aggiunta route `/unlock`

### Varie
- Aggiunto il **Volume condiviso** tra tutti i container
- Aggiunta cartella di mount **RobotConfig** con file di configurazione `robots.txt`

---

## Modulo T23

### Cambiamenti a `docker-compose.yml`
- Aggiunto `Volume:/Volume`

---

## Modulo T4

### Cambiamenti a `docker-compose.yml`
- Aggiunto `Volume:/Volume`

---

## Modulo T5

### Cambiamenti a `docker-compose.yml`
- Aggiunto `Volume:/Volume`

---

## Modulo T6

### Cambiamenti a `docker-compose.yml`
- Aggiunto `Volume:/Volume`

---

## Modulo T7

### Cambiamenti a `docker-compose.yml`
- Aggiunto `Volume:/Volume`

---

## Modulo T8

### Cambiamenti a `docker-compose.yml`
- Aggiunto `Volume:/Volume`

---

## Modulo T9

### Cambiamenti a `docker-compose.yml`
- Aggiunto `Volume:/Volume`

---

## Modifiche agli script di installazione

### `installer.bat`
- Aggiunto il comando `docker volume create Volume`

### `installermac.sh`
- Aggiunto il comando `docker volume create Volume`

### `installer-linux-server.sh`
- Aggiunto il comando `docker volume create Volume`
