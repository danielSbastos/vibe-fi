package service;

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
            return "{\"id\":\""+user.getId()+
            "\",\"user\":\""+user.getName()+
            "\",\"imageURL\":" + (user.getImageURL() == null ? null : ("\""+user.getImageURL()+
            "\""))+",\"stats\":{"+
            "\"popularity\":"+user.getStats().getPopularity()+
            ",\"tempo\":"+user.getStats().getTempo()+
            ",\"valence\":"+user.getStats().getValence()+
            ",\"liveness\":"+user.getStats().getLiveness()+
            ",\"acousticness\":"+user.getStats().getAcousticness()+
            ",\"danceability\":"+user.getStats().getDanceability()+
            ",\"energy\":"+user.getStats().getEnergy()+
            ",\"speechiness\":"+user.getStats().getSpeechiness()+
            ",\"instrumentalness\":"+user.getStats().getInstrumentalness()+
            "}}";
        } else {
            response.status(404); // 404 Not found
            return "User " + id + " no encontrado.";
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