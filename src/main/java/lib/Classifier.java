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
import java.util.*;
import java.util.HashSet;

public class Classifier {
    private static final String MODEL_URL = "http://2652d983-01d3-4cde-9313-462c2d1df8f8.eastus2.azurecontainer.io/score";
    private Features[] features;
    public HashSet<String> foundClasses;

    public Classifier(Features[] features) {
        this.features = features;
        this.foundClasses = new HashSet<String>();
    }

    public List<Map<String, Object>> classify() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(MODEL_URL))
                .headers("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody(features)))
                .build();

        List<Map<String, Object>> classifiedFeatures = null;
        try {
            HttpResponse<String> response  = client.send(request, HttpResponse.BodyHandlers.ofString());
            List<Map<String, Object>> classification = responseMapBody(response.body());
            classifiedFeatures = classifiedFeatures(classification);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return classifiedFeatures;
    }

    private List<Map<String, Object>> classifiedFeatures(List<Map<String, Object>> classificationResponse) {
        List<Map<String, Object>> res = new ArrayList<>();
        Map<String, Object> hm;
        Features classifiedFeature;

        for (Features feature : features) {
            for (Map<String, Object> classifiedObj : classificationResponse) {
                classifiedFeature = new Features(
                        1,
                        (Double) classifiedObj.get("tempo"),
                        (Double) classifiedObj.get("valence"),
                        (Double) classifiedObj.get("liveness"),
                        (Double) classifiedObj.get("acousticness"),
                        (Double) classifiedObj.get("danceability"),
                        (Double) classifiedObj.get("energy"),
                        (Double) classifiedObj.get("speechiness"),
                        (Double) classifiedObj.get("instrumentalness"));

                if (classifiedFeature.equals(feature)) {
                    hm = new HashMap<>();
                    hm.put("class", classifiedObj.get("Scored Labels"));
                    hm.put("score", classifiedObj.get("Scored Probabilities_" + classifiedObj.get("Scored Labels")));
                    hm.put("feature", feature);
                    foundClasses.add((String) classifiedObj.get("Scored Labels"));
                    res.add(hm);
                }
            }
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

    private String requestBody(Features[] features) {
        StringBuilder json = new StringBuilder().append("[");
        for (int i = 0; i < (features.length - 1); i++) {
            json.append(features[i].asJsonString()).append(",");
        }
        json.append(features[features.length - 1].asJsonString()).append("]");

        return json.toString();
    }

}
