package app;
import static spark.Spark.*;
import service.Service;

public class Application {

    private static Service service = new Service();
    
    public static void main(String[] args) throws Exception {

        port(6789);
        get("/user/:id", (request, response) -> service.get(request, response));
    }
}
