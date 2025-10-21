package com.g2.t5;

import com.g2.model.ScalataGiocata;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * Classe legata alla modalità di gioco Scalata, che attualmente è disabilitata in quanto necessita di essere riscritta completamente.
 */
public class ScalataDataWriter {

    private final HttpClient httpClient = HttpClientBuilder.create().build();

    // Void constructor
    public ScalataDataWriter() {

        System.out.println("ScalataDataWriter created.");

    }

    public JSONObject saveScalata(ScalataGiocata scalataGiocata) {

        try {

            // Getting the current date-time in UTC and formats it into a string in the format: '2011-12-03T10:15:30Z'
            // String time = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);

            // Creation of the JSON object
            JSONObject jsonObject = new JSONObject();

            // Filling the JSON object with the data of the ScalataGiocata
            jsonObject.put("playerID", scalataGiocata.getPlayerID());
            jsonObject.put("scalataName", scalataGiocata.getScalataName());
            jsonObject.put("creationTime", scalataGiocata.getCreationTime());
            // jsonObject.put("startingTime", time);
            jsonObject.put("creationDate", scalataGiocata.getCreationDate());

            // POST request to the service in T4
            System.out.println("(ScalataDataWriter) saveScalata sending Json to t4: " + jsonObject);
            HttpPost httpPost = new HttpPost("http://t4-g18-app-1:3000/scalates");
            StringEntity jsonEntity = new StringEntity(jsonObject.toString(), ContentType.APPLICATION_JSON);
            httpPost.setEntity(jsonEntity);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();

            if (statusCode > 299) {
                System.err.println(EntityUtils.toString(httpResponse.getEntity()));
                return null;
            }

            HttpEntity responseEntity = httpResponse.getEntity();
            String responseBody = EntityUtils.toString(responseEntity);
            JSONObject responseObj = new JSONObject(responseBody);

            Integer scalataGameId = responseObj.getInt("id");
            System.out.println("(ScalataDataWriter)[T5] ScalataGiocata successfully created: " + scalataGameId);
            System.out.println("(ScalataDataWriter)[T5] ScalataGiocata successfully created: " + responseObj);

            // TODO: Round setting
            // JSONObject jsonRound = new JSONObject();
            // jsonRound.put("scalataGameId", scalataGameId);

            JSONObject resp = new JSONObject();
            resp.put("scalataGameId", scalataGameId);

            return resp;

        } catch (Exception error) {

            // Error handling
            System.err.println(error);
            return null;
        }


    }

}
