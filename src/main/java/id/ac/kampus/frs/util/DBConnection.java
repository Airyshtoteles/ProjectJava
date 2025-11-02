package id.ac.kampus.frs.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

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
        String dbType = props.getProperty("db.type", "mysql").trim().toLowerCase();
        if ("sqlite".equals(dbType)) {
            String dbPath = props.getProperty("db.sqlite.path", "frs.db");
            String url = "jdbc:sqlite:" + dbPath;
            Connection con = DriverManager.getConnection(url);
            try (Statement st = con.createStatement()) {
                st.execute("PRAGMA foreign_keys=ON");
            }
            return con;
        } else {
            String host = props.getProperty("db.host");
            String port = props.getProperty("db.port");
            String db = props.getProperty("db.name");
            String user = props.getProperty("db.user");
            String pass = props.getProperty("db.password");
            String params = props.getProperty("jdbc.params", "useSSL=false");

            String url = String.format("jdbc:mysql://%s:%s/%s?%s", host, port, db, params);
            return DriverManager.getConnection(url, user, pass);
        }
    }

    public static boolean isSQLite() {
        String dbType = props.getProperty("db.type", "mysql");
        return "sqlite".equalsIgnoreCase(dbType);
    }
}
