package service;

import dao.*;
import model.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import spark.Request;
import spark.Response;
import util.exceptions.InvalidSeedTypeValueException;

import java.util.List;
import java.util.Map;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;

public class VibeSeedService {

    private VibeSeedDAO vibeSeedDAO;
    private VibeDAO vibeDAO;
    private static final String SPOTIFY_API = "https://api.spotify.com/v1/";

    public VibeSeedService() {
        vibeSeedDAO = new VibeSeedDAO();
        vibeDAO = new VibeDAO();
    }

    public Object add(Request request, Response response) throws InvalidSeedTypeValueException, ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(request.body());
        JSONArray array = (JSONArray) obj;

        List<Boolean> statuses = new ArrayList<>();
        boolean status;

        for (Object jsonObject : array) {
            String vibeId = (String) ((JSONObject) jsonObject).get("vibeId");
            String identifier = (String) ((JSONObject) jsonObject).get("identifier");
            String type = (String) ((JSONObject) jsonObject).get("type");

            VibeSeed vibeSeed = new VibeSeed(vibeId, identifier, type);

            status = vibeSeedDAO.createVibeSeed(vibeSeed);
            statuses.add(status);
        }

        if (!statuses.contains(false)) {
            response.status(201);
        } else {
            response.status(500);
        }
        return "ok";
    }

    private JSONObject vibeSeedToJSON(VibeSeed vs) {
        Map<String, Object> seedMap = new HashMap<>();

        seedMap.put("vibeId", vs.getVibeId());
        seedMap.put("identifier", vs.getIdentifier());
        seedMap.put("description", vs.getDescription());
        seedMap.put("type", vs.getType());

        return new JSONObject(seedMap);
    }

    private HttpRequest getSeedsDescriptionRequest(String authorization, String ids, String type) {
        String baseUrl = SPOTIFY_API + type;
        String query = "?ids=" + ids;
        return HttpRequest.newBuilder().uri(URI.create(baseUrl + query)).headers("Authorization", authorization,
                "Accept", "application/json", "Content-Type", "application/json").GET().build();
    }

    private JSONObject getSeedsJSONFromSpotify(String artistIds, String trackIds, String authorization) {
        Map<String, Object> seedsMap = new HashMap<>();
        HttpClient client = HttpClient.newHttpClient();

        if (!artistIds.equals("")) {
            try {
                HttpResponse<String> spotifyResponse = client.send(
                        getSeedsDescriptionRequest(authorization, artistIds, "artists"),
                        HttpResponse.BodyHandlers.ofString());
                if (spotifyResponse.statusCode() == 200) {
                    seedsMap.put("artists", ((JSONObject) JSONValue.parse(spotifyResponse.body())).get("artists"));
                } else {
                    return (JSONObject) JSONValue.parse(spotifyResponse.body());
                }
            } catch (Exception err) {
                err.printStackTrace();
            }
        }

        if (!trackIds.equals("")) {
            try {
                HttpResponse<String> spotifyResponse = client.send(
                        getSeedsDescriptionRequest(authorization, trackIds, "tracks"),
                        HttpResponse.BodyHandlers.ofString());
                if (spotifyResponse.statusCode() == 200) {
                    seedsMap.put("tracks", ((JSONObject) JSONValue.parse(spotifyResponse.body())).get("tracks"));
                } else {
                    return (JSONObject) JSONValue.parse(spotifyResponse.body());
                }
            } catch (Exception err) {
                err.printStackTrace();
            }
        }

        return new JSONObject(seedsMap);
    }

    private VibeSeed[] getSeedDescriptions(VibeSeed[] vibeSeeds, String authorization) {
        String artistIds = "";
        String trackIds = "";

        for (int i = 0; i < vibeSeeds.length; i++) {
            if (vibeSeeds[i].getType().equals("track")) {
                trackIds += trackIds.equals("") ? "" : ",";
                trackIds += vibeSeeds[i].getIdentifier();
            } else if (vibeSeeds[i].getType().equals("artist")) {
                artistIds += artistIds.equals("") ? "" : ",";
                artistIds += vibeSeeds[i].getIdentifier();
            }
        }

        JSONObject seedJSON = getSeedsJSONFromSpotify(artistIds, trackIds, authorization);

        if (seedJSON.get("artists") != null) {
            for (Object artist : (JSONArray) seedJSON.get("artists")) {
                if (artist instanceof JSONObject) {
                    JSONObject artistJSON = (JSONObject) artist;
                    boolean found = false;
                    for (int i = 0; i < vibeSeeds.length && !found; i++) {
                        if (vibeSeeds[i].getVibeId().equals((String) artistJSON.get("id"))) {
                            vibeSeeds[i].setDescription((String) artistJSON.get("name"));
                        }
                    }
                }
            }
        }

        if (seedJSON.get("tracks") != null) {
            for (Object track : (JSONArray) seedJSON.get("tracks")) {
                if (track instanceof JSONObject) {
                    JSONObject trackJSON = (JSONObject) track;
                    boolean found = false;
                    for (int i = 0; i < vibeSeeds.length && !found; i++) {
                        if (vibeSeeds[i].getIdentifier().equals((String) trackJSON.get("id"))) {
                            vibeSeeds[i].setDescription((String) trackJSON.get("name"));
                        }
                    }
                }
            }
        }

        return vibeSeeds;
    }

    @SuppressWarnings("unchecked")
    public Object getSeedsByVibe(Request request, Response response) {
        String id = (request.params(":vibeId"));
        Map<String, Object> returnObj = new HashMap<>();
        String authorization = request.headers("Authorization");
        Vibe vibe = vibeDAO.getVibe(id);
        VibeSeed[] vibeSeed = (VibeSeed[]) vibeSeedDAO.getVibeSeedsByVibe(id);

        if (vibeSeed != null) {
            response.header("Content-Type", "application/json");
            response.header("Content-Encoding", "UTF-8");

            JSONArray seedArray = new JSONArray();

            vibeSeed = getSeedDescriptions(vibeSeed, authorization);

            for (int i = 0; i < vibeSeed.length; i++) {
                seedArray.add(vibeSeedToJSON(vibeSeed[i]));
            }

            returnObj.put("vibeId", vibe.getId());
            returnObj.put("vibeName", vibe.getName());
            returnObj.put("vibeDescription", vibe.getDescription());
            returnObj.put("vibeUser", vibe.getUserId());
            returnObj.put("templateId", vibe.getOriginTemplateId());
            returnObj.put("vibeseeds", seedArray);

            return new JSONObject(returnObj);
        } else {
            response.status(404); // 404 Not found
            return "Vibeseed " + id + " nao encontrado.";
        }

    }

    public String update(Request request, Response response) throws InvalidSeedTypeValueException {
        String vibeId = request.params(":id");
        String oldIdentifier = request.queryParams("oldIdentifier");
        String newIdentifier = request.queryParams("newIdentifier");
        String type = request.queryParams("type");

        VibeSeed vibeSeed = new VibeSeed(vibeId, newIdentifier, type);

        if (vibeId != null) {
            vibeSeedDAO.updateVibeSeed(vibeSeed, oldIdentifier);
            response.status(200); // success
            return vibeSeed.getIdentifier();
        } else {
            response.status(404); // 404 Not found
            return "Produto nao encontrado.";
        }
    }

    public Object remove(Request request, Response response) throws InvalidSeedTypeValueException {
        String id = (request.params(":id"));
        String identifier = request.queryParams("identifier");

        VibeSeed vibeSeed = new VibeSeed(id, identifier, "track");

        if (id != null) {

            vibeSeedDAO.deleteVibeSeed(vibeSeed);

            response.status(200); // success
            return id;
        } else {
            response.status(404); // 404 Not found
            return "User nao encontrado.";
        }
    }

}