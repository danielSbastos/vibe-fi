package lib;

import model.Features;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class Classifier {
    private static final String MODEL_URL = "http://2652d983-01d3-4cde-9313-462c2d1df8f8.eastus2.azurecontainer.io/score";
    private Features[] features;

    public Classifier(Features[] features) {
        this.features = features;
    }

    public void classify() {
        HttpClient client = HttpClient.newHttpClient();

        StringBuilder json = new StringBuilder().append("[");
        for (int i = 0; i < (features.length - 1); i++) {
            json.append(features[i].asJsonString()).append(",");
        }
        json.append(features[features.length - 1].asJsonString()).append("]");

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(MODEL_URL))
                .headers("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject obj = new JSONObject(responseMapBody(response.body()));
            System.out.println(obj);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    private Map<String, Object> responseMapBody(String body) {
        Map<String, Object> hm = new HashMap<>();

        Object obj = JSONValue.parse(body);
        JSONObject jsonObject = (JSONObject) obj;
        JSONObject[] objs = (JSONObject[]) jsonObject.get("items");

        for (JSONObject _obj : objs) {
            for (Object o : _obj.keySet()) {
                String key = (String) o;
                hm.put(key, jsonObject.get(key));
            }
        }

        return hm;
    }

}
