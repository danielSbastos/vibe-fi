package service;

import dao.*;
import model.*;
import spark.Request;
import spark.Response;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

import java.util.Map;

public class VibeService {

    private VibeDAO vibeDAO;
    private VibeSeedDAO vibeSeedDAO;
    private static final String SPOTIFY_API = "https://api.spotify.com/v1/";

    public VibeService() {
        vibeDAO = new VibeDAO();
        vibeSeedDAO = new VibeSeedDAO();
    }

    public Object add(Request request, Response response) {
        String name = request.queryParams("name");
        String id = request.queryParams("id");
        String description = request.queryParams("description");
        String userId = request.queryParams("userId");
        String originTemplateId = request.queryParams("originTemplateId");
        Vibe vibe = new Vibe(userId, originTemplateId, name, description);

        if (vibeDAO.createVibe(vibe)) {
            response.status(201);
        } else {
            response.status(500);
        }
        return id;
    }

    public Object get(Request request, Response response) {
        String id = (request.params(":id"));

        Vibe vibe = (Vibe) vibeDAO.getVibe(id);

        if (vibe != null) {
            response.header("Content-Type", "application/json");
            response.header("Content-Encoding", "UTF-8");

            return parseVibe(vibe, vibeSeedDAO.getVibeSeedsByVibe(vibe.getId()));
        } else {
            response.status(404); // 404 Not found
            return "User " + id + " nao encontrado.";
        }

    }

    public Object remove(Request request, Response response) {
        String id = (request.params(":id"));

        Vibe vibe = (Vibe) vibeDAO.getVibe(id);

        if (vibe != null) {
            VibeSeedDAO vibeSeedDAO = new VibeSeedDAO();
            vibeSeedDAO.deleteAllVibeSeedFromVibe(vibe.getId());
            vibeDAO.deleteTemplate(vibe.getId());

            response.status(200); // success
            return id;
        } else {
            response.status(404); // 404 Not found
            return "vibe nao encontrado.";
        }
    }

    @SuppressWarnings("unchecked")
    public Object getFromUser(Request request, Response response) {
        String id = (request.params(":userId"));
        JSONArray resp = new JSONArray();
        Vibe[] vibe = (Vibe[]) vibeDAO.getUserVibes(id);

        if (vibe != null) {
            response.header("Content-Type", "application/json");
            response.header("Content-Encoding", "UTF-8");

            for (int i = 0; i < vibe.length; i++) {
                resp.add(parseVibe(vibe[i]));
            }

            return resp;
        } else {
            response.status(404); // 404 Not found
            return "User " + id + " nao encontrado.";
        }

    }

    public Object update(Request request, Response response) {
        String id = (request.params(":id"));
        
        Vibe vibe = (Vibe) vibeDAO.getVibe(id);
        JSONObject requestBody = (JSONObject) JSONValue.parse(request.body());

        if (vibe != null) {
            vibe.setName((String) requestBody.get("name"));
            vibe.setUserId((String) requestBody.get("userId"));
            vibe.setOriginTemplateId((String) requestBody.get("originTemplateId"));
            vibe.setDescription((String) requestBody.get("description"));

            // System.out.println(request.queryParams("minFeatures[tempo]") == "" ? 0 :
            // request.queryParams("minFeatures[tempo]"));
            JSONObject features = (JSONObject) requestBody.get("features");

            vibe.getFeatures().setPopularity(features.get("popularity") == null ? null
                    : Integer.valueOf(String.valueOf(features.get("popularity"))));
            vibe.getFeatures().setTempo(
                    features.get("tempo") == null ? null : Double.valueOf(String.valueOf(features.get("tempo"))));
            vibe.getFeatures().setValence(
                    features.get("valence") == null ? null : Double.valueOf(String.valueOf(features.get("valence"))));
            vibe.getFeatures().setLiveness(
                    features.get("liveness") == null ? null : Double.valueOf(String.valueOf(features.get("liveness"))));
            vibe.getFeatures().setAcousticness(features.get("acousticness") == null ? null
                    : Double.valueOf(String.valueOf(features.get("acousticness"))));
            vibe.getFeatures().setDanceability(features.get("danceability") == null ? null
                    : Double.valueOf(String.valueOf(features.get("danceability"))));
            vibe.getFeatures().setEnergy(
                    features.get("energy") == null ? null : Double.valueOf(String.valueOf(features.get("energy"))));
            vibe.getFeatures().setSpeechiness(features.get("speechiness") == null ? null
                    : Double.valueOf(String.valueOf(features.get("speechiness"))));
            vibe.getFeatures().setInstrumentalness(features.get("instrumentalness") == null ? null
                    : Double.valueOf(String.valueOf(features.get("instrumentalness"))));

            vibeDAO.updateVibe(vibe);

            return parseVibe(vibe);
        } else {
            response.status(404); // 404 Not found
            return "Produto nao encontrado.";
        }

    }

