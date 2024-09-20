package com.g2.Interfaces;

import org.springframework.web.client.RestTemplate;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import com.g2.Model.ClassUT;

public class T1Service extends BaseService {

    private static final String BASE_URL = "http://manvsclass-controller-1:8080";

    public T1Service(RestTemplate restTemplate) {
        super(restTemplate, BASE_URL);

        // Registrazione delle azioni
        registerAction("getClasses", new ServiceActionDefinition(
            params -> getClasses()  //Metodo senza argomenti
        ));

        registerAction("getClassUnderTest", new ServiceActionDefinition(
            params -> getClassUnderTest((String) params[0]),
            String.class
        ));
    }

    // Metodi effettivi

    private List<ClassUT> getClasses() {
        try{
            return callRestGET("/home", null, new ParameterizedTypeReference<List<ClassUT>>() {});
        }catch(Exception e){
            throw new IllegalArgumentException("GetClasses fallimento errore:" + e.getMessage());
        }
        
    }

    private String getClassUnderTest(String nomeCUT) {
        try{
            byte[] result = callRestGET("/downloadFile/" + nomeCUT, null, byte[].class);
            return removeBOM(convertToString(result));
        }catch(Exception e){
            throw new IllegalArgumentException("getClassUnderTest fallimento errore:" + e.getMessage());
        }
    }

}