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

        String query = "INSERT INTO vibefi.template("
                + "idtemplate, name, description, minpopularity, maxpopularity, mintempo, maxtempo, minvalence, maxvalence,"
                + " minliveness, maxliveness, minacousticness, maxacousticness, mindanceability, maxdanceability, minenergy,"
                + " maxenergy, minspeechiness, maxspeechiness, mininstrumentalness, maxinstrumentalness)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement pst = connection.prepareStatement(query);

        pst.setString(1, template.getId());
        pst.setString(2, template.getName());
        pst.setObject(3, template.getDescription());
        pst.setObject(4, template.getMinFeatures().getPopularity());
        pst.setObject(5, template.getMaxFeatures().getPopularity());
        pst.setObject(6, template.getMinFeatures().getTempo());
        pst.setObject(7, template.getMaxFeatures().getTempo());
        pst.setObject(8, template.getMinFeatures().getValence());
        pst.setObject(9, template.getMaxFeatures().getValence());
        pst.setObject(10, template.getMinFeatures().getLiveness());
        pst.setObject(11, template.getMaxFeatures().getLiveness());
        pst.setObject(12, template.getMinFeatures().getAcousticness());
        pst.setObject(13, template.getMaxFeatures().getAcousticness());
        pst.setObject(14, template.getMinFeatures().getDanceability());
        pst.setObject(15, template.getMaxFeatures().getDanceability());
        pst.setObject(16, template.getMinFeatures().getEnergy());
        pst.setObject(17, template.getMaxFeatures().getEnergy());
        pst.setObject(18, template.getMinFeatures().getSpeechiness());
        pst.setObject(19, template.getMaxFeatures().getSpeechiness());
        pst.setObject(20, template.getMinFeatures().getInstrumentalness());
        pst.setObject(21, template.getMaxFeatures().getInstrumentalness());

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

        String query = "UPDATE vibefi.template"
                + " SET name=?, description=?, minpopularity=?, maxpopularity=?, mintempo=?, maxtempo=?, minvalence=?, maxvalence=?,"
                + " minliveness=?, maxliveness=?, minacousticness=?, maxacousticness=?, mindanceability=?, maxdanceability=?,"
                + " minenergy=?, maxenergy=?, minspeechiness=?, maxspeechiness=?, mininstrumentalness=?, maxinstrumentalness=?"
                + " WHERE idtemplate=?;";
        PreparedStatement pst = connection.prepareStatement(query);

        pst.setString(1, template.getName());
        pst.setObject(2, template.getDescription());
        pst.setObject(3, template.getMinFeatures().getPopularity());
        pst.setObject(4, template.getMaxFeatures().getPopularity());
        pst.setObject(5, template.getMinFeatures().getTempo());
        pst.setObject(6, template.getMaxFeatures().getTempo());
        pst.setObject(7, template.getMinFeatures().getValence());
        pst.setObject(8, template.getMaxFeatures().getValence());
        pst.setObject(9, template.getMinFeatures().getLiveness());
        pst.setObject(10, template.getMaxFeatures().getLiveness());
        pst.setObject(11, template.getMinFeatures().getAcousticness());
        pst.setObject(12, template.getMaxFeatures().getAcousticness());
        pst.setObject(13, template.getMinFeatures().getDanceability());
        pst.setObject(14, template.getMaxFeatures().getDanceability());
        pst.setObject(15, template.getMinFeatures().getEnergy());
        pst.setObject(16, template.getMaxFeatures().getEnergy());
        pst.setObject(17, template.getMinFeatures().getSpeechiness());
        pst.setObject(18, template.getMaxFeatures().getSpeechiness());
        pst.setObject(19, template.getMinFeatures().getInstrumentalness());
        pst.setObject(20, template.getMaxFeatures().getInstrumentalness());
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
