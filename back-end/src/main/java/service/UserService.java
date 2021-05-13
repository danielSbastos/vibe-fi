package service;

import java.io.IOException;
import dao.*;
import model.*;
import spark.Request;
import spark.Response;

public class UserService {

    private UserDAO userDAO;

    public UserService() {
        userDAO = new UserDAO();
    }

    public Object add(Request request, Response response) {
        String nome = request.queryParams("name");
        String id = request.queryParams("id");
        User user = new User(id, nome);

        if (userDAO.createUser(user)) {
            response.status(201);
        } else {
            response.status(500);
        }
        return id;
    }

    public Object get(Request request, Response response) {
        String id = (request.params(":id"));

        User user = (User) userDAO.getUser(id);

        if (user != null) {
            response.header("Content-Type", "application/json");
            response.header("Content-Encoding", "UTF-8");

            // return "\n<user>\n" + "\t<id>" + user.getId() + "</id>\n" + "\t<nome>" + user.getName() + "</nome>\n"
            //         + "</user>\n";
            return "{\n\"id\":\""+user.getId()+
            "\",\n\"user\":\""+user.getName()+
            "\",\n\"stats\":["+
            "\n\"popularity\":\""+user.getStats().getPopularity()+
            "\",\n\"tempo\":\""+user.getStats().getTempo()+
            "\",\n\"valence\":\""+user.getStats().getValence()+
            "\",\n\"liveness\":\""+user.getStats().getLiveness()+
            "\",\n\"acousticness\":\""+user.getStats().getAcousticness()+
            "\",\n\"danceability\":\""+user.getStats().getDanceability()+
            "\",\n\"energy\":\""+user.getStats().getEnergy()+
            "\",\n\"speechiness\":\""+user.getStats().getSpeechiness()+
            "\",\n\"instrumentalness\":\""+user.getStats().getInstrumentalness()+
            "]\n}";
        } else {
            response.status(404); // 404 Not found
            return "User " + id + " nao encontrado.";
        }

    }

    public Object update(Request request, Response response) {
        String id = (request.params(":id"));
        
		User user = (User) userDAO.getUser(id);

        if (user != null) {
            user.setName(request.queryParams("name"));
            user.getStats().setPopularity(Integer.parseInt(request.queryParams("popularity")));
            user.getStats().setTempo(Double.parseDouble(request.queryParams("tempo")));
            user.getStats().setValence(Double.parseDouble(request.queryParams("valence")));
            user.getStats().setLiveness(Double.parseDouble(request.queryParams("liveness")));
            user.getStats().setAcousticness(Double.parseDouble(request.queryParams("acousticness")));
            user.getStats().setDanceability(Double.parseDouble(request.queryParams("danceability")));
            user.getStats().setEnergy(Double.parseDouble(request.queryParams("energy")));
            user.getStats().setSpeechiness(Double.parseDouble(request.queryParams("speechiness")));
            user.getStats().setInstrumentalness(Double.parseDouble(request.queryParams("instrumentalness")));
        	userDAO.updateUser(user);
        	
            return id;
        } else {
            response.status(404); // 404 Not found
            return "Produto nao encontrado.";
        }

	}

	public Object remove(Request request, Response response) {
        String id = (request.params(":id"));

        User user = (User) userDAO.getUser(id);

        if (user != null) {

            userDAO.deleteUser(user.getId());

            response.status(200); // success
        	return id;
        } else {
            response.status(404); // 404 Not found
            return "User nao encontrado.";
        }
	}
}