package service;

import spark.Request;
import spark.Response;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SpotifyService {

    private static final String SPOTIFY_API = "https://api.spotify.com/v1/";

    private HttpRequest userTopRequest(String type, String authorization, String timeRange, int limit) {
        String baseUrl = SPOTIFY_API + "me/top/" + type;
        String query = "?time_range=" + timeRange + "&limit=" + limit;
        return HttpRequest.newBuilder().uri(URI.create(baseUrl + query)).headers("Authorization", authorization,
                "Accept", "application/json", "Content-Type", "application/json").GET().build();
    }

    private HttpRequest getAudioFeaturesRequest(String authorization, String ids) {
        String baseUrl = SPOTIFY_API + "audio-features";
        String query = "?ids=" + ids;
        return HttpRequest.newBuilder().uri(URI.create(baseUrl + query)).headers("Authorization", authorization,
                "Accept", "application/json", "Content-Type", "application/json").GET().build();
    }

    public JSONObject getUserTop(Request request, Response response) {
        String authorization = request.headers("Authorization");
        String timeRange = request.queryParams("timeRange") != null ? request.queryParams("timeRange") : "medium_term";
        int limit = request.queryParams("limit") != null ? Integer.parseInt(request.queryParams("limit")) : 50;

        return getUserTop(authorization, timeRange, limit);
    }

    public JSONObject getUserTop(String authToken) {
        String authorization = "Bearer " + authToken;
        String timeRange = "medium_term";
        int limit = 50;

        return getUserTop(authorization, timeRange, limit);
    }

    public JSONObject getUserTopTracks(String authToken) {
        String authorization = "Bearer " + authToken;
        String timeRange = "medium_term";
        int limit = 50;

        return getUserTopTracks(authorization, timeRange, limit);
    }

    private JSONObject getUserTopTracks(String authorization, String timeRange, int limit) {
        HttpClient client = HttpClient.newHttpClient();

        JSONObject tracksJSON;
        tracksJSON = requestTopTracks(client, authorization, timeRange, limit);

        return tracksJSON;
    }

    private JSONObject getUserTop(String authorization, String timeRange, int limit) {
        HttpClient client = HttpClient.newHttpClient();

        JSONObject returnJSON = new JSONObject();
        JSONObject tracksJSON = new JSONObject();
        JSONObject artistsJSON = new JSONObject();

        tracksJSON = requestTopTracks(client, authorization, timeRange, limit);
        artistsJSON = requestTopArtists(client, authorization, timeRange, limit);

        returnJSON = mergeJSONs(tracksJSON, artistsJSON);

        return returnJSON;
    }

    private String getResponseIds(JSONObject spotifyResponse) {
        String ids = "";

        Iterator<?> responseIterator = ((JSONArray) spotifyResponse.get("items")).iterator();

        while (responseIterator.hasNext()) {
            JSONObject item = (JSONObject) responseIterator.next();
            if (responseIterator.hasNext()) {
                ids += item.get("id") + ",";
            } else {
                ids += item.get("id");
            }
        }

        return ids;
    }

    private JSONObject mergeJSONs(JSONObject tracksJSON, JSONObject artistsJSON) {
        Map<String, Object> returnObj = new HashMap<>();

        returnObj = convertJSONtoMap(tracksJSON);
        returnObj.putAll(convertJSONtoMap(artistsJSON));

        returnObj.put("avgFeatures", calculateUserFeatures(returnObj));

        return new JSONObject(returnObj);
    }

    @SuppressWarnings("unchecked")
    private JSONObject calculateUserFeatures(Map<String, Object> returnObj) {
        Integer popularity = 0;
        Double tempo = 0.0;
        Double valence = 0.0;
        Double liveness = 0.0;
        Double acousticness = 0.0;
        Double danceability = 0.0;
        Double energy = 0.0;
        Double speechiness = 0.0;
        Double instrumentalness = 0.0;

        for (Object track : (JSONArray) returnObj.get("tracks")) {
            JSONObject trackJSON = (JSONObject) track;
            JSONObject trackFeaturesJSON = new JSONObject((Map<String, Object>) trackJSON.get("features"));

            tempo += (Double) (trackFeaturesJSON.get("tempo") == null ? 0 : trackFeaturesJSON.get("tempo"));
            valence += (Double) (trackFeaturesJSON.get("valence") == null ? 0 : trackFeaturesJSON.get("valence"));
            liveness += (Double) (trackFeaturesJSON.get("liveness") == null ? 0 : trackFeaturesJSON.get("liveness"));
            acousticness += (Double) (trackFeaturesJSON.get("acousticness") == null ? 0
                    : trackFeaturesJSON.get("acousticness"));
            danceability += (Double) (trackFeaturesJSON.get("danceability") == null ? 0
                    : trackFeaturesJSON.get("danceability"));
            energy += (Double) (trackFeaturesJSON.get("energy") == null ? 0 : trackFeaturesJSON.get("energy"));
            speechiness += (Double) (trackFeaturesJSON.get("speechiness") == null ? 0
                    : trackFeaturesJSON.get("speechiness"));
            instrumentalness += (Double) (trackFeaturesJSON.get("instrumentalness") == null ? 0
                    : trackFeaturesJSON.get("instrumentalness"));
        }

        for (Object artist : (JSONArray) returnObj.get("artists")) {
            JSONObject artistSON = (JSONObject) artist;

            popularity += (Integer) (artistSON.get("popularity") == null ? 0 : artistSON.get("popularity"));
        }

        popularity = popularity / (Integer) returnObj.get("totalArtists");
        tempo = tempo / (Integer) returnObj.get("totalTracks");
        valence = valence / (Integer) returnObj.get("totalTracks");
        liveness = liveness / (Integer) returnObj.get("totalTracks");
        acousticness = acousticness / (Integer) returnObj.get("totalTracks");
        danceability = danceability / (Integer) returnObj.get("totalTracks");
        energy = energy / (Integer) returnObj.get("totalTracks");
        speechiness = speechiness / (Integer) returnObj.get("totalTracks");
        instrumentalness = instrumentalness / (Integer) returnObj.get("totalTracks");

        Map<String, Object> avgFeatures = new HashMap<>();

        avgFeatures.put("popularity", popularity);
        avgFeatures.put("tempo", tempo);
        avgFeatures.put("valence", valence);
        avgFeatures.put("liveness", liveness);
        avgFeatures.put("acousticness", acousticness);
        avgFeatures.put("danceability", danceability);
        avgFeatures.put("energy", energy);
        avgFeatures.put("speechiness", speechiness);
        avgFeatures.put("instrumentalness", instrumentalness);

        return new JSONObject(avgFeatures);
    }

    private Map<String, Object> convertJSONtoMap(JSONObject jsonObject) {
        Map<String, Object> hm = new HashMap<>();

        for (Object o : jsonObject.keySet()) {
            String key = (String) o;
            hm.put(key, jsonObject.get(key));
        }

        return hm;
    }

    private JSONObject requestTopArtists(HttpClient client, String authorization, String timeRange, int limit) {
        JSONObject returnJSON = new JSONObject();

        try {
            HttpRequest http = userTopRequest("artists", authorization, timeRange, limit);
            HttpResponse<String> spotifyResponse = client.send(http, HttpResponse.BodyHandlers.ofString());

            if (spotifyResponse.statusCode() == 200) {
                JSONObject topArtists = (JSONObject) JSONValue.parse(spotifyResponse.body());
                returnJSON = parseArtists(topArtists);
            } else {
                returnJSON = (JSONObject) JSONValue.parse(spotifyResponse.body());
            }
        } catch (Exception err) {
            err.printStackTrace();
        }

        return new JSONObject(returnJSON);
    }

    @SuppressWarnings("unchecked")
    private JSONObject parseArtists(JSONObject topArtists) {
        Map<String, Object> returnObj = new HashMap<>();

        JSONArray artists = new JSONArray();
        int total = 0;

        for (Object artist : (JSONArray) topArtists.get("items")) {
            if (artist instanceof JSONObject) {
                artists.add(parseArtist((JSONObject) artist));
                
                total++;
            }
        }

        returnObj.put("artists", artists);
        returnObj.put("totalArtists", total);

        return new JSONObject(returnObj);
    }

    private JSONObject parseArtist(JSONObject artist) {
        Map<String, Object> artistMap = new HashMap<>();

        artistMap.put("id", artist.get("id"));
        artistMap.put("name", artist.get("name"));
        artistMap.put("genres", artist.get("genres"));
        artistMap.put("popularity", ((Number) artist.get("popularity")).intValue());

        return new JSONObject(artistMap);
    }

    private JSONObject requestTopTracks(HttpClient client, String authorization, String timeRange, int limit) {
        JSONObject returnJSON = new JSONObject();

        try {
            HttpRequest http = userTopRequest("tracks", authorization, timeRange, limit);
            HttpResponse<String> spotifyResponse = client.send(http, HttpResponse.BodyHandlers.ofString());

            if (spotifyResponse.statusCode() == 200) {
                JSONObject topTracks = (JSONObject) JSONValue.parse(spotifyResponse.body());
                JSONObject trackFeatures = requestTrackFeatures(client, getResponseIds(topTracks), authorization);
                returnJSON = parseTracks(topTracks, trackFeatures);
            } else {
                returnJSON = (JSONObject) JSONValue.parse(spotifyResponse.body());
            }
        } catch (Exception err) {
            err.printStackTrace();
        }

        return new JSONObject(returnJSON);
    }

    private JSONObject requestTrackFeatures(HttpClient client, String ids, String authorization) {
        JSONObject returnJSON = new JSONObject();

        try {
            HttpRequest request = getAudioFeaturesRequest(authorization, ids);
            HttpResponse<String> spotifyResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (spotifyResponse.statusCode() == 200) {
                returnJSON = parseFeatures((JSONObject) JSONValue.parse(spotifyResponse.body()));
            } else {
                returnJSON = (JSONObject) JSONValue.parse(spotifyResponse.body());
            }
        } catch (Exception err) {
            err.printStackTrace();
        }

        return returnJSON;
    }

    @SuppressWarnings("unchecked")
    private JSONObject parseTracks(JSONObject tracksResponse, JSONObject trackFeatures) {
        Map<String, Object> returnObj = new HashMap<>();

        JSONArray tracks = new JSONArray();
        int total = 0;

        for (Object track : (JSONArray) tracksResponse.get("items")) {
            if (track instanceof JSONObject) {
                tracks.add(parseTrack((JSONObject) track, trackFeatures));

                total++;
            }
        }

        returnObj.put("tracks", tracks);
        returnObj.put("totalTracks", total);

        return new JSONObject(returnObj);
    }

    private JSONObject parseTrack(JSONObject track, JSONObject trackFeatures) {
        Map<String, Object> trackMap = new HashMap<>();

        trackMap.put("id", track.get("id"));
        trackMap.put("name", track.get("name"));
        trackMap.put("artists", parseTrackArtists((JSONArray) track.get("artists")));
        trackMap.put("features", trackFeatures.get(track.get("id")));

        return new JSONObject(trackMap);
    }

    private JSONObject parseFeatures(JSONObject audioFeaturesResponse) {
        Map<String, Object> returnObj = new HashMap<>();

        Iterator<?> responseIterator = ((JSONArray) audioFeaturesResponse.get("audio_features")).iterator();

        while (responseIterator.hasNext()) {
            JSONObject audioFeatures = (JSONObject) responseIterator.next();
            Map<String, Object> features = new HashMap<>();

            features.put("tempo", ((Number) audioFeatures.get("tempo")).doubleValue());
            features.put("valence", ((Number) audioFeatures.get("valence")).doubleValue());
            features.put("liveness", ((Number) audioFeatures.get("liveness")).doubleValue());
            features.put("acousticness", ((Number) audioFeatures.get("acousticness")).doubleValue());
            features.put("danceability", ((Number) audioFeatures.get("danceability")).doubleValue());
            features.put("energy", ((Number) audioFeatures.get("energy")).doubleValue());
            features.put("speechiness", ((Number) audioFeatures.get("speechiness")).doubleValue());
            features.put("instrumentalness", ((Number) audioFeatures.get("instrumentalness")).doubleValue());

            returnObj.put((String) audioFeatures.get("id"), features);
        }

        return new JSONObject(returnObj);
    }

    @SuppressWarnings("unchecked")
    private JSONArray parseTrackArtists(JSONArray trackArtists) {
        JSONArray artists = new JSONArray();

        for (Object artist : trackArtists) {
            if (artist instanceof JSONObject) {
                JSONObject artistJSON = (JSONObject) artist;
                Map<String, Object> parsedArtistMap = new HashMap<>();

                parsedArtistMap.put("id", artistJSON.get("id"));
                parsedArtistMap.put("name", artistJSON.get("name"));

                artists.add(new JSONObject(parsedArtistMap));
            }
        }

        return artists;
    }

}
