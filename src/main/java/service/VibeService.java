package service;

import dao.*;
import model.*;
import spark.Request;
import spark.Response;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class VibeService {

    private VibeDAO vibeDAO;

    public VibeService() {
        vibeDAO = new VibeDAO();
    }

    public Object add(Request request, Response response) {
        String name = request.queryParams("name");
        String id = request.queryParams("id");
        String description = request.queryParams("description");
        String userId = request.queryParams("userId");
        String originTemplateId = request.queryParams("originTemplateId");
        Vibe vibe = new Vibe(userId, originTemplateId, name, description);

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
            "\",\"features\":{"+
            "\"popularity\":"+vibe.getFeatures().getPopularity()+
            ",\"tempo\":"+vibe.getFeatures().getTempo()+
            ",\"valence\":"+vibe.getFeatures().getValence()+
            ",\"liveness\":"+vibe.getFeatures().getLiveness()+
            ",\"acousticness\":"+vibe.getFeatures().getAcousticness()+
            ",\"danceability\":"+vibe.getFeatures().getDanceability()+
            ",\"energy\":"+vibe.getFeatures().getEnergy()+
            ",\"speechiness\":"+vibe.getFeatures().getSpeechiness()+
            ",\"instrumentalness\":"+vibe.getFeatures().getInstrumentalness()+
            "}"+
            "}";
        } else {
            response.status(404); // 404 Not found
            return "User " + id + " nao encontrado.";
        }

    }

    public Object remove(Request request, Response response) {
        String id = (request.params(":id"));

        Vibe vibe = (Vibe) vibeDAO.getVibe(id);

        if (vibe != null) {
            VibeSeedDAO vibeSeedDAO = new VibeSeedDAO();
            vibeSeedDAO.deleteAllVibeSeedFromVibe(vibe.getId());
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
                "\",\"features\":{"+
                "\"popularity\":"+vibe[i].getFeatures().getPopularity()+
                ",\"tempo\":"+vibe[i].getFeatures().getTempo()+
                ",\"valence\":"+vibe[i].getFeatures().getValence()+
                ",\"liveness\":"+vibe[i].getFeatures().getLiveness()+
                ",\"acousticness\":"+vibe[i].getFeatures().getAcousticness()+
                ",\"danceability\":"+vibe[i].getFeatures().getDanceability()+
                ",\"energy\":"+vibe[i].getFeatures().getEnergy()+
                ",\"speechiness\":"+vibe[i].getFeatures().getSpeechiness()+
                ",\"instrumentalness\":"+vibe[i].getFeatures().getInstrumentalness()+
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
        JSONObject requestBody = (JSONObject) JSONValue.parse(request.body());

        if (vibe != null) {
            vibe.setName((String) requestBody.get("name"));
            vibe.setUserId((String) requestBody.get("userId"));
            vibe.setOriginTemplateId((String) requestBody.get("originTemplateId"));
            vibe.setDescription((String) requestBody.get("description"));
            
            // System.out.println(request.queryParams("minFeatures[tempo]") == "" ? 0 : request.queryParams("minFeatures[tempo]"));
            JSONObject features = (JSONObject) requestBody.get("features");

            vibe.getFeatures().setPopularity(features.get("popularity") == null ? null : Integer.valueOf(String.valueOf(features.get("popularity"))));
            vibe.getFeatures().setTempo( features.get("tempo") == null ? null : Double.valueOf(String.valueOf( features.get("tempo"))));
            vibe.getFeatures().setValence(features.get("valence") == null ? null : Double.valueOf(String.valueOf( features.get("valence"))));
            vibe.getFeatures().setLiveness(features.get("liveness") == null ? null : Double.valueOf(String.valueOf( features.get("liveness"))));
            vibe.getFeatures().setAcousticness(features.get("acousticness") == null ? null : Double.valueOf(String.valueOf( features.get("acousticness"))));
            vibe.getFeatures().setDanceability(features.get("danceability") == null ? null : Double.valueOf(String.valueOf( features.get("danceability"))));
            vibe.getFeatures().setEnergy( features.get("energy") == null ? null : Double.valueOf(String.valueOf( features.get("energy"))));
            vibe.getFeatures().setSpeechiness(features.get("speechiness") == null ? null : Double.valueOf(String.valueOf( features.get("speechiness"))));
            vibe.getFeatures().setInstrumentalness(features.get("instrumentalness") == null ? null : Double.valueOf(String.valueOf( features.get("instrumentalness"))));

            vibeDAO.updateVibe(vibe);
            
            return vibe;
        } else {
            response.status(404); // 404 Not found
            return "Produto nao encontrado.";
        }

	}

}
