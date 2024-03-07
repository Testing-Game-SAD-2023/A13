package randoop;

import interfaces.IObserver;
import interfaces.ISubject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static randoop.FolderOperations.*;

public class RandoopSubject implements ISubject {
    private List<IObserver> observers = new ArrayList<>();
    
    private int cov;
    private int ex_cov;
    private int timelimit;
    private static int max_iter = 5;
    private int iter;
    private int sat;
    private static int max_sat = 10;

    private static int maxlevel = 10;
    private int livello;

    @Override
    public void registerObserver(IObserver observer) {
        observers.add(observer);
    }

    @Override
    public void unregisterObserver(IObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (IObserver observer : observers) {
            observer.update();
        }
    }

    public void file_testing(File class_file) throws IOException {

        cov = 0;
        ex_cov = 0;
        timelimit = 5;       
        iter = 0;
        sat = 0;
        livello = 0;

        String[] result = null;

        File file = new File("classes/"+ class_file.getName());

        Path source = Paths.get(class_file.getPath());
        Path destination = Paths.get(file.getPath());

        Files.copy(source, destination,  StandardCopyOption.REPLACE_EXISTING);

        File director = new File("./FolderTree/" + class_file.getName().subSequence(0,class_file.getName().length()-5) + "/RobotTest/RandoopTest/");

        director.mkdir();

        do {


            result = RunRandoop.Run(class_file, timelimit, iter-1, iter);
            notifyObservers();


            cov = EmmaCoverage.LineCoverage(result[1]);
            if(ex_cov >= cov) {
                sat++;

            } else {
                livello++;
                sat = 0;
                System.out.println("new level-->"+" coverage: "+cov);
                File directory_level;
                if(livello >= 10) {
                    directory_level = new File("./FolderTree/" + class_file.getName().subSequence(0,class_file.getName().length()-5) + "/RobotTest/RandoopTest/" + livello +"Level");
                } else {
                    directory_level = new File("./FolderTree/" + class_file.getName().subSequence(0,class_file.getName().length()-5) + "/RobotTest/RandoopTest/" + "0"+livello +"Level");

                }
                directory_level.mkdir();


                for(int i=0; i<=iter; i++) {
                    File sourceFile = new File("./FolderTree/"+class_file.getName().subSequence(0,class_file.getName().length()-5) + "/RobotTest/RandoopTest/"+class_file.getName().subSequence(0,class_file.getName().length()-5)+"-"+i+"-dati_di_copertura/"+class_file.getName().subSequence(0,class_file.getName().length()-5)+"_Test");

                    File[] files = sourceFile.listFiles();

                    if (files != null) {
                        try {
                            Path origine = (new File("./FolderTree/"+class_file.getName().subSequence(0,class_file.getName().length()-5) + "/RobotTest/RandoopTest/"+class_file.getName().subSequence(0,class_file.getName().length()-5)+"-"+i+"-dati_di_copertura/coveragetot.xml")).toPath();
                            Path destinazione = new File(directory_level.getPath(), "coveragetot.xml").toPath();
                            Files.copy(origine, destinazione, StandardCopyOption.REPLACE_EXISTING);
                            System.out.println("File copiato: " + destinazione.toString());
                        } catch (IOException e) {
                            System.out.println("File xml non copiato: " + e.toString());
                            e.printStackTrace();
                        }
                        for (File f : files) {
                            if (f.isFile()) {


                                String nomeFile = f.getName();
                                

                                String nomeSenzaEstensione = nomeFile.substring(0, nomeFile.lastIndexOf('.'));
                                

                                String estensione = nomeFile.substring(nomeFile.lastIndexOf('.'));
                                


                                String nuovoNome = nomeSenzaEstensione + "_it"+iter +"_livello"+ livello + estensione;
                                


                                File nuovoFile = new File(f.getParent(), nuovoNome);
                                

                                f.renameTo(nuovoFile);
                                Path origine = nuovoFile.toPath();
                                

                                Path destinazione = new File(directory_level.getPath(), nuovoNome).toPath();
                                


                                try {
                                    Files.copy(origine, destinazione, StandardCopyOption.REPLACE_EXISTING);
                                    System.out.println("File copiato: " + nuovoNome);
                                } catch (IOException e) {
                                    System.out.println("Errore durante la copia del file: " + nuovoNome);
                                    e.printStackTrace();
                                }

                            }
                        }
                    }
                }

                ex_cov = cov;

            }

            iter++;
            timelimit = timelimit +2;
        }
        while(iter < max_iter && sat < max_sat && livello < maxlevel);


        File classDir = new File("./classes");
        
        FolderOperations.clearFolder();
        File classFiles [] = classDir.listFiles();
        for(File classFile : classFiles) {
        	classFile.delete();
        }
        
    }

}
