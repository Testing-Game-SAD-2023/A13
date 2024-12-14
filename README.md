# Gruppo B8 Task-1:
D'Avino Matteo, Fienga Luigi e Roscigno Andrea

# Modifiche Apportate:
1. Modulo T23: 
    - Modifiche ai model:
        - `User.java` aggiunto i campi di:
          - `List<User> following` [Logica Follow]
          - `List<User> followers` [Logica Follow]
          - `missionToken`
          - `biography`
    - Modifiche ai repository:
      - `UserRepository.java` aggiunto il metodo:
        - `existsFollowRelationship`
    - Aggiunto al `Controller.java` i metodi/route:
        - `addFollow`
        - `rmFollow`
        - `searchPlayer`
        - `modifyPlayer`
    - Aggiunto a `EmailService.java` i metodi:
        - `sendMailUpdate`
        - `sendMailPassword`
2. Ui Gateway:
    - Aggiunto addFollow, rmFollow, searchPlayer, modifyPlayer a riga 51 di default.conf
    - Aggiunto profile/modifyUser, profile/searchPlayer, profile/addFollow e profile/rmFollow a riga 58 di default.conf
3. Modulo T5:
    - Aggiunto il model:
      - `Ratio.java`
    - Cambi nei test:
      - `T23ServiceTest.java` [Model di User]
    - Modifiche a:
      - `User.java`, rispecchiando il model di T23
    - Modifiche a Controller `GuiController.java`:
      - Cambiata `/profile/{playerID}`, per gestire correttamente il prelievo degli utenti e se renderizzare pagina personale o pagina followed
      - Aggiunta le route `/profile/modifyUser`
      - Aggiunta le route `/profile/searchPlayer`
      - Aggiunta le route `/profile/addFollow`
      - Aggiunta le route `/profile/rmFollow`
    - Mofiche/Aggiunte front-end:
      - `profile.html`
      - `profile.js`:
        - `populateForm`
        - `updateUserObject`
        - `viewCompletedAchievements`
        - `viewAllAchievements`
        - `toggleAchievements`
        - `saveChanges`
        - `search`
        - `renderSearchResults`
        - `isUserFollowing`
        - `toggleFollow`
        - `selectRobotFilter`
        - `selectGameModeFilter`
        - `filterStatistics`
        - `updateRatioVisibility`
        - `clearFilter`
      - `profile_followed.html`
    - Aggiunto il service `UserService.java` con i metodi:
      - `getUserbyID`
      - `isUserInFollower`
      - `getAuthenticated`
      - `modifyUser`
      - `searchPlayer`
      - `addFollow`
      - `rmFollow`
    - Aggiunto ad `AchivmentService.java` con i metodi:
      - `calculateRatiosForPlayer`
      - `getStatisticsByPlayer` [MODIFICATO]
    - Aggiunto al `T23Service.java` i metodi:
      - `GetUserbyID`
      - `ModifyUser`
      - `SearchPlayer`
      - `AddFollow`
      - `RmFollow`

# Come Compilare:
1. Creare un file .bat contente le righe da `echo "Installing Txxxx"` fino a `exit /b 0`

2. Chiudere il container associato per quella modifica

3. Eseguire effettivamente il file .bat con il docker aperto (Non è necessario cancellare il vecchio container)

4. Aspettare il completamento e vedere se il container parte, se non parte controllare il possibile errore di compilazione a terminale e fare le modifche

Nel caso in cui qualcosa non sia andato a buon fine reinstalla tutto (Tutto sia riferito alla sola cancellazione del container modificato e la sua reinstallazione, o mal che vada reinstalla l'applicativo)
