package service;

import dao.*;
import model.*;
import spark.Request;
import spark.Response;

public class VibeService {

    private VibeDAO vibeDAO;

    public VibeService() {
        vibeDAO = new VibeDAO();
    }

    public Object add(Request request, Response response) {
        String name = request.queryParams("name");
        String id = request.queryParams("id");
        String userId = request.queryParams("userId");
        String originTemplateId = request.queryParams("originTemplateId");
        Vibe vibe = new Vibe(id, userId,originTemplateId, name);

        if (vibeDAO.createVibe(vibe)) {
            response.status(201);
        } else {
            response.status(500);
        }
        return id;
    }

    public Object get(Request request, Response response) {
        String id = (request.params(":id"));

        Vibe vibe = (Vibe) vibeDAO.getVibe(id);

        if (vibe != null) {
            response.header("Content-Type", "application/json");
            response.header("Content-Encoding", "UTF-8");

            return "{\"id\":\""+vibe.getId()+
            "\",\"userId\":\""+vibe.getUserId()+
            "\",\"originTemplateId\":\""+vibe.getOriginTemplateId()+
            "\",\"name\":\""+vibe.getName()+
            "\",\"description\":\""+vibe.getDescription()+
            "\",\"minFeatures\":{"+
            "\"popularity\":"+vibe.getMinFeatures().getPopularity()+
            ",\"tempo\":"+vibe.getMinFeatures().getTempo()+
            ",\"valence\":"+vibe.getMinFeatures().getValence()+
            ",\"liveness\":"+vibe.getMinFeatures().getLiveness()+
            ",\"acousticness\":"+vibe.getMinFeatures().getAcousticness()+
            ",\"danceability\":"+vibe.getMinFeatures().getDanceability()+
            ",\"energy\":"+vibe.getMinFeatures().getEnergy()+
            ",\"speechiness\":"+vibe.getMinFeatures().getSpeechiness()+
            ",\"instrumentalness\":"+vibe.getMinFeatures().getInstrumentalness()+
            "},\"maxFeatures\":{"+
            "\"popularity\":"+vibe.getMaxFeatures().getPopularity()+
            ",\"tempo\":"+vibe.getMaxFeatures().getTempo()+
            ",\"valence\":"+vibe.getMaxFeatures().getValence()+
            ",\"liveness\":"+vibe.getMaxFeatures().getLiveness()+
            ",\"acousticness\":"+vibe.getMaxFeatures().getAcousticness()+
            ",\"danceability\":"+vibe.getMaxFeatures().getDanceability()+
            ",\"energy\":"+vibe.getMaxFeatures().getEnergy()+
            ",\"speechiness\":"+vibe.getMaxFeatures().getSpeechiness()+
            ",\"instrumentalness\":"+vibe.getMaxFeatures().getInstrumentalness()+
            "}}";
        } else {
            response.status(404); // 404 Not found
            return "User " + id + " nao encontrado.";
        }

    }

    public Object remove(Request request, Response response) {
        String id = (request.params(":id"));

        Vibe vibe = (Vibe) vibeDAO.getVibe(id);

        if (vibe != null) {

            vibeDAO.deleteTemplate(vibe.getId());

            response.status(200); // success
        	return id;
        } else {
            response.status(404); // 404 Not found
            return "vibe nao encontrado.";
        }
	}

    public Object getFromUser(Request request, Response response) {
        String id = (request.params(":userId"));
        String resp = "[";
        Vibe[] vibe = (Vibe[]) vibeDAO.getUserVibes(id);

        if (vibe != null) {
            response.header("Content-Type", "application/json");
            response.header("Content-Encoding", "UTF-8");

            for(int i = 0; i <vibe.length; i++){
                resp += "{\"id\":\""+vibe[i].getId()+
                "\",\"userId\":\""+vibe[i].getUserId()+
                "\",\"originTemplateId\":\""+vibe[i].getOriginTemplateId()+
                "\",\"name\":\""+vibe[i].getName()+
                "\",\"description\":\""+vibe[i].getDescription()+
                "\",\"minFeatures\":{"+
                "\"popularity\":"+vibe[i].getMinFeatures().getPopularity()+
                ",\"tempo\":"+vibe[i].getMinFeatures().getTempo()+
                ",\"valence\":"+vibe[i].getMinFeatures().getValence()+
                ",\"liveness\":"+vibe[i].getMinFeatures().getLiveness()+
                ",\"acousticness\":"+vibe[i].getMinFeatures().getAcousticness()+
                ",\"danceability\":"+vibe[i].getMinFeatures().getDanceability()+
                ",\"energy\":"+vibe[i].getMinFeatures().getEnergy()+
                ",\"speechiness\":"+vibe[i].getMinFeatures().getSpeechiness()+
                ",\"instrumentalness\":"+vibe[i].getMinFeatures().getInstrumentalness()+
                "},\"maxFeatures\":{"+
                "\"popularity\":"+vibe[i].getMaxFeatures().getPopularity()+
                ",\"tempo\":"+vibe[i].getMaxFeatures().getTempo()+
                ",\"valence\":"+vibe[i].getMaxFeatures().getValence()+
                ",\"liveness\":"+vibe[i].getMaxFeatures().getLiveness()+
                ",\"acousticness\":"+vibe[i].getMaxFeatures().getAcousticness()+
                ",\"danceability\":"+vibe[i].getMaxFeatures().getDanceability()+
                ",\"energy\":"+vibe[i].getMaxFeatures().getEnergy()+
                ",\"speechiness\":"+vibe[i].getMaxFeatures().getSpeechiness()+
                ",\"instrumentalness\":"+vibe[i].getMaxFeatures().getInstrumentalness()+
                "}"+
                "},";
            }
            resp = resp.substring(0, resp.length() - 1);
            resp += "]";
            return resp;
        } else {
            response.status(404); // 404 Not found
            return "User " + id + " nao encontrado.";
        }

    }

