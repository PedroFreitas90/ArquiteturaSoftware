package DAOS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {

    public static Connection connect(){
        Connection connect = null;
        try{
            String url = "jdbc:sqlite:/home/francisco/Downloads/db";
            Class.forName("org.sqlite.JDBC");
            connect = DriverManager.getConnection(url);
        } catch(SQLException | ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        return connect;
    }

    public static void close(Connection c){
        try{
            if(c != null && !c.isClosed()){
                c.close();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
