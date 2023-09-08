# T5-G2
## Testing Game
### :desktop_computer: Traccia
Il giocatore (dopo essersi autenticato) avvia una nuova partita del Primo Scenario, l’applicazione gli mostra un elenco di classi da testare ed un elenco di Robot disponibili, il giocatore sceglie la classe ed il Robot contro cui confrontarsi. A questo punto il sistema crea la partita con tutte le scelte fatte, le associa un IdPartita, e la salva. Successivamente l’applicazione avvia l’ambiente di editing in cui visualizza la classe da testare e gli offre una finestra in cui può scrivere la classe di test.
#### :man_technologist: Membri del gruppo
* Alessandro Acerola M63001398
* Giorgio Casolaro M63001404
* Michele De Fazio M63001440
* Lorenzo Esposito M63001417
* Armando Zevola M63001514

#### :clapper: Video Dimostrazione


https://github.com/Testing-Game-SAD-2023/T5-G2/assets/72261684/56415fb2-dcb5-466c-8215-bc50507c9815




### :bulb: Tool e framework
![4373217_java_logo_logos_icon](https://github.com/Testing-Game-SAD-2023/T5-G2/assets/72261684/1a8e4b40-8453-44f5-ae7a-2c4523b29183)
![65687_html_logo_html5_5_five_icon](https://github.com/Testing-Game-SAD-2023/T5-G2/assets/72261684/6aaf2323-475e-474c-888c-4d4324902fb0)
![9055841_bxl_spring_boot_icon](https://github.com/Testing-Game-SAD-2023/T5-G2/assets/72261684/cf720665-8706-4d34-b80e-f3bb7d199e39)
![4373190_docker_logo_logos_icon](https://github.com/Testing-Game-SAD-2023/T5-G2/assets/72261684/37df3b0b-d0c0-43f8-988a-edd0b36425ee)
![icons8-automazione-del-test-di-selenio-64](https://github.com/Testing-Game-SAD-2023/T5-G2/assets/72261684/ce90aed2-f61c-40a8-b02f-69a1a3bb1ab6)
![308436_css_front-end_long shadow_web_web technology_icon](https://github.com/Testing-Game-SAD-2023/T5-G2/assets/72261684/a696f142-71fd-4524-b984-aaf943e91ab9)
![9039990_bootstrap_icon](https://github.com/Testing-Game-SAD-2023/T5-G2/assets/72261684/a00b17bf-7deb-4d4b-9e49-c4de97fbe44c)




### :floppy_disk: Installazione
#### :bangbang: Prerequisiti
* Abilitare WSL (Solo per Windows)
* Docker Engine oppure Docker Desktop
* Maven
  
```bash
cd t5
mvn package
```
Dopo aver creato il file .jar
```bash
cd target
docker build -t test -f test.dockerfile .
docker run -d -p 8080:8080 --name test_container-name test
```
Per visualizzare delle informazioni relative all' applicazione in esecuzione:
```bash
docker ps
```

### :handshake: Integrazione
* T2-3 :point_right: [G1](https://github.com/Testing-Game-SAD-2023/T23-G1.git)
* T6 :point_right: [G12](https://github.com/Testing-Game-SAD-2023/T6-G12.git)

### :clipboard: Documentazione
[Clicca qui](https://github.com/Testing-Game-SAD-2023/T5-G2/blob/main/Documentazione/T5-G2.pdf)

## :white_check_mark: Testing
https://github.com/Testing-Game-SAD-2023/T5-G2/assets/72261684/c87b76b0-1575-483e-9f2d-eee072fa6c72




