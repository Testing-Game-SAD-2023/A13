/*
 *   Copyright (c) 2024 Stefano Marano https://github.com/StefanoMarano80017
 *   All rights reserved.

 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at

 *   http://www.apache.org/licenses/LICENSE-2.0

 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.g2.Game;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.w3c.dom.NodeList;

import com.g2.Interfaces.ServiceManager;

public class Sfida extends GameLogic {

    private int currentTurn;
    private int userScore;
    private int robotScore;
    private int totalTurns = 10;
    private Boolean GameOVer = false;
    private double lines_factor=1;


    //Questa classe si specializza in una partita semplice basata sui turni, prende il nome di Sfida nella UI
    public Sfida(ServiceManager serviceManager, String PlayerID, String ClasseUT,
                                String type_robot, String difficulty, String gamemode) {
        super(serviceManager, PlayerID, ClasseUT, type_robot, difficulty, gamemode);
        currentTurn = 0;
    }

    @Override
    public void playTurn(double userScore, int robotScore) {
        String Time = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
        currentTurn++;
        //CreateTurn(Time, userScore);
        System.out.println("[GAME] Turn " + currentTurn + " played. User Score: " + userScore + ", Robot Score: " + robotScore);
    }

    @Override
    public Boolean isGameEnd() {
        return false; //il giocatore può fare quanti turni vuole quindi ritorno sempre false
    }

    /*MODIFICA Metodo che confronta l'xml di coverage con l'arraylist con le linee dei metodi privati */
    private int countCoveredPrivateMethods(ArrayList<Integer> privateMethodLines, String jacocoXml) throws Exception {
        try {
            System.out.println("Private Method Lines: " + privateMethodLines);
            // Configura DocumentBuilderFactory per ignorare DTD esterni
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setNamespaceAware(false);

            // Disabilita il caricamento di DTD esterni per evitare errori
            try {
                factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            } catch (ParserConfigurationException e) {
                // Alcuni parser potrebbero non supportare questa feature
                System.err.println("Impossibile disabilitare il caricamento di DTD esterni: " + e.getMessage());
            }

            // Crea DocumentBuilder
            DocumentBuilder builder = factory.newDocumentBuilder();
            // Imposta un ErrorHandler che ignora gli errori DTD
            builder.setErrorHandler(new org.xml.sax.helpers.DefaultHandler());

            // Parsing della stringa XML
            InputStream xmlInput = new ByteArrayInputStream(jacocoXml.getBytes("UTF-8"));
            org.w3c.dom.Document document = builder.parse(xmlInput);
            document.getDocumentElement().normalize();

            // Trova tutti gli elementi <line> nel file di coverage
            NodeList lineNodes = document.getElementsByTagName("line");
            Set<Integer> coveredLines = new HashSet<>();

            for (int i = 0; i < lineNodes.getLength(); i++) {
                org.w3c.dom.Element lineElement = (org.w3c.dom.Element) lineNodes.item(i);

                // Verifica che gli attributi "nr" e "ci" esistano
                if (lineElement.hasAttribute("nr") && lineElement.hasAttribute("ci")) {
                    try {
                        int lineNumber = Integer.parseInt(lineElement.getAttribute("nr"));
                        int covered = Integer.parseInt(lineElement.getAttribute("ci"));

                        // Se la riga è coperta, aggiungila all'insieme delle righe coperte
                        if (covered > 0) {
                            coveredLines.add(lineNumber - 1);
                        }
                        System.out.println("Covered Lines: " + coveredLines);
                    } catch (NumberFormatException e) {
                        // Log dell'errore di parsing e continua
                        System.err.println("Errore nel parsing degli attributi nr o ci: " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Errore durante il confronto delle linee dei metodi privati.", e);
        }
        return 0; 
    }

    //MODIFICA restituisce il valore di coverage da 0 a 100
    private int LineCoverage(String cov) {
        try {
            // Parsing del documento XML con Jsoup
            Document doc = Jsoup.parse(cov, "", Parser.xmlParser());
            // Selezione dell'elemento counter di tipo "LINE"
            Element line = doc.selectFirst("report > counter[type=LINE]");
            // Verifica se l'elemento è stato trovato
            if (line == null) {
                throw new IllegalArgumentException("Elemento 'counter' di tipo 'LINE' non trovato nel documento XML.");
            }
            // Lettura degli attributi "covered" e "missed" e calcolo della percentuale di copertura
            int covered = Integer.parseInt(line.attr("covered"));
            int missed = Integer.parseInt(line.attr("missed"));
            // Calcolo della percentuale di copertura
            return 100 * covered / (covered + missed);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Gli attributi 'covered' e 'missed' devono essere numeri interi validi.", e);
        } catch (Exception e) {
            throw new RuntimeException("Errore durante l'elaborazione del documento XML.", e);
        }
    }

/*MODIFICA */
        
    @Override
    public int GetScore(String jacoco_xml,int num_lines,int numPrivateMethods, ArrayList<Integer> privateMethodLines, int cyclomaticComplexity, String difficulty) {

        // Se loc è 0, il punteggio è sempre 0
        int coverage = LineCoverage(jacoco_xml);
        if (coverage == 0) {
            return 0;
        }
        // Penalità crescente per ogni turno aggiuntivo
        double penaltyFactor = Math.pow(0.9, currentTurn);
       
      
        
        
        
        
        /*MODIFICA aggingo un moltiplicatore se le linee di codice coperte superano un certo valore */
        /*Se secgli un robot difficile e la copertura al primo turno supera una certa soglia ottieni un moltiplicatore */

        double difficultyBot_factor= Double.parseDouble(difficulty);

        //per dare una maggiore idea di competitività all'utente
        if (currentTurn==0 && coverage>=75){
            if ( difficultyBot_factor==2){
                lines_factor=1.1;
                }
            else if(difficultyBot_factor==3){
                lines_factor=1.3;
            }

        }

        double privateMethodsFactor=1;
    
        //MODIFICA

        //Se ho un numero maggiore di 0 di numeri privati allora aggiungo un fattore moltiplicativo pari al rapporto tra metodi privati in cui si
        if (numPrivateMethods>0){
            int privateMethodsCovered = 0;
            try {
                privateMethodsCovered = countCoveredPrivateMethods(privateMethodLines, jacoco_xml);
            } catch (Exception ex) {
            }
         privateMethodsFactor = (double) 1 + privateMethodsCovered/numPrivateMethods;
        }
       
       
        /*MODIFICA  considero un fattore statico che tenga conto 
        dellla complessità della classe come complessità ciclomatica, numero di righe totali e numero di metodi private*/
        double difficultyClass=(double) (cyclomaticComplexity+num_lines+numPrivateMethods);

        //do a quest della difficoltà della classe un peso in millessimi
        double difficultyClassFactor=(difficultyClass/1000);

        //considero un valore di difficoltà complessivo come somma della difficoltà del bot e di quella della classe
        double difficulty_factor=difficultyBot_factor+difficultyClassFactor;
        



        /*Qui moltiplico alla percentuale un numero di punti pari alla difficoltà della classe testata e del bot */
        double score_diff=coverage*difficulty_factor;

        /*Qui tengo conto degli eventuali fattori lines_factor e privateMethodsFactor */
        double score_add=(score_diff*lines_factor)*privateMethodsFactor;

        /*Qui tengo conto dell'eventuale fattore di penalizzazione in base al numero di turni */
        double partial_score=score_add*penaltyFactor;
        
            
        int score_finale=Math.toIntExact(Math.round(partial_score));          
            

        

        // Restituisci il punteggio 
        return score_finale;
    }

}
