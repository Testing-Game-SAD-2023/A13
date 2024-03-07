import interfaces.*;
import randoop.RandoopObserver;
import randoop.RandoopSubject;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        // Creazione del soggetto
        RandoopSubject randoopSubject = new RandoopSubject();

        // Creazione degli osservatori
        RandoopObserver randoopObserver = new RandoopObserver();


        // Registrazione degli osservatori presso il soggetto
        randoopSubject.registerObserver(randoopObserver);

        // Esecuzione del file batch e notifica degli osservatori
        File folder = new File("./FolderTree");
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {

            if (listOfFiles[i].isDirectory()) {
                System.out.println("Directory --> " + listOfFiles[i].getName());

                //VERIFICO CHE NON CI SIANO GIA' LIVELLI
                File level_folder = new File("./FolderTree/"+listOfFiles[i].getName()+"/RobotTest/RandoopTest/01Level");
                if(level_folder.exists() == false) {
                	System.out.println("Generazione test per la classe --> " + listOfFiles[i].getName());
                    File class_file = new File("./FolderTree/"+listOfFiles[i].getName()+"/"+listOfFiles[i].getName()+"SourceCode/"+listOfFiles[i].getName()+".java");
                    
                    randoopSubject.file_testing(class_file);
                }

            }
        }

        // Rimozione degli osservatori dal soggetto
        randoopSubject.unregisterObserver(randoopObserver);
    }
}
