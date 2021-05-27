package app;

import static spark.Spark.*;

import dao.VibeTemplateDAO;
import lib.Classifier;
import model.Features;
import model.VibeTemplate;
import service.*;
import util.cors.*;

public class Application {

    private static UserService userService = new UserService();
    private static VibeService vibeService = new VibeService();
    private static VibeSeedService vibeSeedService = new VibeSeedService();
    private static AuthService authService = new AuthService();
    private static SpotifyService spotifyService = new SpotifyService();
    private static TemplateService templateService = new TemplateService();

    public static void main(String[] args) {
        String systemPort = System.getenv("PORT");
        int port = systemPort != null ? Integer.parseInt(systemPort) : 6789;
        port(port);

        staticFiles.location("/public");

        CorsFilter corsFilter = new CorsFilter();
        corsFilter.apply();

//        Features[] features = new Features[5];
//        features[0] = new Features(1,68.322,0.117,0.11,0.91,0.0999,0.0273,0.0405,0.66); //dormir
//        features[1] = new Features(1,100.036,0.432,0.0754,0.00576,0.954,0.696,0.204,0.000498); // correr
//        features[2] = new Features(1, 147.486,0.246,0.107,0.82,0.536,0.311,0.0288,0.0000165); // triste
//        features[3] = new Features(1,139.898,0.132,0.149,0.131,0.333,0.637,0.0581,0.000018); // feliz
//        features[4] = new Features(1, 109.885,0.052,0.0948,0.99,0.367,0.00888,0.0532,0.914); // calmo
//
//        Classifier classifier = new Classifier(features);
//        System.out.println(classifier.classify());

        get("/userTop", "application/json",
                (request, response) -> spotifyService.getUserTop(request, response));

        get("/vibe/generate","application/json",
                 (request, response) -> vibeService.generate(request, response));

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
        post("/vibe", (request, response) -> vibeService.add(request, response));
        get("/vibe/:id", (request, response) -> vibeService.get(request, response));
        get("/vibe/user/:userId", (request, response) -> vibeService.getFromUser(request, response));
        get("/vibe/update/:id", (request, response) -> vibeService.update(request, response));
        get("/vibe/delete/:id", (request, response) -> vibeService.remove(request, response));

        // Application VibeSeed
        post("/vibeseed", (request, response) -> vibeSeedService.add(request, response));
        get("/vibeseed/:id", (request, response) -> vibeSeedService.get(request, response));
        get("/vibeseed/update/:id", (request, response) -> vibeSeedService.update(request, response));
        get("/vibeseed/delete/:id", (request, response) -> vibeSeedService.remove(request, response));

        // Application VibeTemplate
        get("/vibetemplate/all", (request, response) -> templateService.getDescription(request, response));
    }
}
