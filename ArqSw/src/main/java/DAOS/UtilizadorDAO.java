/*package DAOS;

import Servidor.Utilizador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class UtilizadorDAO implements Map<Integer, Utilizador> {

        private Connection conn;


        public void clear () {
            try {
                conn = Connect.connect();
                Statement stm = conn.createStatement();
                stm.executeUpdate("DELETE FROM Utilizador");
            }catch(SQLException e){
                throw new NullPointerException(e.getMessage());
            }finally {
                Connect.close(conn);
            }
        }

        public boolean containsKey(Object key) throws NullPointerException {
            boolean b = false;
            try {
                conn = Connect.connect();
                Statement stm = conn.createStatement();
                String sql = "SELECT Username FROM Utilizador WHERE Username='"+(String)key+"'";
                ResultSet rs = stm.executeQuery(sql);
                b = rs.next();
            }catch(SQLException e){
                throw new NullPointerException(e.getMessage());
            }finally {
                Connect.close(conn);
            }
            return b;
        }

        public boolean containsValue(Object value) {
            boolean b = false;
            try {
                conn = Connect.connect();
                Statement stm = conn.createStatement();
                Utilizador u = (Utilizador) value;
                String sql = "SELECT Username FROM Utilizador WHERE Username='"+u.getUsername()+"' AND password ='"+u.getPassword()+"'";
                ResultSet rs = stm.executeQuery(sql);
                b = rs.next();
            }catch(SQLException e){
                throw new NullPointerException(e.getMessage());
            }finally {
                Connect.close(conn);
            }
            return b;
        }

        public Set<Entry<Integer, Utilizador>> entrySet() {
            Set<String> keys = new HashSet<>(this.keySet());

            Map<String,Utilizador> map = new HashMap<>();
            for(String key : keys){
                map.put(key,this.get(key));
            }
            return map.entrySet();
        }

        public boolean equals(Object o) {
            throw new NullPointerException("not implemented!");
        }

        public Funcionario get(Object key) {
            Funcionario f = null;
            try {
                conn = Connect.connect();
                Statement stm = conn.createStatement();
                String sql = "SELECT * FROM Funcionario WHERE Username='"+(String)key+"'";
                ResultSet rs = stm.executeQuery(sql);
                if (rs.next())
                    f = new Funcionario(rs.getString(1),rs.getString(2));
            }catch(SQLException e){
                throw new NullPointerException(e.getMessage());
            }finally {
                try{
                    Connect.close(conn);
                }
                catch(Exception e){
                    System.out.printf(e.getMessage());
                }
            }
            return f;
        }



        public boolean isEmpty() {
            return size() == 0;
        }

        public Set<String> keySet() {
            Set<String> col = null;
            try {
                conn = Connect.connect();
                col = new HashSet<>();
                Statement stm = conn.createStatement();
                ResultSet rs = stm.executeQuery("SELECT Username FROM Funcionario");
                for (;rs.next();) {
                    col.add(rs.getString("Username"));
                }
            }catch(SQLException e){
                throw new NullPointerException(e.getMessage());
            }finally {
                Connect.close(conn);
            }
            return col;
        }

        public Funcionario put(String key, Funcionario value) {
            Funcionario f = null;

            if(this.containsKey(key))
                f = this.get(key);
            else f = value;

            try {
                conn = Connect.connect();
                PreparedStatement ps = conn.prepareStatement("DELETE FROM Funcionario WHERE Username = ?");
                ps.setString(1,key);
                ps.executeUpdate();

                ps = conn.prepareStatement("INSERT INTO Funcionario (Username,Password) VALUES (?,?)");
                ps.setString(1,key);
                ps.setString(2,value.getPassword());
                ps.executeUpdate();
            }
            catch(SQLException e){
                System.out.printf(e.getMessage());
            }
            finally{
                try{
                    Connect.close(conn);
                }
                catch(Exception e){
                    System.out.printf(e.getMessage());
                }
            }
            return f;
        }


        public Funcionario remove(Object key) {
            Funcionario f = this.get(key);
            try {
                conn = Connect.connect();
                PreparedStatement stm = conn.prepareStatement("DELETE FROM Funcionario WHERE Username = ?");
                stm.setString(1,(String)key);
                stm.executeUpdate();
            } catch(SQLException e){
                System.out.printf(e.getMessage());
            }
            finally{
                try{
                    Connect.close(conn);
                }
                catch(Exception e){
                    System.out.printf(e.getMessage());
                }
            }
            return f;
        }


        public int size() {
            int size = -1;
            try {
                conn = Connect.connect();
                Statement stm = conn.createStatement();
                ResultSet rs = stm.executeQuery("SELECT COUNT(*) FROM Funcionario");
                if(rs.next())
                    size = rs.getInt(1);
            }
            catch(SQLException e){
                System.out.printf(e.getMessage());
            }
            finally{
                try{
                    Connect.close(conn);
                }
                catch(Exception e){
                    System.out.printf(e.getMessage());
                }
            }
            return size;
        }


        public Collection<Utilizador> values() {
            Collection<Utilizador> col = new HashSet<>();
            try {
                conn = Connect.connect();
                Statement stm = conn.createStatement();
                ResultSet rs = stm.executeQuery("SELECT * FROM Utilizador");
                for (;rs.next();) {
                    col.add(new Utilizador(rs.getString(1),rs.getString(2)));
                }
            }catch(SQLException e){
                throw new NullPointerException(e.getMessage());
            }finally {
                Connect.close(conn);
            }
            return col;
        }


        @Override
        public void putAll(Map<? extends Integer, ? extends Utilizador> m) {
            throw new UnsupportedOperationException("Not supported yet.");
        }



    }

*/
