package service;

import java.io.IOException;
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

            return "{\n\"id\":\""+vibe.getId()+
            "\",\n\"userId\":\""+vibe.getUserId()+
            "\",\n\"originTemplateId\":\""+vibe.getOriginTemplateId()+
            "\",\n\"name\":\""+vibe.getName()+
            "\",\n\"description\":\""+vibe.getDescription()+
            "\",\n\"minFeatures\":["+
            "\n\"popularity\":\""+vibe.getMinFeatures().getPopularity()+
            "\",\n\"tempo\":\""+vibe.getMinFeatures().getTempo()+
            "\",\n\"valence\":\""+vibe.getMinFeatures().getValence()+
            "\",\n\"liveness\":\""+vibe.getMinFeatures().getLiveness()+
            "\",\n\"acousticness\":\""+vibe.getMinFeatures().getAcousticness()+
            "\",\n\"danceability\":\""+vibe.getMinFeatures().getDanceability()+
            "\",\n\"energy\":\""+vibe.getMinFeatures().getEnergy()+
            "\",\n\"speechiness\":\""+vibe.getMinFeatures().getSpeechiness()+
            "\",\n\"instrumentalness\":\""+vibe.getMinFeatures().getInstrumentalness()+
            "\"\n],\n\"maxFeatures\":["+
            "\n\"popularity\":\""+vibe.getMaxFeatures().getPopularity()+
            "\",\n\"tempo\":\""+vibe.getMaxFeatures().getTempo()+
            "\",\n\"valence\":\""+vibe.getMaxFeatures().getValence()+
            "\",\n\"liveness\":\""+vibe.getMaxFeatures().getLiveness()+
            "\",\n\"acousticness\":\""+vibe.getMaxFeatures().getAcousticness()+
            "\",\n\"danceability\":\""+vibe.getMaxFeatures().getDanceability()+
            "\",\n\"energy\":\""+vibe.getMaxFeatures().getEnergy()+
            "\",\n\"speechiness\":\""+vibe.getMaxFeatures().getSpeechiness()+
            "\",\n\"instrumentalness\":\""+vibe.getMaxFeatures().getInstrumentalness()+
            "\"\n]"+
            "\n}";
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
                resp += "{\n\"id\":\""+vibe[i].getId()+
                "\",\n\"userId\":\""+vibe[i].getUserId()+
                "\",\n\"originTemplateId\":\""+vibe[i].getOriginTemplateId()+
                "\",\n\"name\":\""+vibe[i].getName()+
                "\",\n\"description\":\""+vibe[i].getDescription()+
                "\",\n\"minFeatures\":["+
                "\n\"popularity\":\""+vibe[i].getMinFeatures().getPopularity()+
                "\",\n\"tempo\":\""+vibe[i].getMinFeatures().getTempo()+
                "\",\n\"valence\":\""+vibe[i].getMinFeatures().getValence()+
                "\",\n\"liveness\":\""+vibe[i].getMinFeatures().getLiveness()+
                "\",\n\"acousticness\":\""+vibe[i].getMinFeatures().getAcousticness()+
                "\",\n\"danceability\":\""+vibe[i].getMinFeatures().getDanceability()+
                "\",\n\"energy\":\""+vibe[i].getMinFeatures().getEnergy()+
                "\",\n\"speechiness\":\""+vibe[i].getMinFeatures().getSpeechiness()+
                "\",\n\"instrumentalness\":\""+vibe[i].getMinFeatures().getInstrumentalness()+
                "\"\n],\n\"maxFeatures\":["+
                "\n\"popularity\":\""+vibe[i].getMaxFeatures().getPopularity()+
                "\",\n\"tempo\":\""+vibe[i].getMaxFeatures().getTempo()+
                "\",\n\"valence\":\""+vibe[i].getMaxFeatures().getValence()+
                "\",\n\"liveness\":\""+vibe[i].getMaxFeatures().getLiveness()+
                "\",\n\"acousticness\":\""+vibe[i].getMaxFeatures().getAcousticness()+
                "\",\n\"danceability\":\""+vibe[i].getMaxFeatures().getDanceability()+
                "\",\n\"energy\":\""+vibe[i].getMaxFeatures().getEnergy()+
                "\",\n\"speechiness\":\""+vibe[i].getMaxFeatures().getSpeechiness()+
                "\",\n\"instrumentalness\":\""+vibe[i].getMaxFeatures().getInstrumentalness()+
                "\"\n]"+
                "\n},";
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

            vibe.getMinFeatures().setPopularity(Integer.parseInt(request.queryParams("popularity")));
            vibe.getMinFeatures().setTempo(Double.parseDouble(request.queryParams("tempo")));
            vibe.getMinFeatures().setValence(Double.parseDouble(request.queryParams("valence")));
            vibe.getMinFeatures().setLiveness(Double.parseDouble(request.queryParams("liveness")));
            vibe.getMinFeatures().setAcousticness(Double.parseDouble(request.queryParams("acousticness")));
            vibe.getMinFeatures().setDanceability(Double.parseDouble(request.queryParams("danceability")));
            vibe.getMinFeatures().setEnergy(Double.parseDouble(request.queryParams("energy")));
            vibe.getMinFeatures().setSpeechiness(Double.parseDouble(request.queryParams("speechiness")));
            vibe.getMinFeatures().setInstrumentalness(Double.parseDouble(request.queryParams("instrumentalness")));
            vibe.getMaxFeatures().setPopularity(Integer.parseInt(request.queryParams("popularity")));
            vibe.getMaxFeatures().setTempo(Double.parseDouble(request.queryParams("tempo")));
            vibe.getMaxFeatures().setValence(Double.parseDouble(request.queryParams("valence")));
            vibe.getMaxFeatures().setLiveness(Double.parseDouble(request.queryParams("liveness")));
            vibe.getMaxFeatures().setAcousticness(Double.parseDouble(request.queryParams("acousticness")));
            vibe.getMaxFeatures().setDanceability(Double.parseDouble(request.queryParams("danceability")));
            vibe.getMaxFeatures().setEnergy(Double.parseDouble(request.queryParams("energy")));
            vibe.getMaxFeatures().setSpeechiness(Double.parseDouble(request.queryParams("speechiness")));
            vibe.getMaxFeatures().setInstrumentalness(Double.parseDouble(request.queryParams("instrumentalness")));

        	vibeDAO.updateVibe(vibe);
        	
            return id;
        } else {
            response.status(404); // 404 Not found
            return "Produto nao encontrado.";
        }

	}

}