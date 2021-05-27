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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Classifier {
    private static final String MODEL_URL = "http://2652d983-01d3-4cde-9313-462c2d1df8f8.eastus2.azurecontainer.io/score";
    private Features[] features;

    public Classifier(Features[] features) {
        this.features = features;
    }

    public List<Map<String, Object>> classify() {
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

        Map<String, Object> hm;
        List<Map<String, Object>> res = new ArrayList<>();
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            List<Map<String, Object>> objs = responseMapBody(response.body());

            Features responsefeature;
            for (Features feature : features) {
                for (Map<String, Object> classifiedObj : objs) {
                    responsefeature = new Features(
                            1,
                            (Double) classifiedObj.get("tempo"),
                            (Double) classifiedObj.get("valence"),
                            (Double) classifiedObj.get("liveness"),
                            (Double) classifiedObj.get("acousticness"),
                            (Double) classifiedObj.get("danceability"),
                            (Double) classifiedObj.get("energy"),
                            (Double) classifiedObj.get("speechiness"),
                            (Double) classifiedObj.get("instrumentalness"));

                    if (responsefeature.equals(feature)) {
                        hm = new HashMap<>();
                        hm.put("class", classifiedObj.get("Scored Labels"));
                        hm.put("feature", feature);
                        res.add(hm);
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return res;
    }

    private List<Map<String, Object>> responseMapBody(String body) {
        Map<String, Object> hm;
        List<Map<String, Object>> res = new ArrayList<>();

        Object obj = JSONValue.parse(body);
        JSONObject jsonObject = (JSONObject) obj;
        JSONArray objs = (JSONArray) jsonObject.get("result");

        for (Object _obj : objs) {
            hm = new HashMap<>();
            for (Object o : ((JSONObject) _obj).keySet()) {
                String key = (String) o;
                hm.put(key, ((JSONObject) _obj).get(key));
            }
            res.add(hm);
        }

        return res;
    }

}
