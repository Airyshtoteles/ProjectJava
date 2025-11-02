package id.ac.kampus.frs.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility to initialize SQLite database by executing schema and seed SQL scripts from classpath.
 * Run this once when using SQLite: it will create tables and seed demo data if missing.
 */
public class SqliteInitializer {
    private static void runScript(Connection con, String resourcePath) throws IOException, SQLException {
        try (InputStream in = SqliteInitializer.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (in == null) throw new IOException("Resource not found: " + resourcePath);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
                 Statement st = con.createStatement()) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    String trimmed = line.trim();
                    // strip line comments
                    if (trimmed.startsWith("--") || trimmed.isEmpty()) continue;
                    sb.append(line).append('\n');
                    // naive split by semicolon as statement terminator
                    int idx;
                    while ((idx = sb.indexOf(";")) >= 0) {
                        String stmt = sb.substring(0, idx).trim();
                        sb.delete(0, idx + 1);
                        if (!stmt.isEmpty()) {
                            st.execute(stmt);
                        }
                    }
                }
                // execute remaining without semicolon if any
                String rest = sb.toString().trim();
                if (!rest.isEmpty()) {
                    st.execute(rest);
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            if (!DBConnection.isSQLite()) {
                System.out.println("DB type is not SQLite. Skipping initialization.");
                return;
            }
            try (Connection con = DBConnection.getConnection()) {
                System.out.println("Initializing SQLite schema...");
                runScript(con, "sql/sqlite-schema.sql");
                System.out.println("Seeding demo data...");
                runScript(con, "sql/sqlite-seed.sql");
                System.out.println("SQLite init done.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
