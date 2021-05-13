package app;

import static spark.Spark.*;
import service.*;

public class Application {

    private static UserService userService = new UserService();
    
    public static void main(String[] args) {
        port(6789);

        get("/user/:id", (request, response) -> userService.get(request, response));
        get("/user/update/:id", (request, response) -> userService.update(request, response));
        post("/user", (request, response) -> userService.add(request, response));
        get("/user/delete/:id", (request, response) -> userService.remove(request, response));
    }
}
