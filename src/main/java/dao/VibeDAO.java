package dao;

import java.sql.*;
import model.Vibe;
import model.VibeTemplate;
import util.db.DatabaseConnection;

public class VibeDAO {
    protected Connection connection;

    public VibeDAO() {
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

    private PreparedStatement prepareCreateVibeSQLStatement(Vibe vibe) throws SQLException {
        connect();

        String query = "INSERT INTO vibefi.vibe("
                + "idvibe, iduser, origintemplate, name, description, popularity,  tempo,"
                + " valence,  liveness,  acousticness,  danceability," + " energy,  speechiness,  instrumentalness)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement pst = connection.prepareStatement(query);

        pst.setString(1, vibe.getId());
        pst.setString(2, vibe.getUserId());
        pst.setObject(3, vibe.getOriginTemplateId());
        pst.setString(4, vibe.getName());
        pst.setObject(5, vibe.getDescription());
        pst.setObject(6, vibe.getFeatures().getPopularity());
        pst.setObject(7, vibe.getFeatures().getTempo());
        pst.setObject(8, vibe.getFeatures().getValence());
        pst.setObject(9, vibe.getFeatures().getLiveness());
        pst.setObject(10, vibe.getFeatures().getAcousticness());
        pst.setObject(11, vibe.getFeatures().getDanceability());
        pst.setObject(12, vibe.getFeatures().getEnergy());
        pst.setObject(13, vibe.getFeatures().getSpeechiness());
        pst.setObject(14, vibe.getFeatures().getInstrumentalness());

        return pst;
    }

    private PreparedStatement prepareGetVibeSQLStatement(String id) throws SQLException {
        connect();

        String query = "SELECT * FROM vibefi.vibe WHERE idvibe = ?;";
        PreparedStatement pst = connection.prepareStatement(query);

        pst.setString(1, id);

        return pst;
    }

    private PreparedStatement prepareGetUserVibesSQLStatement(String userId) throws SQLException {
        connect();

        String query = "SELECT * FROM vibefi.vibe WHERE iduser = ?;";
        PreparedStatement pst = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);

        pst.setString(1, userId);

        return pst;
    }

    private PreparedStatement prepareUpdateVibeSQLStatement(Vibe vibe) throws SQLException {
        connect();

        String query = "UPDATE vibefi.vibe" + " SET name=?, description=?, popularity=?, tempo=?, valence=?,"
                + " liveness=?, acousticness=?, danceability=?," + " energy=?, speechiness=?, instrumentalness=?"
                + " WHERE idvibe=?;";
        PreparedStatement pst = connection.prepareStatement(query);

        pst.setString(1, vibe.getName());
        pst.setObject(2, vibe.getDescription());
        pst.setObject(3, vibe.getFeatures().getPopularity());
        pst.setObject(4, vibe.getFeatures().getTempo());
        pst.setObject(5, vibe.getFeatures().getValence());
        pst.setObject(6, vibe.getFeatures().getLiveness());
        pst.setObject(7, vibe.getFeatures().getAcousticness());
        pst.setObject(8, vibe.getFeatures().getDanceability());
        pst.setObject(9, vibe.getFeatures().getEnergy());
        pst.setObject(10, vibe.getFeatures().getSpeechiness());
        pst.setObject(11, vibe.getFeatures().getInstrumentalness());
        pst.setString(12, vibe.getId());

        return pst;
    }

    private PreparedStatement prepareDeleteVibeSQLStatement(String id) throws SQLException {
        connect();
        
        String query = "DELETE FROM vibefi.vibe WHERE idvibe = ?;";
        PreparedStatement pst = connection.prepareStatement(query);

        pst.setString(1, id);
        
        return pst;
    }

    private PreparedStatement prepareDeleteVibeFromUserSQLStatement(String id) throws SQLException {
        connect();
        
        String query = "DELETE FROM vibefi.vibe WHERE iduser = ?;";
        PreparedStatement pst = connection.prepareStatement(query);

        pst.setString(1, id);

        return pst;
    }

    /******************************************************************************************/
    /* CRUD */

    public boolean createVibe(Vibe vibe) {
        boolean status = false;

        try {
            PreparedStatement pst = prepareCreateVibeSQLStatement(vibe);

            pst.executeUpdate();
            pst.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }

        return status;
    }

    public Vibe getVibe(String id) {
        Vibe vibe = null;

        try {
            PreparedStatement pst = prepareGetVibeSQLStatement(id);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                vibe = new Vibe(rs.getString("idVibe"), rs.getString("iduser"), (String) rs.getObject("originTemplate"),
                        rs.getString("name"), (String) rs.getObject("description"),
                        (Integer) rs.getObject("popularity"), (Double) rs.getObject("tempo"),
                        (Double) rs.getObject("valence"), (Double) rs.getObject("liveness"),
                        (Double) rs.getObject("acousticness"), (Double) rs.getObject("danceability"),
                        (Double) rs.getObject("energy"), (Double) rs.getObject("speechiness"),
                        (Double) rs.getObject("instrumentalness"));

            }
            pst.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return vibe;
    }

    public Vibe[] getUserVibes(String userId) {
        Vibe[] vibes = null;

        try {
            PreparedStatement pst = prepareGetUserVibesSQLStatement(userId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                rs.last();
                vibes = new Vibe[rs.getRow()];
                rs.beforeFirst();

                for (int i = 0; rs.next(); i++) {
                    vibes[i] = new Vibe(rs.getString("idVibe"), rs.getString("iduser"),
                            (String) rs.getObject("originTemplate"), rs.getString("name"),
                            (String) rs.getObject("description"), (Integer) rs.getObject("popularity"),
                            (Double) rs.getObject("tempo"), (Double) rs.getObject("valence"),
                            (Double) rs.getObject("liveness"), (Double) rs.getObject("acousticness"),
                            (Double) rs.getObject("danceability"), (Double) rs.getObject("energy"),
                            (Double) rs.getObject("speechiness"), (Double) rs.getObject("instrumentalness"));
                }
            }

            pst.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return vibes;
    }

    public boolean updateVibe(Vibe vibe) {
        boolean status = false;

        try {
            PreparedStatement pst = prepareUpdateVibeSQLStatement(vibe);
            pst.executeUpdate();
            pst.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }

        return status;
    }
    
    public boolean deleteVibe(String id) {
        boolean status = false;
        
        try {
            PreparedStatement pst = prepareDeleteVibeSQLStatement(id);
            pst.executeUpdate();
            pst.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }

        return status;
    }

    public boolean deleteAllUserVibes(String id) {
        boolean status = false;

        try {
            PreparedStatement pst = prepareDeleteVibeFromUserSQLStatement(id);
            pst.executeUpdate();
            pst.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }

        return status;
    }

    /******************************************************************************************/
    /* extra */

    public boolean createVibesFromTemplate(String userId, VibeTemplate template) {
        boolean status = false;

        try {
            Vibe vibe = new Vibe(userId, template);
            PreparedStatement pst = prepareCreateVibeSQLStatement(vibe);
            pst.executeUpdate();
            pst.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }

        return status;
    }

    public boolean createVibesFromTemplates(String userId) {
        boolean status = false;
        VibeTemplateDAO vtDao = new VibeTemplateDAO();

        try {
            VibeTemplate[] vibeTemplates = vtDao.getNVibeTemplates(10);
            for (int i = 0; i < vibeTemplates.length; i++) {
                createVibesFromTemplate(userId, vibeTemplates[i]);
            }
            status = true;
        } catch (Exception u) {
            throw new RuntimeException(u);
        }

        return status;
    }
}
