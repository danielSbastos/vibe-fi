package util.db;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection implements Serializable {
    private static Connection connection = null;
    
    private DatabaseConnection() {
    }

    private static boolean connect() {
        boolean status = false;

        String driverName = "org.postgresql.Driver";
        String serverName = "ti2-cc-vibefi.postgres.database.azure.com";
        // String serverName = "localhost";
        String mydatabase = "vibeFi";
        int porta = 5432;
        String url = "jdbc:postgresql://" + serverName + ":" + porta + "/" + mydatabase;
        String username = "vibefi_admin@ti2-cc-vibefi";
        // String username = "postgres";
        String password = "CL6QeZTnqg8qdc6";
        // String password = "admin";
        
        try {
            Class.forName(driverName);
            connection = DriverManager.getConnection(url, username, password);
            status = (connection == null);
            System.out.println("Conexao efetuada com o postgres!");
        } catch (ClassNotFoundException e) {
            System.err.println("Conexao NAO efetuada com o postgres -- Driver nao encontrado -- " + e.getMessage());
        } catch (SQLException e) {
            System.err.println(e);
        }

        return status;
    }

    private static boolean close() {
        boolean status = false;

        try {
            connection.close();
            status = true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return status;
    }

    /*
     * Return a connection from the datasource pool
     */
    public static Connection getConnection() throws Exception {
        if (connection == null) {
            connect();
        }
        return connection;
    }

    public static boolean closeConnection() throws Exception {
        if (connection == null) {
            return true;
        } else {
            return close();
        }
    }

}