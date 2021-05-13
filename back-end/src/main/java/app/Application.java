package app;

import static spark.Spark.*;
import service.*;

public class Application {

    private static UserService userService = new UserService();
    private static VibeService vibeService = new VibeService();
    
    public static void main(String[] args) {
        port(6789);
        
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
    }
}
