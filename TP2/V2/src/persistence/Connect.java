package persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
    private static final String URL = "localhost";
    private static final String schema = "tplatform";

    public static Connection connect() {
        try {
            Class.forName("org.postgresql.Driver");
            Connection c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/tplatform","davide","jw8s0F4");
            return c;
        } catch (SQLException | ClassNotFoundException e) {
            // unable to connect
            e.printStackTrace();
        }
        return null;
    }

    public static void close(Connection c) {
        try {
            c.close();
        } catch (Exception e) {
            // nothing to do here :/
        }
    }
}
