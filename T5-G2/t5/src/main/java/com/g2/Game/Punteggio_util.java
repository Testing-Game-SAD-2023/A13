package com.g2.Game;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.NodeList;

public class Punteggio_util {



    public static ArrayList<Integer> findPrivateMethodLines(String underTestClassCode) {
        // Regex per trovare metodi privati
        String methodRegex = "\\bprivate\\b\\s+(?:static\\s+|final\\s+)?\\S+\\s+\\w+\\s*\\([^\\)]*\\)\\s*\\{";
        Pattern pattern = Pattern.compile(methodRegex);

        // Analisi del codice riga per riga
        String[] lines = underTestClassCode.split("\n");
        ArrayList<Integer> privateLineNumbers = new ArrayList<>();

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            Matcher matcher = pattern.matcher(line);

            // Se trovi un metodo privato, aggiungi il numero della riga (i + 2 perché le righe iniziano da 1 e considero la riga immediatamente successiva alla firma)
            if (matcher.find()) {
                privateLineNumbers.add(i + 2);
            }
        }

        return privateLineNumbers;
    }

    

    /* Metodo che confronta l'xml di coverage con l'arraylist con le linee dei metodi privati */
    public static int countCoveredPrivateMethods(ArrayList<Integer> privateMethodLines, String jacocoXml) throws Exception {
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

    //METODO PER IL CONTEGGIO DEI METODI PRIVATE
     public static int countPrivateMethods(String underTestClassCode) {
        // Regex per trovare metodi privati
        String methodRegex = "\\bprivate\\b\\s+(?:static\\s+|final\\s+|abstract\\s+)?\\S+\\s+\\w+\\s*\\([^\\)]*\\)\\s*\\{";
        Pattern pattern = Pattern.compile(methodRegex);
        Matcher matcher = pattern.matcher(underTestClassCode);

        // Conta il numero di metodi trovati
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }



    

    /*MODIFICA metodo per il calcolo delle linee di codice tot */
    public static  int countCodeLines(String underTestClassCode) {
        // Rimuove i commenti su singola linea
        underTestClassCode = underTestClassCode.replaceAll("//.*", "");
        // Rimuove i commenti multi-linea
        underTestClassCode = underTestClassCode.replaceAll("/\\*.*?\\*/", "");
        // Splitta in righe
        String[] lines = underTestClassCode.split("\\R"); // "\\R" cattura tutti i tipi di newline
        // Conta solo righe non vuote
        int codeLines = 0;
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                codeLines++;
            }
        }
        return codeLines;
    }


    /*MODIFICA funzione per il calcolo della complessità ciclomatica */
    
    public static int calculateCyclomaticComplexity(String underTestClassCode) {
        // Rimuove i commenti in linea (// commento) e i commenti multilinea (/* commento */)
        String cleanedCode = underTestClassCode.replaceAll("//.*", "")  // Rimuove i commenti in linea
                       .replaceAll("/\\*.*?\\*/", "");  // Rimuove i commenti multilinea
    
        // Parole chiave che rappresentano predicati logici
        String[] keywords = {
            "\\bif\\b", "\\belse if\\b", "\\bfor\\b", "\\bwhile\\b",
            "\\bcase\\b", "\\bcatch\\b"
        };
    
        int count = 0;
    
        // Per ogni parola chiave, conta le occorrenze nel codice "pulito"
        for (String keyword : keywords) {
            Pattern pattern = Pattern.compile(keyword);
            Matcher matcher = pattern.matcher(cleanedCode);
            while (matcher.find()) {
                count++;
            }
        }
    
        return count + 1;
    }



}
