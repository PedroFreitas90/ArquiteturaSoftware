package persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
    private static final String URL = "localhost";
    private static final String schema = "tplatform";

    public static Connection connect() {
        try {
            Connection c = DriverManager.getConnection("jdbc:postgresql://" + URL + ":5432/" + schema);
            return c;
        } catch (SQLException e) {
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
