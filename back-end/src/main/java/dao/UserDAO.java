package dao;

import java.sql.*;
import model.User;
import util.db.DatabaseConnection;

public class UserDAO {
    protected Connection connection;

    public UserDAO() {
        connection = null;
    }

    protected boolean connect() {
        boolean status = false;
        try {
            connection = DatabaseConnection.getConnection();
            status = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    public boolean disconect() {
        boolean status = false;
        try {
            DatabaseConnection.closeConnection();
            status = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    public boolean createUser(User user) {
        boolean status = false;
        if (connection == null) {
            connect();
        }

        String query = "INSERT INTO vibefi.user (idSpotify, name, popularity, tempo, valence, liveness, acousticness, danceability, energy, speechiness, instrumentalness)"
                + " VALUES ('" + user.getId() + "', '" + user.getName() + "', " + user.getStats().getPopularity() + ", "
                + user.getStats().getTempo() + ", " + user.getStats().getValence() + ", "
                + user.getStats().getLiveness() + ", " + user.getStats().getAcousticness() + ", "
                + user.getStats().getDanceability() + ", " + user.getStats().getEnergy() + ", "
                + user.getStats().getSpeechiness() + ", " + user.getStats().getInstrumentalness() + ");";

        try {
            Statement st = connection.createStatement();
            st.executeUpdate(query);
            st.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }

    public User getUser(String id) {
        User user = null;
        if (connection == null) {
            connect();
        }

        try {
            Statement st = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = st.executeQuery("SELECT * FROM vibefi.user WHERE idSpotify = '" + id + "';");
            if (rs.next()) {
                user = new User(rs.getString("idSpotify"), rs.getString("name"), rs.getInt("popularity"),
                        rs.getDouble("tempo"), rs.getDouble("valence"), rs.getDouble("liveness"),
                        rs.getDouble("acousticness"), rs.getDouble("danceability"), rs.getDouble("energy"),
                        rs.getDouble("speechiness"), rs.getDouble("instrumentalness"));
            }
            st.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return user;
    }

    public boolean updateUser(User user) {
        boolean status = false;
        if (connection == null) {
            connect();
        }

        String query = "UPDATE vibefi.user" + " SET name = '" + user.getName() + "', popularity = "
                + user.getStats().getPopularity() + ", tempo = " + user.getStats().getTempo() + ", valence = "
                + user.getStats().getValence() + ", liveness = " + user.getStats().getLiveness() + ", acousticness = "
                + user.getStats().getAcousticness() + ", danceability = " + user.getStats().getDanceability()
                + ", energy = " + user.getStats().getEnergy() + ", speechiness = " + user.getStats().getSpeechiness()
                + ", instrumentalness = " + user.getStats().getInstrumentalness() + " WHERE idSpotify = '"
                + user.getId() + "';";

        try {
            Statement st = connection.createStatement();
            st.executeUpdate(query);
            st.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }

        return status;
    }

    public boolean deleteUser(String id) {
        boolean status = false;
        if (connection == null) {
            connect();
        }

        String query = "DELETE FROM vibefi.user WHERE idSpotify = '" + id + "';";

        try {
            Statement st = connection.createStatement();
            st.executeUpdate(query);
            st.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }

        return status;
    }
}
