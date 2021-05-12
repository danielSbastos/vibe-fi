package dao;

import java.sql.*;
import model.User;
import util.db.DatabaseConnection;

public class UserDAO {
    protected Connection connection;

    public UserDAO() {
        connection = null;
    }

    /* connection methods */
    protected boolean connect() {
        boolean status = false;
        if (connection == null) {
            try {
                connection = DatabaseConnection.getConnection();
                status = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            status = true;
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

    /******************************************************************************************/

    /* preparedStatement methods */

    private PreparedStatement prepareCreateUserSQLStatement(User user) throws SQLException {
        connect();

        String query = "INSERT INTO vibefi.user("
                + "idSpotify, name, popularity, tempo, valence, liveness, acousticness, danceability, energy, speechiness, instrumentalness)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement pst = connection.prepareStatement(query);

        pst.setString(1, user.getId());
        pst.setString(2, user.getName());
        pst.setObject(3, user.getStats().getPopularity());
        pst.setObject(4, user.getStats().getTempo());
        pst.setObject(5, user.getStats().getValence());
        pst.setObject(6, user.getStats().getLiveness());
        pst.setObject(7, user.getStats().getAcousticness());
        pst.setObject(8, user.getStats().getDanceability());
        pst.setObject(9, user.getStats().getEnergy());
        pst.setObject(10, user.getStats().getSpeechiness());
        pst.setObject(11, user.getStats().getInstrumentalness());
        
        return pst;
    }

    private PreparedStatement prepareGetUserSQLStatement(String id) throws SQLException {
        connect();

        String query = "SELECT * FROM vibefi.user WHERE idSpotify = ?;";
        PreparedStatement pst = connection.prepareStatement(query);

        pst.setString(1, id);

        return pst;
    }

    private PreparedStatement prepareUpdateUserSQLStatement(User user) throws SQLException {
        connect();

        String query = "UPDATE vibefi.user"
                + " SET name=?, popularity=?, tempo=?, valence=?, liveness=?, acousticness=?, danceability=?, energy=?, speechiness=?, instrumentalness=?"
                + " WHERE idSpotify=?;";
        PreparedStatement pst = connection.prepareStatement(query);

        pst.setString(1, user.getName());
        pst.setObject(2, user.getStats().getPopularity());
        pst.setObject(3, user.getStats().getTempo());
        pst.setObject(4, user.getStats().getValence());
        pst.setObject(5, user.getStats().getLiveness());
        pst.setObject(6, user.getStats().getAcousticness());
        pst.setObject(7, user.getStats().getDanceability());
        pst.setObject(8, user.getStats().getEnergy());
        pst.setObject(9, user.getStats().getSpeechiness());
        pst.setObject(10, user.getStats().getInstrumentalness());
        pst.setString(11, user.getId());

        return pst;
    }

    private PreparedStatement prepareDeleteUserSQLStatement(String id) throws SQLException {
        connect();

        String query = "DELETE FROM vibefi.user WHERE idSpotify = ?;";
        PreparedStatement pst = connection.prepareStatement(query);

        pst.setString(1, id);

        return pst;
    }

    /******************************************************************************************/

    /* CRUD */

    public boolean createUser(User user) {
        boolean status = false;
        if (connection == null) {
            connect();
        }

        try {
            PreparedStatement pst = prepareCreateUserSQLStatement(user);

            pst.executeUpdate();
            pst.close();
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
            PreparedStatement pst = prepareGetUserSQLStatement(id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                user = new User(rs.getString("idSpotify"), rs.getString("name"), (Integer) rs.getObject("popularity"),
                        (Double) rs.getObject("tempo"), (Double) rs.getObject("valence"),
                        (Double) rs.getObject("liveness"), (Double) rs.getObject("acousticness"),
                        (Double) rs.getObject("danceability"), (Double) rs.getObject("energy"),
                        (Double) rs.getObject("speechiness"), (Double) rs.getObject("instrumentalness"));
            }
            pst.close();
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

        try {
            PreparedStatement pst = prepareUpdateUserSQLStatement(user);
            pst.executeUpdate();
            pst.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }

        return status;
    }

    public boolean deleteUser(String id) {
        boolean status = false;

        try {
            PreparedStatement pst = prepareDeleteUserSQLStatement(id);
            pst.executeUpdate();
            pst.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }

        return status;
    }
}