    private String getRecommendationQuery(VibeSeed[] seeds, Features features, int limit) {
        String query = "?limit=" + Integer.toString(limit);
        String tracks = "";
        String artists = "";
        String genres = "";

        if (seeds != null) {
            for (int i = 0; i < seeds.length; i++) {
                if (seeds[i].getType().equals("track")) {
                    tracks += tracks.length() == 0 ? "" : ",";
                    tracks += seeds[i].getIdentifier();
                } else if (seeds[i].getType().equals("artist")) {
                    artists += artists.length() == 0 ? "" : ",";
                    artists += seeds[i].getIdentifier();
                } else if (seeds[i].getType().equals("genres")) {
                    genres += genres.length() == 0 ? "" : ",";
                    genres += seeds[i].getIdentifier();
                }
            }
        }

        query += tracks.length() > 0 ? "&seed_tracks=" + tracks : "";
        query += artists.length() > 0 ? "&seed_artists=" + artists : "";
        query += genres.length() > 0 ? "&seed_genres=" + genres : "";

        query += features.getPopularity() != null ? "&target_popularity=" + Integer.toString(features.getPopularity())
                : "";
        query += features.getTempo() != null ? "&target_tempo=" + Double.toString(features.getTempo()) : "";
        query += features.getValence() != null ? "&target_valence=" + Double.toString(features.getValence()) : "";
        query += features.getLiveness() != null ? "&target_liveness=" + Double.toString(features.getLiveness()) : "";
        query += features.getAcousticness() != null
                ? "&target_acousticness=" + Double.toString(features.getAcousticness())
                : "";
        query += features.getDanceability() != null
                ? "&target_danceability=" + Double.toString(features.getDanceability())
                : "";
        query += features.getEnergy() != null ? "&target_energy=" + Double.toString(features.getEnergy()) : "";
        query += features.getSpeechiness() != null ? "&target_speechiness=" + Double.toString(features.getSpeechiness())
                : "";
        query += features.getInstrumentalness() != null
                ? "&target_instrumentalness=" + Double.toString(features.getInstrumentalness())
                : "";

        return query;
    }
    
    private HttpRequest userRecommendationRequest(String authorization, VibeSeed[] seeds, Features features,
            int limit) {
        String baseUrl = SPOTIFY_API + "recommendations";
        String query = getRecommendationQuery(seeds, features, limit);
        return HttpRequest.newBuilder().uri(URI.create(baseUrl + query)).headers("Authorization", authorization,
                "Accept", "application/json", "Content-Type", "application/json").GET().build();
    }

    public JSONObject recommend(Request request, Response response) {
        HttpClient client = HttpClient.newHttpClient();
        JSONObject returnJSON = new JSONObject();
        String id = (request.params(":id"));

        Vibe vibe = (Vibe) vibeDAO.getVibe(id);

        try {
            HttpRequest http = userRecommendationRequest(request.headers("Authorization"), 
                    vibeSeedDAO.getVibeSeedsByVibe(vibe.getId()), vibe.getFeatures(), 50);
            HttpResponse<String> spotifyResponse = client.send(http, HttpResponse.BodyHandlers.ofString());

            if (spotifyResponse.statusCode() == 200) {
                returnJSON = (JSONObject) JSONValue.parse(spotifyResponse.body());
            } else {
                returnJSON = (JSONObject) JSONValue.parse(spotifyResponse.body());
            }
        } catch (Exception err) {
            err.printStackTrace();
        }

        return new JSONObject(returnJSON);
    }

    private JSONObject parseVibe(Vibe vibe) {
        Map<String, Object> vibeMap = new HashMap<>();
        Map<String, Object> featureMap = new HashMap<>();

        vibeMap.put("id", vibe.getId());
        vibeMap.put("userId", vibe.getUserId());
        vibeMap.put("originTemplateId", vibe.getOriginTemplateId());
        vibeMap.put("name", vibe.getName());
        vibeMap.put("description", vibe.getDescription());

        featureMap.put("popularity", vibe.getFeatures().getPopularity());
        featureMap.put("tempo", vibe.getFeatures().getTempo());
        featureMap.put("valence", vibe.getFeatures().getValence());
        featureMap.put("liveness", vibe.getFeatures().getLiveness());
        featureMap.put("acousticness", vibe.getFeatures().getAcousticness());
        featureMap.put("danceability", vibe.getFeatures().getDanceability());
        featureMap.put("energy", vibe.getFeatures().getEnergy());
        featureMap.put("speechiness", vibe.getFeatures().getSpeechiness());
        featureMap.put("instrumentalness", vibe.getFeatures().getInstrumentalness());

        vibeMap.put("features", featureMap);

        return new JSONObject(vibeMap);
    }

    @SuppressWarnings("unchecked")
    private JSONObject parseVibe(Vibe vibe, VibeSeed[] seeds) {
        JSONArray vibeSeeds = new JSONArray();

        JSONObject vibeJSON = parseVibe(vibe);

        if (seeds != null) {
            for (int i = 0; i < seeds.length; i++) {
                Map<String, Object> seed = new HashMap<>();

                seed.put("id", seeds[i].getIdentifier());
                seed.put("type", seeds[i].getType());

                vibeSeeds.add(seed);
            }
        }

        vibeJSON.put("seeds", vibeSeeds);

        return vibeJSON;
    }

}
