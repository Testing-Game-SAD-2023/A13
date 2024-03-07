package randoop;

import java.io.File;

public class FolderOperations {
    static void clearFolder() {
        File root = new File(".");
        System.out.println(root.getAbsolutePath());
        clearSubfolders(root);
    }

    static void clearSubfolders(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    if (file.getName().contains("dati_di_copertura")) {
                        clearSubfolders(file); // Chiamata ricorsiva per eliminare le sottocartelle
                        deleteFolder(file); // Elimina la cartella corrente
                    } else {
                        clearSubfolders(file); // Chiamata ricorsiva per le sottocartelle senza eliminazione
                    }
                }
            }
        }
    }

    static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteFolder(file); // Chiamata ricorsiva per eliminare le sottocartelle
                } else {
                    file.delete(); // Elimina il file nella cartella corrente
                }
            }
        }
        folder.delete(); // Elimina la cartella corrente
        System.out.println("Cartella eliminata: " + folder.getAbsolutePath());
    }
}
