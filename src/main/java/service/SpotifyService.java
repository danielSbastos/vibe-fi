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

    public Object getUserTop(Request request, Response response) {
        HttpClient client = HttpClient.newHttpClient();
        String authorization = request.headers("Authorization");
        String timeRange = request.queryParams("timeRange") != null ? request.queryParams("timeRange") : "medium_term";
        int limit = request.queryParams("limit") != null ? Integer.parseInt(request.queryParams("limit")) : 50;

        JSONObject returnJSON = new JSONObject();

        returnJSON = requestTopTracks(client, authorization, timeRange, limit);

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
        returnObj.put("total", total);

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

            features.put("tempo", audioFeatures.get("tempo"));
            features.put("valence", audioFeatures.get("valence"));
            features.put("liveness", audioFeatures.get("liveness"));
            features.put("acousticness", audioFeatures.get("acousticness"));
            features.put("danceability", audioFeatures.get("danceability"));
            features.put("energy", audioFeatures.get("energy"));
            features.put("speechiness", audioFeatures.get("speechiness"));
            features.put("instrumentalness", audioFeatures.get("instrumentalness"));

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
