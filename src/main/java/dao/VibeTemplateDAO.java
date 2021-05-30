package dao;

import java.sql.*;
import model.VibeTemplate;
import util.db.DatabaseConnection;

public class VibeTemplateDAO {
    protected Connection connection;

    public VibeTemplateDAO() {
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

    private PreparedStatement prepareCreateTemplateSQLStatement(VibeTemplate template) throws SQLException {
        connect();

        String query = "INSERT INTO vibefi.template(" + "idtemplate, name, description, popularity, tempo, valence,"
                + " liveness, acousticness, danceability, energy," + " speechiness, instrumentalness)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement pst = connection.prepareStatement(query);

        pst.setString(1, template.getId());
        pst.setString(2, template.getName());
        pst.setObject(3, template.getDescription());
        pst.setObject(4, template.getFeatures().getPopularity());
        pst.setObject(5, template.getFeatures().getTempo());
        pst.setObject(6, template.getFeatures().getValence());
        pst.setObject(7, template.getFeatures().getLiveness());
        pst.setObject(8, template.getFeatures().getAcousticness());
        pst.setObject(9, template.getFeatures().getDanceability());
        pst.setObject(10, template.getFeatures().getEnergy());
        pst.setObject(11, template.getFeatures().getSpeechiness());
        pst.setObject(12, template.getFeatures().getInstrumentalness());

        return pst;
    }

    private PreparedStatement prepareGetTemplateSQLStatement(String id) throws SQLException {
        connect();

        String query = "SELECT * FROM vibefi.template WHERE idTemplate = ?;";
        PreparedStatement pst = connection.prepareStatement(query);

        pst.setString(1, id);

        return pst;
    }

    private PreparedStatement prepareGetNTemplatesSQLStatement(int n) throws SQLException {
        connect();

        String query = "SELECT * FROM vibefi.template ORDER BY idtemplate ASC LIMIT ?;";
        PreparedStatement pst = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);

        pst.setInt(1, n);

        return pst;
    }

    private PreparedStatement prepareUpdateTemplateSQLStatement(VibeTemplate template) throws SQLException {
        connect();

        String query = "UPDATE vibefi.template" + " SET name=?, description=?, popularity=?, tempo=?, valence=?,"
                + " liveness=?, acousticness=?, danceability=?," + " energy=?, speechiness=?, instrumentalness=?"
                + " WHERE idtemplate=?;";
        PreparedStatement pst = connection.prepareStatement(query);

        pst.setString(1, template.getName());
        pst.setObject(2, template.getDescription());
        pst.setObject(3, template.getFeatures().getPopularity());
        pst.setObject(4, template.getFeatures().getTempo());
        pst.setObject(5, template.getFeatures().getValence());
        pst.setObject(6, template.getFeatures().getLiveness());
        pst.setObject(7, template.getFeatures().getAcousticness());
        pst.setObject(8, template.getFeatures().getDanceability());
        pst.setObject(9, template.getFeatures().getEnergy());
        pst.setObject(10, template.getFeatures().getSpeechiness());
        pst.setObject(11, template.getFeatures().getInstrumentalness());
        pst.setString(21, template.getId());

        return pst;
    }

    private PreparedStatement prepareDeleteTemplateSQLStatement(String id) throws SQLException {
        connect();

        String query = "DELETE FROM vibefi.template WHERE idtemplate = ?;";
        PreparedStatement pst = connection.prepareStatement(query);

        pst.setString(1, id);

        return pst;
    }

    /******************************************************************************************/
    /* CRUD */

    public boolean createVibeTemplate(VibeTemplate template) {
        boolean status = false;

        try {
            PreparedStatement pst = prepareCreateTemplateSQLStatement(template);

            pst.executeUpdate();
            pst.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }

        return status;
    }

    public VibeTemplate getVibeTemplate(String id) {
        VibeTemplate template = null;

        try {
            PreparedStatement pst = prepareGetTemplateSQLStatement(id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                template = new VibeTemplate(rs.getString("idTemplate"), rs.getString("name"),
                        (String) rs.getObject("description"), (Integer) rs.getObject("popularity"),
                        (Double) rs.getObject("tempo"), (Double) rs.getObject("valence"),
                        (Double) rs.getObject("liveness"), (Double) rs.getObject("acousticness"),
                        (Double) rs.getObject("danceability"), (Double) rs.getObject("energy"),
                        (Double) rs.getObject("speechiness"), (Double) rs.getObject("instrumentalness"));
            }
            pst.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return template;
    }

    public VibeTemplate[] getNVibeTemplates(int n) {
        VibeTemplate[] templates = null;

        try {
            PreparedStatement pst = prepareGetNTemplatesSQLStatement(n);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                rs.last();
                templates = new VibeTemplate[rs.getRow()];
                rs.beforeFirst();

                for (int i = 0; rs.next(); i++) {
                    templates[i] = new VibeTemplate(rs.getString("idTemplate"), rs.getString("name"),
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
        return templates;
    }

    public boolean updateTemplate(VibeTemplate template) {
        boolean status = false;

        try {
            PreparedStatement pst = prepareUpdateTemplateSQLStatement(template);
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
            PreparedStatement pst = prepareDeleteTemplateSQLStatement(id);
            pst.executeUpdate();
            pst.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }

        return status;
    }
}
