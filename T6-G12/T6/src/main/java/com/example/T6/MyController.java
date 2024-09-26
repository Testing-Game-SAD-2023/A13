package com.example.T6;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
//import net.minidev.json.JSONObject;

@CrossOrigin
@Controller
public class MyController {
    private final RestTemplate restTemplate;

    @Autowired
    public MyController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/")
    public String indexPage() {
        return "index";
    }

    //Stefano: questo sostituito 
    @GetMapping("/receiveClassUnderTest")
    public ResponseEntity<String> receiveClassUnderTest(
            @RequestParam("idUtente") String idUtente,
            @RequestParam("nomeCUT") String nomeCUT,
            @RequestParam("robotScelto") String robotScelto,
            @RequestParam("difficolta") String difficolta) {
        try {
            // Esegui la richiesta HTTP al servizio esterno per ottenere il file
            // ClassUnderTest.java
            String url = "http://manvsclass-controller-1:8080/downloadFile/" + nomeCUT;
            byte[] classUnderTest = restTemplate.getForObject(url, byte[].class);

            JSONObject resp = new JSONObject();
            String ut = new String(classUnderTest);
            // Remove BOM Character
            if (ut.startsWith("\uFEFF"))
                ut = ut.substring(1);

            resp.put("class", ut);

            // Restituisci una risposta di successo
            return new ResponseEntity<>(resp.toString(), HttpStatus.OK);
        } catch (Exception e) {
            System.err.println(e);
            // Gestisci eventuali errori e restituisci una risposta di errore
            return new ResponseEntity<>("Errore durante la ricezione del file ClassUnderTest.java",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private final HttpClient httpClient = HttpClientBuilder.create().build();

    // questa è la parte in cui interagiamo con T7
    @PostMapping("/sendInfo") // COMPILA IL CODICE DELL'UTENTE E RESTITUISCE OUTPUT DI COMPILAZIONE CON MVN
    public ResponseEntity<String> handleSendInfoRequest(HttpServletRequest request) {
        try {
            System.out.println("/sendInfo, pippo il player ha cliccato sul bottone 'Compile'");
            HttpPost httpPost = new HttpPost("http://remoteccc-app-1:1234/compile-and-codecoverage");
            JSONObject obj = new JSONObject();
            obj.put("testingClassName", request.getParameter("testingClassName"));
            obj.put("testingClassCode", request.getParameter("testingClassCode"));
            obj.put("underTestClassName", request.getParameter("underTestClassName"));
            obj.put("underTestClassCode", request.getParameter("underTestClassCode"));

            StringEntity jsonEntity = new StringEntity(obj.toString(), ContentType.APPLICATION_JSON);

            httpPost.setEntity(jsonEntity);

            System.out.println(" (/sendInfo) Esecuzione POST presso /compile-and-codecoverage");

            HttpResponse response = httpClient.execute(httpPost);

            System.out.println(" (/sendInfo) POST presso /compile-and-codecoverage eseguita con successo");

            HttpEntity entity = response.getEntity();

            String responseBody = EntityUtils.toString(entity);
            JSONObject responseObj = new JSONObject(responseBody);

            String out_string = responseObj.getString("outCompile");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            // headers.setContentDisposition(ContentDisposition.attachment().filename("index.html").build());

            return new ResponseEntity<>(out_string, headers, HttpStatus.OK);
        } catch (IOException e) {
            System.err.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private byte[] getFileBytes(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.readAllBytes(path);
    }

    // @PostMapping("/getResultXml")
    // public String handleGetResultXmlRequest() {
    // // try {
    // // // Esegui la richiesta HTTP al servizio di destinazione
    // // HttpPost httpPost = new HttpPost("URL_DEL_SERVIZIO_DESTINAZIONE");
    // // JSONObject obj = new JSONObject();

    // // obj.put("testingClassName", request.getParameter("testingClassName"));
    // // obj.put("testingClassCode", request.getParameter("testingClassCode"));
    // // obj.put("underTestClassName", request.getParameter("underTestClassName"));
    // // obj.put("underTestClassCode", request.getParameter("underTestClassCode"));
    // // StringEntity jsonEntity = new StringEntity(obj.toString(),
    // ContentType.APPLICATION_JSON);

    // // httpPost.setEntity(jsonEntity);
    // // HttpResponse targetServiceResponse = httpClient.execute(httpPost);

    // // // Verifica lo stato della risposta
    // // int statusCode = targetServiceResponse.getStatusLine().getStatusCode();
    // // if (statusCode == HttpStatus.OK.value()) {
    // // // Leggi il contenuto del file XML dalla risposta
    // // HttpEntity entity = targetServiceResponse.getEntity();
    // // String compileContent = EntityUtils.toString(entity);

    // // String responseBody = EntityUtils.toString(entity);
    // // JSONObject responseObj = new JSONObject(responseBody);
    // // // Restituisci il contenuto del file XML come risposta al client
    // // return xmlContent;
    // // } else {
    // // // Restituisci un messaggio di errore al client
    // // return "Errore durante il recupero del file XML.";
    // // }
    // // } catch (Exception e) {
    // // // Gestisci eventuali errori e restituisci un messaggio di errore al
    // client
    // // return "Si è verificato un errore durante la richiesta del file XML.";
    // // }
    // }
    // FUNZIONE CHE DOVREBBE RICEVERE I RISULTATI DEI ROBOT

    @PostMapping("/run") // NON ESISTE NESSUN INTERFACCIA VERSO I COMPILATORI DEI ROBOT EVOSUITE E
                         // RANDOOP
    public ResponseEntity<String> runner(HttpServletRequest request) {
        try {
            // Esegui la richiesta HTTP al servizio di destinazione
            // RISULTATI UTENTE VERSO TASK 7
            HttpPost httpPost = new HttpPost("http://remoteccc-app-1:1234/compile-and-codecoverage");
            HttpPut httpPut = new HttpPut();
            String time = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
            double gameScore = 0;
            boolean isWinner = false;
            Enumeration<String> params = request.getParameterNames(); 
            while(params.hasMoreElements()){
                String paramName = params.nextElement();
                System.out.println("(in /run) Request Parameter Name - "+paramName+", Value - "+request.getParameter(paramName));
            }
            JSONObject obj = new JSONObject();
            obj.put("testingClassName", request.getParameter("testingClassName"));
            obj.put("testingClassCode", request.getParameter("testingClassCode"));
            obj.put("underTestClassName", request.getParameter("underTestClassName"));
            obj.put("underTestClassCode", request.getParameter("underTestClassCode"));

            StringEntity jsonEntity = new StringEntity(obj.toString(), ContentType.APPLICATION_JSON);

            httpPost.setEntity(jsonEntity);

            System.out.println("(in /run) esecuzione della richiesta POST presso /compile-and-codecoverage "+ obj);
            HttpResponse response = httpClient.execute(httpPost);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode > 299) {
                System.out.println("Errore in /compile-and-codecoverage");
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            //MODIFICA 25/03
            System.out.println("POST /compile-and-codecoverage, statusCode: "+ statusCode);
            //FINE MODIFICA

            //Recupero della risposta
            System.out.println(" (/run) Recupero della risposta a seguito della POST presso /compile-and-codecoverage");
            HttpEntity entity = response.getEntity();

            String responseBody = EntityUtils.toString(entity);
            JSONObject responseObj = new JSONObject(responseBody);

            String xml_string = responseObj.getString("coverage");
            String outCompile = responseObj.getString("outCompile");

            //Recupero del punteggio dell'utente
            System.out.println(" (/run) Recupero del punteggio del player");
            int userScore = ParseUtil.LineCoverage(xml_string);

            //Invio dei risultati verso il task4
            System.out.println("(/run) Recupero dei risultati prodotti tramite GET verso /robots");

            URIBuilder builder = new URIBuilder("http://t4-g18-app-1:3000/robots");
            builder.setParameter("testClassId", request.getParameter("testClassId"))
                    .setParameter("type", request.getParameter("type"))
                    .setParameter("difficulty", request.getParameter("difficulty"));

            HttpGet get = new HttpGet(builder.build());
            System.out.println("(/run) Esecuzione GET verso /robots");
            response = httpClient.execute(get);
            get.releaseConnection();

            // Verifica lo stato della risposta
            statusCode = response.getStatusLine().getStatusCode();
            if (statusCode > 299) {
                System.out.println("Errore in /robots");
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            //MODIFICA 25/03
            System.out.println("GET /robots, statusCode: " + statusCode);
            //FINE MODIFICA

            // Leggi il contenuto dalla risposta
            entity = response.getEntity();
            responseBody = EntityUtils.toString(entity);
            responseObj = new JSONObject(responseBody);
            System.out.println("(/run) Lettura contenuto della risposta restituita dalla GET presso /robots" + responseObj);

            String score = responseObj.getString("scores");
            Integer roboScore = Integer.parseInt(score);
            int numTurnsPlayed = Integer.parseInt(request.getParameter("order"));

            System.out.println("numTurnsPlayed "+ numTurnsPlayed);
            System.out.println("userScore "+ userScore);
            System.out.println("roboScore "+ roboScore);

            if (roboScore > userScore) {
                System.out.println("roboScore > userScore -> isWinner: "+ isWinner);
                gameScore = Math.round(ParseUtil.calculateScore(userScore, numTurnsPlayed + 1));
            } else {
                System.out.println("userScore > roboScore");
                gameScore = Math.round(ParseUtil.calculateScore(userScore, numTurnsPlayed + 1));
                isWinner = true;
            }

            // conclusione e salvataggio partita

            if (numTurnsPlayed == 0) {

                //Aggiornamento del turno corrente (primo turno) con i risultati appena ottenuti
                httpPut = new HttpPut("http://t4-g18-app-1:3000/turns/" + String.valueOf(request.getParameter("turnId")));

                obj = new JSONObject();
                obj.put("scores", String.valueOf(userScore));

                time = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
                obj.put("closedAt", time);

                jsonEntity = new StringEntity(obj.toString(), ContentType.APPLICATION_JSON);

                httpPut.setEntity(jsonEntity);

                response = httpClient.execute(httpPut);
                httpPut.releaseConnection();

                statusCode = response.getStatusLine().getStatusCode();
                if (statusCode > 299) {
                    System.out.println("Errore durante la PUT verso /turns");
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {

                // Codice per la richiesta POST (creazione nuovo turno per lo specifico player durante round x)
                httpPost = new HttpPost("http://t4-g18-app-1:3000/turns");

                obj = new JSONObject();
                obj.put("roundId", Integer.parseInt(request.getParameter("roundId")));
                obj.put("scores", String.valueOf(userScore));
                obj.put("order", numTurnsPlayed + 1);
                JSONArray playersArray = new JSONArray();
                playersArray.put(request.getParameter("playerId"));
                obj.put("players", playersArray);

                time = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
                obj.put("startedAt", time);
                obj.put("closedAt", time);

                jsonEntity = new StringEntity(obj.toString(), ContentType.APPLICATION_JSON);

                httpPost.setEntity(jsonEntity);
                
                System.out.println("richiesta POST verso /turns");
                response = httpClient.execute(httpPost);
                httpPost.releaseConnection();

                statusCode = response.getStatusLine().getStatusCode();
                System.out.println("POST /turns, statusCode: "+ statusCode);

                if (statusCode > 299) {
                    System.out.println("Errore durante la POST verso /turns");
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

            // chiusura round
            System.out.println("(/run) Aggiornamento e chiusura round...");
            httpPut = new HttpPut("http://t4-g18-app-1:3000/rounds/" + String.valueOf(request.getParameter("roundId")));

            obj = new JSONObject();

            obj.put("closedAt", time);

            jsonEntity = new StringEntity(obj.toString(), ContentType.APPLICATION_JSON);

            httpPut.setEntity(jsonEntity);

            response = httpClient.execute(httpPut);
            httpPut.releaseConnection();

            statusCode = response.getStatusLine().getStatusCode();
            if (statusCode > 299) {
                System.out.println("Errore durante la PUT in /rounds/{id}");
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // chiusura gioco STEFANO QUA TI SEI FERMATO
            System.out.println("(/run) Aggiornamento e chiusura della partita...");
            httpPut = new HttpPut("http://t4-g18-app-1:3000/games/" + String.valueOf(request.getParameter("gameId")));

            obj = new JSONObject();
            obj.put("closedAt", time);
            obj.put("username", request.getParameter("username"));
            obj.put("score", (float) gameScore);
            obj.put("isWinner", isWinner);

            //MODIFICA 25/03: Mancano dei parametri per l'esecuzione della PUT...

            jsonEntity = new StringEntity(obj.toString(), ContentType.APPLICATION_JSON);

            httpPut.setEntity(jsonEntity);

            response = httpClient.execute(httpPut);
            httpPut.releaseConnection();
            statusCode = response.getStatusLine().getStatusCode();
            if (statusCode > 299) {
                System.out.println("Errore durante la PUT in /games/{id}");
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // costruzione risposta verso task5
            System.out.println("Costruzione risposta verso task 5...");
            JSONObject result = new JSONObject();
            result.put("outCompile", outCompile);
            result.put("coverage", xml_string);
            result.put("win", userScore >= roboScore);
            result.put("robotScore", roboScore);
            result.put("score", userScore);
            result.put("gameScore", gameScore);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            return new ResponseEntity<>(result.toString(), headers, HttpStatus.OK);
        } catch (Exception e) {
            // Gestisci eventuali errori e restituisci un messaggio di errore al client
            System.err.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/allenamento")
    public ResponseEntity<String> handleAllenamentoRequest(HttpServletRequest request) {
        try {
            // Esegui la prima richiesta HTTP al servizio di destinazione per ottenere il
            // coverage e outCompile
            System.out.println("POST /allenamento");
            
            HttpPost httpPost = new HttpPost("http://remoteccc-app-1:1234/compile-and-codecoverage");
            JSONObject obj = new JSONObject();
            obj.put("testingClassName", request.getParameter("testingClassName"));
            obj.put("testingClassCode", request.getParameter("testingClassCode"));
            obj.put("underTestClassName", request.getParameter("underTestClassName"));
            obj.put("underTestClassCode", request.getParameter("underTestClassCode"));
            StringEntity jsonEntity = new StringEntity(obj.toString(), ContentType.APPLICATION_JSON);
            httpPost.setEntity(jsonEntity);

            System.out.println("(/allenamento) esecuzione della POST presso /compile-and-codecoverage");

            HttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode > 299) {
                System.out.println("(/allenamento) Errore durante la POST presso /compile-and-codecoverage");
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            System.out.println("(/allenamento) POST eseguita con successo presso /compile-and-coverage, statusCode: " + statusCode);

            HttpEntity entity = response.getEntity();
            String responseBody = EntityUtils.toString(entity);
            JSONObject responseObj = new JSONObject(responseBody);
            String xml_string = responseObj.getString("coverage");
            String outCompile = responseObj.getString("outCompile");
            
            int userScore = ParseUtil.LineCoverage(xml_string);
            System.out.println(" (/allenamento) Punteggio dell'utente, userScore = "+ userScore);

            // Esegui la seconda richiesta GET per ottenere il roboScore
            System.out.print("(/allenamento) Esecuzione GET presso /robots");

            URIBuilder builder = new URIBuilder("http://t4-g18-app-1:3000/robots");
            builder.setParameter("testClassId", request.getParameter("testClassId"))
                    .setParameter("type", request.getParameter("type"))
                    .setParameter("difficulty", request.getParameter("difficulty"));
            HttpGet get = new HttpGet(builder.build());
            response = httpClient.execute(get);
            get.releaseConnection();
            statusCode = response.getStatusLine().getStatusCode();
            if (statusCode > 299) {
                System.out.println("(/allenamento) Errore durante la GET presso /robots");
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            System.out.println("(/allenamento) GET eseguita con successo presso /robots, statusCode: "+ statusCode);

            entity = response.getEntity();
            responseBody = EntityUtils.toString(entity);
            responseObj = new JSONObject(responseBody);
            String score = responseObj.getString("scores");
            Integer roboScore = Integer.parseInt(score);
            System.out.println("(/allenamento) Punteggio del robot, roboScore = "+ roboScore);

            // Costruzione della risposta contenente outCompile, coverage, roboScore e score
            JSONObject result = new JSONObject();
            result.put("outCompile", outCompile);
            result.put("coverage", xml_string);
            result.put("robotScore", roboScore);
            result.put("score", userScore);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            return new ResponseEntity<>(result.toString(), headers, HttpStatus.OK);
        } catch (Exception e) {
            // Gestisci eventuali errori e restituisci un messaggio di errore al client
            System.err.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/calculateScalataFinalScore")
    public ResponseEntity<String> handleCalculateScalataFinalScore(@RequestParam("scalataId") String scalataId) {
        try {
            JSONObject resp = new JSONObject();
            int finalScore = 0;
            System.out.println("calculateScalataFinalScore, scalataId: "+ scalataId);
            // Get scalata with all games played
            URIBuilder builder = new URIBuilder("http://t4-g18-app-1:3000/scalates/" + Integer.parseInt(scalataId));
            HttpGet get = new HttpGet(builder.build());
            HttpResponse response = httpClient.execute(get);
            get.releaseConnection();
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode > 299) {
                System.out.println("Errore durante la GET in /scalates/{id}");
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            HttpEntity entity = response.getEntity();
            String responseBody = EntityUtils.toString(entity);
            JSONObject responseObj = new JSONObject(responseBody);
            JSONArray games = responseObj.getJSONArray("games");
            // for each game make a get to games/{id} and sum the score
            for (int i = 0; i < games.length(); i++) {
                JSONObject game = games.getJSONObject(i);
                int gameId = game.getInt("id");
                builder = new URIBuilder("http://t4-g18-app-1:3000/games/" + gameId);
                get = new HttpGet(builder.build());
                response = httpClient.execute(get);
                get.releaseConnection();
                statusCode = response.getStatusLine().getStatusCode();
                if (statusCode > 299) {
                    System.out.println("Errore durante la GET in /games/{id}");
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
                entity = response.getEntity();
                responseBody = EntityUtils.toString(entity);
                responseObj = new JSONObject(responseBody);
                finalScore += responseObj.getInt("score");
            }
            resp.put("finalScore", finalScore);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>(resp.toString(), headers, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println(e);
            return new ResponseEntity<>("Errore durante il calcolo dello score finale per la scalata con id "+scalataId,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // FUNZIONE CHE DOVREBBE RICEVERE I RISULTATI DEI ROBOT
    // @GetMapping("/getResultRobot")
    // public String handleGetResultRobotRequest() {
    // try {
    // // Esegui la richiesta HTTP al servizio di destinazione
    // HttpGet httpGet = new HttpGet("URL_DEL_SERVIZIO_DESTINAZIONE");

    // HttpResponse targetServiceResponse = httpClient.execute(httpGet);

    // // Verifica lo stato della risposta
    // int statusCode = targetServiceResponse.getStatusLine().getStatusCode();
    // if (statusCode == HttpStatus.OK.value()) {
    // // Leggi il contenuto del file XML dalla risposta
    // HttpEntity entity = targetServiceResponse.getEntity();
    // String xmlContent = EntityUtils.toString(entity);

    // // Restituisci il contenuto del file XML come risposta al client
    // return xmlContent;
    // } else {
    // // Restituisci un messaggio di errore al client
    // return "Errore durante il recupero del file XML.";
    // }
    // } catch (Exception e) {
    // // Gestisci eventuali errori e restituisci un messaggio di errore al client
    // return "Si è verificato un errore durante la richiesta del file XML.";
    // }
    // }

    @PostMapping("/getJaCoCoReport")
    public ResponseEntity<String> getJaCoCoReport(HttpServletRequest request) {
        try {
            HttpPost httpPost = new HttpPost("http://remoteccc-app-1:1234/compile-and-codecoverage");

            JSONObject obj = new JSONObject();

            obj.put("testingClassName", request.getParameter("testingClassName"));
            obj.put("testingClassCode", request.getParameter("testingClassCode"));
            obj.put("underTestClassName", request.getParameter("underTestClassName"));
            obj.put("underTestClassCode", request.getParameter("underTestClassCode"));

            StringEntity jsonEntity = new StringEntity(obj.toString(), ContentType.APPLICATION_JSON);

            httpPost.setEntity(jsonEntity);

            HttpResponse response = httpClient.execute(httpPost);

            if (response.getStatusLine().getStatusCode() > 299) {
                System.err.println("Erorre compilazione");
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            HttpEntity entity = response.getEntity();
            String responseBody = EntityUtils.toString(entity);
            JSONObject responseObj = new JSONObject(responseBody);

            String xml_string = responseObj.getString("coverage");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_XML);
            // headers.setContentDisposition(ContentDisposition.attachment().filename("index.html").build());

            return new ResponseEntity<>(xml_string, headers, HttpStatus.OK);
        } catch (IOException e) {
            System.err.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // @PostMapping("/inviaDatiEFile")
    // public ResponseEntity<String> handleInviaDatiEFileRequest(
    // @RequestParam("idUtente") String idUtente,
    // @RequestParam("idPartita") String idPartita,
    // @RequestParam("idTurno") String idTurno,
    // @RequestParam("robotScelto") String robotScelto,
    // @RequestParam("difficolta") String difficolta,
    // @RequestParam("file") MultipartFile file,
    // @RequestParam("playerTestClass") String playerTestClass) {
    // try {
    // // Creazione di una richiesta HTTP POST al servizio di destinazione
    // HttpPost httpPost = new HttpPost("URL_DEL_SERVIZIO_DESTINAZIONE");// CHIAMA
    // UPDATE TURN TASK4
    // // Creazione del corpo della richiesta multipart
    // MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create()
    // .addTextBody("idUtente", idUtente)
    // .addTextBody("idPartita", idPartita)
    // .addTextBody("idTurno", idTurno)
    // .addTextBody("robotScelto", robotScelto)
    // .addTextBody("difficolta", difficolta)
    // .addTextBody("playerTestClass", playerTestClass); // Aggiungi la classe Java
    // come parte del corpo
    // // della richiesta
    // // .addBinaryBody("file", file.getBytes(),
    // ContentType.APPLICATION_OCTET_STREAM,
    // // file.getOriginalFilename());

    // // Esecuzione della richiesta HTTP al servizio di destinazione
    // HttpResponse targetServiceResponse = httpClient.execute(httpPost);
    // // Restituisci una risposta di successo
    // return ResponseEntity.ok("Dati, file e classe Java inviati con successo");
    // } catch (Exception e) {
    // // Gestisci eventuali errori e restituisci una risposta di errore
    // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    // .body("Errore durante l'invio dei dati, del file e della classe Java");
    // }
    // }
}