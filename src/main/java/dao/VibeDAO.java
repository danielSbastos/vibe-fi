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
                + "idvibe, iduser, origintemplate, name, description, minpopularity, maxpopularity, mintempo, maxtempo,"
                + " minvalence, maxvalence, minliveness, maxliveness, minacousticness, maxacousticness, mindanceability,"
                + " maxdanceability, minenergy, maxenergy, minspeechiness, maxspeechiness, mininstrumentalness, maxinstrumentalness)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement pst = connection.prepareStatement(query);

        pst.setString(1, vibe.getId());
        pst.setString(2, vibe.getUserId());
        pst.setObject(3, vibe.getOriginTemplateId());
        pst.setString(4, vibe.getName());
        pst.setObject(5, vibe.getDescription());
        pst.setObject(6, vibe.getMinFeatures().getPopularity());
        pst.setObject(7, vibe.getMaxFeatures().getPopularity());
        pst.setObject(8, vibe.getMinFeatures().getTempo());
        pst.setObject(9, vibe.getMaxFeatures().getTempo());
        pst.setObject(10, vibe.getMinFeatures().getValence());
        pst.setObject(11, vibe.getMaxFeatures().getValence());
        pst.setObject(12, vibe.getMinFeatures().getLiveness());
        pst.setObject(13, vibe.getMaxFeatures().getLiveness());
        pst.setObject(14, vibe.getMinFeatures().getAcousticness());
        pst.setObject(15, vibe.getMaxFeatures().getAcousticness());
        pst.setObject(16, vibe.getMinFeatures().getDanceability());
        pst.setObject(17, vibe.getMaxFeatures().getDanceability());
        pst.setObject(18, vibe.getMinFeatures().getEnergy());
        pst.setObject(19, vibe.getMaxFeatures().getEnergy());
        pst.setObject(20, vibe.getMinFeatures().getSpeechiness());
        pst.setObject(21, vibe.getMaxFeatures().getSpeechiness());
        pst.setObject(22, vibe.getMinFeatures().getInstrumentalness());
        pst.setObject(23, vibe.getMaxFeatures().getInstrumentalness());

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

        String query = "UPDATE vibefi.vibe"
                + " SET name=?, description=?, minpopularity=?, maxpopularity=?, mintempo=?, maxtempo=?, minvalence=?, maxvalence=?,"
                + " minliveness=?, maxliveness=?, minacousticness=?, maxacousticness=?, mindanceability=?, maxdanceability=?,"
                + " minenergy=?, maxenergy=?, minspeechiness=?, maxspeechiness=?, mininstrumentalness=?, maxinstrumentalness=?"
                + " WHERE idvibe=?;";
        PreparedStatement pst = connection.prepareStatement(query);

        pst.setString(1, vibe.getName());
        pst.setObject(2, vibe.getDescription());
        pst.setObject(3, vibe.getMinFeatures().getPopularity());
        pst.setObject(4, vibe.getMaxFeatures().getPopularity());
        pst.setObject(5, vibe.getMinFeatures().getTempo());
        pst.setObject(6, vibe.getMaxFeatures().getTempo());
        pst.setObject(7, vibe.getMinFeatures().getValence());
        pst.setObject(8, vibe.getMaxFeatures().getValence());
        pst.setObject(9, vibe.getMinFeatures().getLiveness());
        pst.setObject(10, vibe.getMaxFeatures().getLiveness());
        pst.setObject(11, vibe.getMinFeatures().getAcousticness());
        pst.setObject(12, vibe.getMaxFeatures().getAcousticness());
        pst.setObject(13, vibe.getMinFeatures().getDanceability());
        pst.setObject(14, vibe.getMaxFeatures().getDanceability());
        pst.setObject(15, vibe.getMinFeatures().getEnergy());
        pst.setObject(16, vibe.getMaxFeatures().getEnergy());
        pst.setObject(17, vibe.getMinFeatures().getSpeechiness());
        pst.setObject(18, vibe.getMaxFeatures().getSpeechiness());
        pst.setObject(19, vibe.getMinFeatures().getInstrumentalness());
        pst.setObject(20, vibe.getMaxFeatures().getInstrumentalness());
        pst.setString(21, vibe.getId());

        return pst;
    }

    private PreparedStatement prepareDeleteVibeSQLStatement(String id) throws SQLException {
        connect();

        String query = "DELETE FROM vibefi.vibe WHERE idvibe = ?;";
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
                        (Integer) rs.getObject("minPopularity"), (Integer) rs.getObject("maxPopularity"),
                        (Double) rs.getObject("minTempo"), (Double) rs.getObject("maxTempo"),
                        (Double) rs.getObject("minValence"), (Double) rs.getObject("maxValence"),
                        (Double) rs.getObject("minLiveness"), (Double) rs.getObject("maxLiveness"),
                        (Double) rs.getObject("minAcousticness"), (Double) rs.getObject("maxAcousticness"),
                        (Double) rs.getObject("minDanceability"), (Double) rs.getObject("maxDanceability"),
                        (Double) rs.getObject("minEnergy"), (Double) rs.getObject("maxEnergy"),
                        (Double) rs.getObject("minSpeechiness"), (Double) rs.getObject("maxSpeechiness"),
                        (Double) rs.getObject("minInstrumentalness"), (Double) rs.getObject("maxInstrumentalness"));

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
                            (String) rs.getObject("description"), (Integer) rs.getObject("minPopularity"),
                            (Integer) rs.getObject("maxPopularity"), (Double) rs.getObject("minTempo"),
                            (Double) rs.getObject("maxTempo"), (Double) rs.getObject("minValence"),
                            (Double) rs.getObject("maxValence"), (Double) rs.getObject("minLiveness"),
                            (Double) rs.getObject("maxLiveness"), (Double) rs.getObject("minAcousticness"),
                            (Double) rs.getObject("maxAcousticness"), (Double) rs.getObject("minDanceability"),
                            (Double) rs.getObject("maxDanceability"), (Double) rs.getObject("minEnergy"),
                            (Double) rs.getObject("maxEnergy"), (Double) rs.getObject("minSpeechiness"),
                            (Double) rs.getObject("maxSpeechiness"), (Double) rs.getObject("minInstrumentalness"),
                            (Double) rs.getObject("maxInstrumentalness"));
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

    public boolean deleteTemplate(String id) {
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
            for (int i = 0; i<vibeTemplates.length; i++) {
                createVibesFromTemplate(userId, vibeTemplates[i]);
            }
            status = true;
        } catch (Exception u) {
            throw new RuntimeException(u);
        }

        return status;
    }
}


