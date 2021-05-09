package app;

import java.sql.*;

import util.db.DatabaseConnection;

public class Application {
    
    public static void main(String[] args) throws Exception {
        
        Connection conn = DatabaseConnection.getConnection();
    
        Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = st.executeQuery("SELECT * FROM user");
        System.out.print(rs);
    }
}
