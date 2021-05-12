package app;

import dao.UserDAO;
import model.User;

public class Application {
    
    public static void main(String[] args) throws Exception {
        
        UserDAO userDAO = new UserDAO();

        Integer popularity = 2;
        Double tempo = 120.0;
        Double valence = 0.2;
        Double liveness = 0.2;
        Double acousticness = 0.2;
        Double danceability = 0.2;
        Double energy = 0.2;
        Double speechiness = 0.2;
        Double instrumentalness = 0.2;
        
        User user = new User("id", "name", popularity, tempo, valence, liveness, acousticness, danceability, energy, speechiness, instrumentalness);
        
        
        //userDAO.createUser(user);
        userDAO.getUser("id");
        //userDAO.updateUser(user);
        //userDAO.deleteUser(user.getId());
        userDAO.disconect();
    }
}
