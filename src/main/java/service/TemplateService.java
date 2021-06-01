package service;
import dao.*;
import model.*;
import spark.Request;
import spark.Response;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
// import org.json.simple.JSONValue;
import java.util.HashMap;
// import java.util.Iterator;
import java.util.Map;

public class TemplateService {

    private VibeTemplateDAO templateDAO;

    public TemplateService() {
        templateDAO = new VibeTemplateDAO();
    }
    
    public JSONObject parseTemplate(VibeTemplate vibeTemplate) {
        Map<String, Object> templateMap = new HashMap<String, Object>();
        
        templateMap.put("id", vibeTemplate.getId());
        templateMap.put("name", vibeTemplate.getName());
        templateMap.put("desc", vibeTemplate.getDescription());
        templateMap.put("imgUrl", vibeTemplate.getImgUrl());
        
        return new JSONObject(templateMap);
    }
    
    public Object getTemplateImage (Request request, Response response) {
        String id = (request.params(":id"));
        VibeTemplate template = templateDAO.getVibeTemplate(id);
        
        if (template != null) {
            return parseTemplate(template);
        } else {
            response.status(404); // 404 Not found
            Map<String, Object> tmp = new HashMap<String, Object>();
            tmp.put("error", 404);
            tmp.put("message", "Not found");
            return new JSONObject(tmp);
        }
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
                templates.add(parseTemplate(template[i]));
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