package com.g2.Game;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Metrics_util {


    /*MODIFICA FUNZIONANTE metodo che localizza i metodi privati della classeUT e li mette in un array di interi */
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
