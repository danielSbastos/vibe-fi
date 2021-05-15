package service;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import spark.Request;
import spark.Response;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

public class AuthService {
    private static final String STATE_KEY = "spotify_auth_state";
    private static final String CLIENT_ID = "3a382d598de845c1a8db261c24be5d63";
    private static final String CLIENT_SECRET = "269ad429b52b47be94781ee6d1949f56";
    private static final String REDIRECT_URI = "http://localhost:8888/callback";

    public Object login(Request request, Response response) {
         String state = "skdhfsidufhsfhuegfuywgefwe";

         String scope = "user-read-private user-read-email";
         String query = "client_id=" + CLIENT_ID + "&scope=" + scope + "&redirect_uri=" + REDIRECT_URI + "&state=" + state + "&response_type=code";

         response.cookie("/", STATE_KEY, state, 5000, false);
         response.redirect("https://accounts.spotify.com/authorize?" + query);

         return "success";
    }

    public Object callback(Request request, Response response) {
        String code = request.queryMap("code").value();

        String rawAuthHeader = CLIENT_ID + ':' + CLIENT_SECRET;
        String encodedString = Base64.getEncoder().encodeToString(rawAuthHeader.getBytes());

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest tokenRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://accounts.spotify.com/api/token"))
                .headers("Authorization", "Basic " + encodedString, "Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString("code="+ code + "&redirect_uri=" + REDIRECT_URI + "&grant_type=authorization_code"))
                .build();
        try {
            HttpResponse<String> tokenResponse = client.send(tokenRequest, HttpResponse.BodyHandlers.ofString());
            if (tokenResponse.statusCode() == 200) {
                Object obj;
                JSONObject jsonObject;

                obj = JSONValue.parse(tokenResponse.body());
                jsonObject = (JSONObject) obj;
                String accessToken = (String) jsonObject.get("access_token");
                String refreshToken = (String) jsonObject.get("refresh_token");
                long expiresIn = (Long) jsonObject.get("expires_in");

                HttpRequest accountRequest = HttpRequest.newBuilder()
                        .uri(URI.create("https://api.spotify.com/v1/me"))
                        .headers("Authorization", "Bearer " + accessToken)
                        .GET()
                        .build();

                HttpResponse<String> accountResponse = client.send(accountRequest, HttpResponse.BodyHandlers.ofString());
                obj = JSONValue.parse(accountResponse.body());
                jsonObject = (JSONObject) obj;

                String userId = (String) jsonObject.get("id");

                response.cookie("user_id", userId);
                response.cookie("access_token", accessToken, (int) expiresIn);
                response.cookie("refresh_token", refreshToken);
            }
        } catch (Exception err) {
            err.printStackTrace();
            return "error";
        }

        return "ok";
    }
}
