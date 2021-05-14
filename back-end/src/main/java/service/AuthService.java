package service;

import spark.Request;
import spark.Response;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.Base64;

public class AuthService {
    private static final String STATE_KEY = "spotify_auth_state";
    private static final String CLIENT_ID = "3a382d598de845c1a8db261c24be5d63";
    private static final String CLIENT_SECRET = System.getenv("CLIENT_SECRET");

    public Object login(Request request, Response response) {
         String state = "skdhfsidufhsfhuegfuywgefwe";

         String scope = "user-read-private user-read-email";
         String redirect_uri = "http://localhost:8888/callback";
         String query = "client_id=" + CLIENT_ID + "&scope=" + scope + "&redirect_uri=" + redirect_uri + "&state=" + state + "&response_type=code";

         response.cookie("/", STATE_KEY, state, 5000, false);
         response.redirect("https://accounts.spotify.com/authorize?" + query);

         return "success";
    }

    public Object callback(Request request, Response response) {
        String code = request.queryMap("code").value();
        String state = request.queryMap("state").value();
        String storedState = request.cookie(STATE_KEY);

        if (state == null || !state.equals(storedState)) {
            response.redirect("/#?error=state_mismatch");
        } else {
            response.removeCookie("/", STATE_KEY);

            HttpClient client = HttpClient.newHttpClient();

            String rawAuthHeader = CLIENT_ID + ':' + CLIENT_SECRET;
            String encodedString = Base64.getEncoder().encodeToString(rawAuthHeader.getBytes());

            // Build form params
            // >> The body of this POST request must contain the following parameters
            // >> encoded in ´application/x-www-form-urlencoded
            HttpRequest _request = HttpRequest.newBuilder(
                    URI.create("https://accounts.spotify.com/api/token"))
                    .header("Authorization", "Basic: " + encodedString)
                    .build();
        }

        return storedState;
    }
}
