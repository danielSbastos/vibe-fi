package dao;

import java.sql.*;

import model.VibeSeed;
import util.db.DatabaseConnection;

public class VibeSeedDAO {
    protected Connection connection;

    public VibeSeedDAO() {
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

    private PreparedStatement prepareCreateVibeSeedSQLStatement(VibeSeed vibeSeed) throws SQLException {
        connect();

        String query = "INSERT INTO vibefi.vibeseed(vibe, seedidentifier, type) VALUES (?, ?, ?);";
        PreparedStatement pst = connection.prepareStatement(query);

        pst.setString(1, vibeSeed.getVibeId());
        pst.setString(2, vibeSeed.getIdentifier());
        pst.setObject(3, vibeSeed.getType(), Types.OTHER);

        return pst;
    }

    private PreparedStatement prepareGetVibeSeedsBySeedSQLStatement(String vibeIdentifier) throws SQLException {
        connect();

        String query = "SELECT vibe, seedidentifier, type FROM vibefi.vibeseed WHERE vibe = ?;";
        PreparedStatement pst = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);

        pst.setString(1, vibeIdentifier);
        return pst;
    }

    private PreparedStatement prepareUpdateVibeSeedSQLStatement(VibeSeed vibeSeed, String oldIdentifier)
            throws SQLException {
        connect();

        String query = "UPDATE vibefi.vibeseed SET seedidentifier=?, type=? WHERE vibe=? AND seedidentifier=?;";
        PreparedStatement pst = connection.prepareStatement(query);

        pst.setString(1, vibeSeed.getIdentifier());
        pst.setObject(2, vibeSeed.getType(), Types.OTHER);
        pst.setString(3, vibeSeed.getVibeId());
        pst.setString(4, oldIdentifier);

        return pst;
    }

    private PreparedStatement prepareDeleteVibeSeedSQLStatement(VibeSeed vibeSeed) throws SQLException {
        connect();

        String query = "DELETE FROM vibefi.vibeseed	WHERE vibe=? AND seedidentifier=?;";
        PreparedStatement pst = connection.prepareStatement(query);

        pst.setString(1, vibeSeed.getVibeId());
        pst.setString(2, vibeSeed.getIdentifier());

        return pst;
    }

    private PreparedStatement prepareDeleteAllVibeSeedsFromVibeSQLStatement(String vibeIdentifier) throws SQLException {
        connect();

        String query = "DELETE FROM vibefi.vibeseed	WHERE vibe=?;";
        PreparedStatement pst = connection.prepareStatement(query);

        pst.setString(1, vibeIdentifier);

        return pst;
    }

    /******************************************************************************************/

    /* CRUD */

    public boolean createVibeSeed(VibeSeed vibeSeed) {
        boolean status = false;

        try {
            PreparedStatement pst = prepareCreateVibeSeedSQLStatement(vibeSeed);

            pst.executeUpdate();
            pst.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }

    public VibeSeed[] getVibeSeedsByVibe(String vibeIdentifier) {
        VibeSeed[] seeds = null;

        try {
            PreparedStatement pst = prepareGetVibeSeedsBySeedSQLStatement(vibeIdentifier);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                rs.last();
                seeds = new VibeSeed[rs.getRow()];
                rs.beforeFirst();

                for (int i = 0; rs.next(); i++) {
                    seeds[i] = new VibeSeed(rs.getString("vibe"), rs.getString("seedidentifier"), rs.getString("type"));
                }
            }

            pst.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return seeds;
    }

    public boolean updateVibeSeed(VibeSeed vibeSeed, String oldIdentifier) {
        boolean status = false;

        try {
            PreparedStatement pst = prepareUpdateVibeSeedSQLStatement(vibeSeed, oldIdentifier);
            pst.executeUpdate();
            pst.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }

        return status;
    }

    public boolean deleteVibeSeed(VibeSeed vibeSeed) {
        boolean status = false;

        try {
            PreparedStatement pst = prepareDeleteVibeSeedSQLStatement(vibeSeed);
            pst.executeUpdate();
            pst.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }

        return status;
    }

    public boolean deleteAllVibeSeedFromVibe(String vibeIdentifier) {
        boolean status = false;

        try {
            PreparedStatement pst = prepareDeleteAllVibeSeedsFromVibeSQLStatement(vibeIdentifier);
            pst.executeUpdate();
            pst.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }

        return status;
    }

}
