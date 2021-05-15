package app;

import static spark.Spark.*;
import service.*;
import util.cors.*;

public class Application {

    private static UserService userService = new UserService();
    private static VibeService vibeService = new VibeService();
    private static VibeSeedService vibeSeedService = new VibeSeedService();
    private static AuthService authService = new AuthService();

    public static void main(String[] args) {
        port(8888);
        CorsFilter corsFilter = new CorsFilter();
        corsFilter.apply();

        //Login
        get("/login", (request, response) -> authService.login(request, response));
        get("/callback", (request, response) -> authService.callback(request, response));

        //Application Users
        get("/user/:id", (request, response) -> userService.get(request, response));
        get("/user/update/:id", (request, response) -> userService.update(request, response));
        post("/user", (request, response) -> userService.add(request, response));
        get("/user/delete/:id", (request, response) -> userService.remove(request, response));

        //Application Vibes
        post("/vibe", (request, response) -> vibeService.add(request, response));
        get("/vibe/:id", (request, response) -> vibeService.get(request, response));
        get("/vibe/user/:userId", (request, response) -> vibeService.getFromUser(request, response));
        get("/vibe/update/:id", (request, response) -> vibeService.update(request, response));
        get("/vibe/delete/:id", (request, response) -> vibeService.remove(request, response));

        //Application VibeSeed
        post("/vibeseed", (request, response) -> vibeSeedService.add(request, response));
        get("/vibeseed/:id", (request, response) -> vibeSeedService.get(request, response));
        get("/vibeseed/update/:id", (request, response) -> vibeSeedService.update(request, response));
        get("/vibeseed/delete/:id", (request, response) -> vibeSeedService.remove(request, response));
    }
}
