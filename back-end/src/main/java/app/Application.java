package app;

import dao.VibeTemplateDAO;
import model.Features;
import model.User;
import model.Vibe;
import model.VibeTemplate;

public class Application {
    
    public static void main(String[] args) throws Exception {
        
        VibeTemplateDAO vibeTemplateDAO = new VibeTemplateDAO();
        
        Integer popularity = 2;
        Double tempo = 120.23;
        Double valence = 0.2;
        Double liveness = 0.24;
        Double acousticness = 0.1;
        Double danceability = 0.2;
        Double energy = 0.6;
        Double speechiness = 0.2;
        Double instrumentalness = 0.2;

        Features min = new Features(popularity, tempo, valence, liveness, acousticness, danceability, energy, speechiness, instrumentalness);
        Features max = new Features(popularity, tempo, valence, liveness, acousticness, danceability, energy, speechiness, instrumentalness);
        
        VibeTemplate vt = new VibeTemplate("id1", "name", "description", min, max);
        
        //User user = new User(";id1", ";SELECT * FROM vibefi.user;namsade", popularity, tempo, valence, liveness, acousticness, danceability, energy, speechiness, instrumentalness);
        
        //vibeTemplateDAO.createVibeTemplate(vt);
        //vibeTemplateDAO.getVibeTemplate("id");
        //vibeTemplateDAO.getNVibeTemplates(10);
        //vibeTemplateDAO.updateTemplate(vt);
        vibeTemplateDAO.deleteTemplate("id1");
        //userDAO.createUser(user);
        //userDAO.getUser(";id1");
        //userDAO.updateUser(user);
        //userDAO.deleteUser(user.getId());
        vibeTemplateDAO.disconect();
    }
}
