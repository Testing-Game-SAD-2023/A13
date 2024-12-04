package com.groom.manvsclass.service;
import java.net.http.HttpResponse;
import java.util.List;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.bson.json.JsonObject;
import org.json.JSONArray;
import org.json.JSONObject;
public class RequestToOtherContainer {
    private JSONArray getStudentiTeam(List<String> idsStudents){
        try{
            JSONArray arrayInput = new JSONArray(idsStudents);
            System.out.println("Ricerca degli studenti del team..");
            JSONArray userArray =new JSONArray();

            HttpGet httpGet= new HttpGet("http://t23-g1-app-1:8080/students_list"); //Devo passargli tutto l'array di id
            
            HttpResponse httpResponse = new HttpClient.execute(httpGet);
            int statusCode = httpResponse.getStatusLine().getStatusCode();

             if (statusCode > 299) {
                System.err.println(EntityUtils.toString(httpResponse.getEntity()));
                return null;
            }

            
            HttpEntity responseEntity = httpResponse.getEntity();
            String responseBody = EntityUtils.toString(responseEntity);
            JSONArray responseObj = new JSONArray(responseBody);

            return responseObj;
        }
    }
}
