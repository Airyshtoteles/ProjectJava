package id.ac.kampus.frs.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Database connection utility for MySQL (XAMPP).
 */
public class DBConnection {
    private static final Properties props = new Properties();

    static {
        try (InputStream in = DBConnection.class.getClassLoader()
                .getResourceAsStream("config/db.properties")) {
            if (in == null) {
                throw new IllegalStateException("config/db.properties not found in resources");
            }
            props.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load DB properties", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        String host = props.getProperty("db.host", "127.0.0.1");
        String port = props.getProperty("db.port", "3306");
        String db = props.getProperty("db.name", "frs_db");
        String user = props.getProperty("db.user", "root");
        String pass = props.getProperty("db.password", "");
        String params = props.getProperty("jdbc.params", "useSSL=false");

        String url = String.format("jdbc:mysql://%s:%s/%s?%s", host, port, db, params);
        return DriverManager.getConnection(url, user, pass);
    }
}
