package service;

import dao.*;
import model.*;
import spark.Request;
import spark.Response;
import util.exceptions.InvalidSeedTypeValueException;

public class VibeSeedService {

    private VibeSeedDAO vibeSeedDAO;

    public VibeSeedService() {
        vibeSeedDAO = new VibeSeedDAO();
    }

    public Object add(Request request, Response response) throws InvalidSeedTypeValueException {
        String vibeId = request.queryParams("vibeId");
        String identifier = request.queryParams("identifier");
        String type = request.queryParams("type");

        VibeSeed vibeSeed = new VibeSeed(vibeId, identifier, type);

        if (vibeSeedDAO.createVibeSeed(vibeSeed)) {
            response.status(201);
        } else {
            response.status(500);
        }
        return identifier;
    }

    public Object get(Request request, Response response) {
        String id = (request.params(":id"));
        String resp = "[";
        VibeSeed[] vibeSeed = (VibeSeed[]) vibeSeedDAO.getVibeSeedsByVibe(id);

        if (vibeSeed != null) {
            response.header("Content-Type", "application/json");
            response.header("Content-Encoding", "UTF-8");

            for(int i = 0; i <vibeSeed.length; i++){
                resp += "{\n\"vibeId\":\""+vibeSeed[i].getVibeId()+
                "\n\"identifier\":\""+vibeSeed[i].getIdentifier()+
                "\n\"type\":\""+vibeSeed[i].getType()+
                "\n},";
            }

            return resp;
        } else {
            response.status(404); // 404 Not found
            return "Vibeseed " + id + " nao encontrado.";
        }

    }
}