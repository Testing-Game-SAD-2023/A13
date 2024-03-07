package com.g2.t5;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.ArrayList;

@Controller
public class FileController {
    private ArrayList<String> Class = new ArrayList<>();
    
public void listFilesInFolder(String folderPath) {
    try {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("file:" + folderPath + "/*");
        for (Resource resource : resources) {
            if (resource.isFile()) {
                //gestisco il nome del file eliminando l'estensione
                String fileName = resource.getFilename();
                int extensionIndex = fileName.lastIndexOf('.');
                if (extensionIndex > 0) {
                    String fileNameWithoutExtension = fileName.substring(0, extensionIndex);
                    //verifico che la classe non sia già stata inserita
                    if (!Class.contains(fileNameWithoutExtension)) {
                        Class.add(fileNameWithoutExtension);
                        System.out.println(fileNameWithoutExtension);
                    }
                }
            }
        }

    } catch (IOException e) {
        e.printStackTrace();
    }
}


     

    public int getClassSize() { return Class.size(); }

    public String getClass(int i) { return Class.get(i); }

}

