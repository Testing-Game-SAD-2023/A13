package randoop;

import java.io.*;
import java.util.Random;
import java.lang.ProcessBuilder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/*
 * Versione di RunRandoop riscritta con commento e stampe su terminale per verificare l'andamento dell'esecuzione.
 * I commenti sono stati aggiungi da ChatGPT. La versione originale (legacy/RunRandoop) è mantenuta per legacy.
 */
public class RunRandoop {
    static String zip_ritorno;
    static String xml_ritorno;
    static String cartella_ritorno;
    static Random r;
    static final int LOW = 0;
    static final int HIGH = 230000;

    public static String[] Run(File class_file, int timelimit, int vecchioiter, int iter) {
        // Estrazione del nome della classe senza estensione
        String name = class_file.getName().replace(".java", "");

        // Inizializzazione variabili
        zip_ritorno = null;
        xml_ritorno = null;
        cartella_ritorno = null;
        r = new Random();

        // Generazione seed casuale
        int seed = r.nextInt(HIGH - LOW) + LOW;

        try {
            // Percorsi per output
            zip_ritorno = "./FolderTree/" + name + "/RobotTest/RandoopTest/" + name + "-" + iter + "-dati_di_copertura/" + name + "_Test.zip";
            cartella_ritorno = "./FolderTree/" + name + "/RobotTest/RandoopTest/" + name + "-" + iter + "-dati_di_copertura/" + name + "_Test";
            xml_ritorno = "./FolderTree/" + name + "/RobotTest/RandoopTest/" + name + "-" + iter + "-dati_di_copertura/coveragetot.xml";

            File f = new File(zip_ritorno);

            System.out.println("Avvio processo Randoop per: " + name);

            // Creazione e avvio processo bash
            ExecutorService executor = Executors.newFixedThreadPool(2);
            ProcessBuilder processBuilder = new ProcessBuilder("bash", "robot.sh", name, String.valueOf(timelimit), String.valueOf(iter), String.valueOf(seed), String.valueOf(vecchioiter));
            Process p = processBuilder.start();

            executor.submit(() -> streamGobbler(p.getInputStream(), "OUTPUT"));
            executor.submit(() -> streamGobbler(p.getErrorStream(), "ERROR"));

            p.waitFor();
            // Attesa della generazione del file zip
            while (!f.exists()) {
                System.out.println("Attesa della generazione del file zip");
                Thread.sleep(100);
            }

            // Ulteriore attesa per completare l'operazione
            Thread.sleep(3000);

            executor.shutdown();
            executor.awaitTermination(10, TimeUnit.SECONDS);

            System.out.println("Processo completato per: " + name);

        } catch (IOException | InterruptedException e) {
            System.err.println("Errore durante l'esecuzione di Randoop: " + e.getMessage());
            e.printStackTrace();
        }

        // Restituzione percorsi generati
        return new String[]{zip_ritorno, xml_ritorno, cartella_ritorno};
    }

    private static void streamGobbler (InputStream inputStream, String streamType){
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[" + streamType + "] " + line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
