package service;

import dao.UserDAO;
import model.Features;
import model.User;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import spark.Request;
import spark.Response;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;

import java.util.Map;

public class AuthService {
    private static final String STATE_KEY = "spotify_auth_state";

    private static final String CLIENT_ID = "3a382d598de845c1a8db261c24be5d63";
    private static final String CLIENT_SECRET = "269ad429b52b47be94781ee6d1949f56";

    private static final String HOST = System.getenv("HOST") != null ? "https://" + System.getenv("HOST")
            : "http://localhost:6789";
    private static final String REDIRECT_URL = HOST + "/callback";
    private static final String AUTHORIZE_URL = "https://accounts.spotify.com/authorize";
    private static final String ACCOUNT_URL = "https://api.spotify.com/v1/me";
    private static final String TOKEN_URL = "https://accounts.spotify.com/api/token";

    public Object login(Request request, Response response) {
        String state = "skdhfsidufhsfhuegfuywgefwe";

        String scope = "user-read-private user-read-email user-top-read playlist-modify-public playlist-modify-private";
        String query = "?client_id=" + CLIENT_ID + "&scope=" + scope + "&redirect_uri=" + REDIRECT_URL + "&state="
                + state + "&response_type=code";

        response.cookie("/", STATE_KEY, state, 5000, false);
        response.redirect(AUTHORIZE_URL + query);
        
        return "success";
    }

    public Object refresh(Request request, Response response) {
        String accessToken = request.cookie("access_token");
        if (accessToken == null) {
            String rawAuthHeader = CLIENT_ID + ':' + CLIENT_SECRET;
            String encodedAuth = Base64.getEncoder().encodeToString(rawAuthHeader.getBytes());
            String refreshToken = request.cookie("refresh_token");

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest refreshRequest = HttpRequest
                    .newBuilder()
                    .uri(URI.create(ACCOUNT_URL))
                    .headers("Authorization", "Basic " + encodedAuth, "Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString("refresh_token=" + refreshToken + "&grant_type=refresh_token"))
                    .build();
            try {
                HttpResponse<String> refreshResponse = client.send(refreshRequest, HttpResponse.BodyHandlers.ofString());
                Map<String, Object> body = responseMapBody(refreshResponse.body());
                response.cookie("access_token", (String) body.get("access_token"),
                        Math.toIntExact((Long) body.get("expires_in")));
            } catch (Exception err) {
                err.printStackTrace();
            }
        }
        return "ok";
    }

    public Object callback(Request request, Response response) {
        HttpClient client = HttpClient.newHttpClient();
        int stat = 0;
        
        String code = request.queryMap("code").value();
        HttpRequest tokenRequest = tokenRequest(code);
        try {
            HttpResponse<String> tokenResponse = client.send(tokenRequest, HttpResponse.BodyHandlers.ofString());
            if (tokenResponse.statusCode() == 200) {
                Map<String, Object> tokenBody = responseMapBody(tokenResponse.body());

                HttpRequest accountRequest = accountRequest((String) tokenBody.get("access_token"));
                HttpResponse<String> accountResponse = client.send(accountRequest,
                        HttpResponse.BodyHandlers.ofString());

                if (accountResponse.statusCode() == 200) {
                    JSONObject accountBody = new JSONObject(responseMapBody(accountResponse.body()));

                    stat = createUser(accountBody, (String) tokenBody.get("access_token"));

                    response.cookie("user_id", (String) accountBody.get("id"));
                    response.cookie("access_token", (String) tokenBody.get("access_token"),
                            Math.toIntExact((Long) tokenBody.get("expires_in")));
                    response.cookie("refresh_token", (String) tokenBody.get("refresh_token"));
                }
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
        if (stat == 1) {
            response.redirect("/screens/selecionarvibes/index.html");
        } else
            response.redirect("/screens/perfil/index.html");

        return "ok";
    }

    /*
     * createUser() @return 0 - erro ao criar user 1 - user criado com sucesso 2 -
     * user ja existente 3 - user atualizado
     */
    private int createUser(JSONObject userAccount, String authToken) {
        UserDAO userDAO = new UserDAO();
        SpotifyService spotifyService = new SpotifyService();
        String id = (String) userAccount.get("id");
        String name = (String) userAccount.get("display_name");
        JSONArray images = (JSONArray) userAccount.get("images");
        String imageURL = images.isEmpty() ? null : (String) ((JSONObject) images.get(0)).get("url");

        User user;
        user = userDAO.getUser(id);
        if (user == null) {
            Features uFeatures = parseFeatures((JSONObject) spotifyService.getUserTop(authToken).get("avgFeatures"));
            user = new User(id, name, imageURL, uFeatures);
            if (userDAO.createUser(user)) {
                return 1;
            } else {
                return 0;
            }
        } else if (user.getLastUpdateDate().before(Timestamp.valueOf(LocalDateTime.now().minusDays(2)))) {
            Features uFeatures = parseFeatures((JSONObject) spotifyService.getUserTop(authToken).get("avgFeatures"));
            user.setName(name);
            user.setImageURL(imageURL);
            user.setStats(uFeatures);
            return (userDAO.updateUser(user)) ? 3 : 0;
        }

        return 2;
    }

    private Features parseFeatures(JSONObject userFeatures) {
        return new Features((Integer) userFeatures.get("popularity"), (Double) userFeatures.get("tempo"),
                (Double) userFeatures.get("valence"), (Double) userFeatures.get("liveness"),
                (Double) userFeatures.get("acousticness"), (Double) userFeatures.get("danceability"),
                (Double) userFeatures.get("energy"), (Double) userFeatures.get("speechiness"),
                (Double) userFeatures.get("instrumentalness"));
    }

    private HttpRequest accountRequest(String accessToken) {
        return HttpRequest.newBuilder().uri(URI.create(ACCOUNT_URL)).headers("Authorization", "Bearer " + accessToken)
                .GET().build();
    }

    private HttpRequest tokenRequest(String code) {
        String rawAuthHeader = CLIENT_ID + ':' + CLIENT_SECRET;
        String encodedString = Base64.getEncoder().encodeToString(rawAuthHeader.getBytes());

        return HttpRequest.newBuilder().uri(URI.create(TOKEN_URL))
                .headers("Authorization", "Basic " + encodedString, "Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers
                        .ofString("code=" + code + "&redirect_uri=" + REDIRECT_URL + "&grant_type=authorization_code"))
                .build();
    }

    private Map<String, Object> responseMapBody(String body) {
        Map<String, Object> hm = new HashMap<>();

        Object obj = JSONValue.parse(body);
        JSONObject jsonObject = (JSONObject) obj;

        for (Object o : jsonObject.keySet()) {
            String key = (String) o;
            hm.put(key, jsonObject.get(key));
        }

        return hm;
    }

    public Object logout(Request request, Response response) {
        response.removeCookie("user_id");
        response.removeCookie("refresh_token");
        response.removeCookie("access_token");
        response.redirect("/");
        return null;
    }
}