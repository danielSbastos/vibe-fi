package app;

import static spark.Spark.*;

import service.*;
import util.cors.*;

import java.util.Arrays;
import java.util.List;

public class Application {
    private static UserService userService = new UserService();
    private static VibeService vibeService = new VibeService();
    private static VibeSeedService vibeSeedService = new VibeSeedService();
    private static AuthService authService = new AuthService();
    private static SpotifyService spotifyService = new SpotifyService();
    private static TemplateService templateService = new TemplateService();
    private static VibeClassificationService vibeClassificationService = new VibeClassificationService();

    public static void main(String[] args) {
        String systemPort = System.getenv("PORT");
        int port = systemPort != null ? Integer.parseInt(systemPort) : 6789;
        port(port);

        staticFiles.location("/public");

        CorsFilter corsFilter = new CorsFilter();
        corsFilter.apply();

        before((request, response) -> {
            List<String> blackListPaths = Arrays.asList("/login", "/callback", "/logout");
            if (!blackListPaths.contains(request.pathInfo())) {
                authService.refresh(request, response);
            }
        });

        get("/userTop", "application/json", (request, response) -> spotifyService.getUserTop(request, response));

        get("/cookies/deleteMissingClasses", (request, response) -> {
            response.removeCookie("/", "missing-classes");
            return 0;
        });

        // Login
        get("/login", (request, response) -> authService.login(request, response));
        get("/callback", (request, response) -> authService.callback(request, response));
        get("/logout", (request, response) -> authService.logout(request, response));

        // Application Users
        get("/user/:id", (request, response) -> userService.get(request, response));
        get("/user/update/:id", (request, response) -> userService.update(request, response));
        post("/user", (request, response) -> userService.add(request, response));
        get("/user/delete/:id", (request, response) -> userService.remove(request, response));

        // Application Vibes
        post("/vibe/generate", (request, response) -> vibeClassificationService.generate(request, response));
        post("/vibe", (request, response) -> vibeService.add(request, response));
        get("/vibe/:id", (request, response) -> vibeService.get(request, response));
        get("/vibe/recommend/:id", (request, response) -> vibeService.recommend(request, response));
        post("/vibe/playlist/:userId", "application/json",
                (request, response) -> vibeService.createPlaylist(request, response));
        get("/vibe/user/:userId", (request, response) -> vibeService.getFromUser(request, response));
        post("/vibe/update/:id", "application/json", (request, response) -> vibeService.update(request, response));
        get("/vibe/delete/:id", (request, response) -> vibeService.remove(request, response));

        // Application VibeSeed
        post("/vibeseed", (request, response) -> vibeSeedService.add(request, response));
        get("/vibeseed/:id", (request, response) -> vibeSeedService.get(request, response));
        get("/vibeseed/update/:id", (request, response) -> vibeSeedService.update(request, response));
        get("/vibeseed/delete/:id", (request, response) -> vibeSeedService.remove(request, response));

        // Application VibeTemplate
        get("/vibetemplate/all", (request, response) -> templateService.getDescription(request, response));
        get("/vibetemplate/:id", (request, response) -> templateService.getTemplateImage(request, response));
    }
}