    public Object update(Request request, Response response) {
        String id = (request.params(":id"));
        
		Vibe vibe = (Vibe) vibeDAO.getVibe(id);

        if (vibe != null) {
            vibe.setName(request.queryParams("name"));
            vibe.setUserId(request.queryParams("userId"));
            vibe.setOriginTemplateId(request.queryParams("originTemplateId"));
            vibe.setDescription(request.queryParams("description"));

            System.out.println(request.queryParams("minFeatures[tempo]") == "" ? 0 : request.queryParams("minFeatures[tempo]"));

            vibe.getMinFeatures().setPopularity(Integer.parseInt(request.queryParams("minFeatures[popularity]") == "" ? "0" : request.queryParams("minFeatures[popularity]")));
            vibe.getMinFeatures().setTempo(Double.parseDouble(request.queryParams("minFeatures[tempo]") == "" ? "0" : request.queryParams("minFeatures[tempo]")));
            vibe.getMinFeatures().setValence(Double.parseDouble(request.queryParams("minFeatures[valence]") == "" ? "0" : request.queryParams("minFeatures[valence]")));
            vibe.getMinFeatures().setLiveness(Double.parseDouble(request.queryParams("minFeatures[liveness]") == "" ? "0" : request.queryParams("minFeatures[liveness]")));
            vibe.getMinFeatures().setAcousticness(Double.parseDouble(request.queryParams("minFeatures[acousticness]") == "" ? "0" : request.queryParams("minFeatures[acousticness]")));
            vibe.getMinFeatures().setDanceability(Double.parseDouble(request.queryParams("minFeatures[danceability]") == "" ? "0" : request.queryParams("minFeatures[danceability]")));
            vibe.getMinFeatures().setEnergy(Double.parseDouble(request.queryParams("minFeatures[energy]") == "" ? "0" : request.queryParams("minFeatures[energy]")));
            vibe.getMinFeatures().setSpeechiness(Double.parseDouble(request.queryParams("minFeatures[speechiness]") == "" ? "0" : request.queryParams("minFeatures[speechiness]")));
            vibe.getMinFeatures().setInstrumentalness(Double.parseDouble(request.queryParams("minFeatures[instrumentalness]") == "" ? "0" : request.queryParams("minFeatures[instrumentalness]")));
            vibe.getMaxFeatures().setPopularity(Integer.parseInt(request.queryParams("maxFeatures[popularity]") == "" ? "0" : request.queryParams("maxFeatures[popularity]")));
            vibe.getMaxFeatures().setTempo(Double.parseDouble(request.queryParams("maxFeatures[tempo]") == "" ? "0" : request.queryParams("maxFeatures[tempo]")));
            vibe.getMaxFeatures().setValence(Double.parseDouble(request.queryParams("maxFeatures[valence]") == "" ? "0" : request.queryParams("maxFeatures[valence]")));
            vibe.getMaxFeatures().setLiveness(Double.parseDouble(request.queryParams("maxFeatures[liveness]") == "" ? "0" : request.queryParams("maxFeatures[liveness]")));
            vibe.getMaxFeatures().setAcousticness(Double.parseDouble(request.queryParams("maxFeatures[acousticness]") == "" ? "0" : request.queryParams("maxFeatures[acousticness]")));
            vibe.getMaxFeatures().setDanceability(Double.parseDouble(request.queryParams("maxFeatures[danceability]") == "" ? "0" : request.queryParams("maxFeatures[danceability]")));
            vibe.getMaxFeatures().setEnergy(Double.parseDouble(request.queryParams("maxFeatures[energy]") == "" ? "0" : request.queryParams("maxFeatures[energy]")));
            vibe.getMaxFeatures().setSpeechiness(Double.parseDouble(request.queryParams("maxFeatures[speechiness]") == "" ? "0" : request.queryParams("maxFeatures[speechiness]")));
            vibe.getMaxFeatures().setInstrumentalness(Double.parseDouble(request.queryParams("maxFeatures[instrumentalness]") == "" ? "0" : request.queryParams("maxFeatures[instrumentalness]")));

        	vibeDAO.updateVibe(vibe);
        	
            return id;
        } else {
            response.status(404); // 404 Not found
            return "Produto nao encontrado.";
        }

	}

}