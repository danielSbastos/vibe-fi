package service;

import spark.Request;
import spark.Response;

public class AuthService {
     public Object login(Request request, Response response) {
        String state = "skdhfsidufhsfhuegfuywgefwe";

        response.cookie("spotify_auth_state", state);
        String client_id = "3a382d598de845c1a8db261c24be5d63";
        String scope = "user-read-private user-read-email";
        String redirect_uri = "http://localhost:6789/callback";
        String query = "client_id=" + client_id + "&scope=" + scope + "&redirect_uri=" + redirect_uri + "&state=" + state + "&response_type=code";

        response.redirect("https://accounts.spotify.com/authorize?" + query);
        return "success";
    }
}
