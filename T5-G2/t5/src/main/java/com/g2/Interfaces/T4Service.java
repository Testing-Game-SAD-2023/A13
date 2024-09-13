package com.g2.Interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.client.RestTemplate;


public class T4Service {
    private final RestService restService;
    private static final String BASE_URL = "http://t4-g18-app-1:3000";

    public T4Service(RestTemplate restTemplate){
        this.restService = new RestService(restTemplate, BASE_URL);
    }

    public List<String> getLevels(String className) {
        List<String> result = new ArrayList<>();
        List<String> robot_type =  List.of("randoop", "evosuite");

        for (int i = 0; i < 11; i++){
            for(String robot_string: robot_type){
                try{
                    // Stefano: Ancora non mi è chiaro perchè fanno così, 
                    // credo che non esista una vera e propria REST API per avere i robots
                    Map<String, String> formData =  new HashMap<>();
                    formData.put("testClassId", className);
                    formData.put("type", robot_string);
                    formData.put("difficulty", String.valueOf(i));
     
                    String response = restService.CallRestGET("/robots", formData, String.class);
                    if (response != null){
                        result.add(String.valueOf(i));
                    }
                }catch(Exception e){
                    System.out.println("Errore getLevels: " + e.getMessage());
                }
            }  
        }
        return result;
    }

}
