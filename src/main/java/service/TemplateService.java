package service;
import dao.*;
import model.*;
import spark.Request;
import spark.Response;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TemplateService {

    private VibeTemplateDAO templateDAO;

    public TemplateService() {
        templateDAO = new VibeTemplateDAO();
    }

    @SuppressWarnings("unchecked")
    public Object getDescription(Request request, Response response) {
        VibeTemplate template[] = (VibeTemplate[]) templateDAO.getNVibeTemplates(10);

        if (template != null) {
            response.header("Content-Type", "application/json");
            response.header("Content-Encoding", "UTF-8");
            Map<String, Object> tmp = new HashMap<String, Object>();
            JSONArray templates = new JSONArray();
            for(int i = 0; i < template.length; i++) {
                Map<String, Object> json = new HashMap<String, Object>();
                json.put("id", template[i].getId());
                json.put("name", template[i].getName());
                json.put("desc", template[i].getDescription());
                templates.add(json);
            }
            tmp.put("templates", templates);
            tmp.put("total", templates.size());
            return new JSONObject(tmp);
        } else {
            response.status(404); // 404 Not found
            Map<String, Object> tmp = new HashMap<String, Object>();
            tmp.put("error", 404);
            tmp.put("message", "Not found");
            return new JSONObject(tmp);
        }

    }
    
}