package DAOS;

import Servidor.Ativo;
import Servidor.Registo;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RegistoDAO implements Map<Integer, Registo> {
    private Connection conn;
    @Override
    public synchronized int size() {
        int i = 0;
        try {
            conn = Connect.connect();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Registo");
            for (;rs.next();i++);

        }catch(SQLException e){
            throw new NullPointerException(e.getMessage());
        }finally {
            Connect.close(conn);
        }
        return i;

    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object o) {
        return false;
    }

    @Override
    public boolean containsValue(Object o) {
        return false;
    }

    @Override
    public Registo get(Object o) {
        return null;
    }

    @Override
    public synchronized Registo put(Integer key, Registo registo) {
        try{
            conn = Connect.connect();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM Registo WHERE idRegisto = ?");
            ps.setString(1,Integer.toString((Integer) key));
            ps.executeUpdate();

            ps = conn.prepareStatement("INSERT INTO Registo (idRegisto,idUtilizador,idAtivo,lucro,quantidade) VALUES (?,?,?,?,?)");
            ps.setString(1,Integer.toString(key));
            ps.setString(2,Integer.toString(registo.getIdUtilizador()));
            ps.setString(3,Integer.toString(registo.getIdAtivo()));
            ps.setString(4,Float.toString(registo.getLucro()));
            ps.setString(5, Integer.toString(registo.getQuantidade()));
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
        return registo;
    }


    @Override
    public Registo remove(Object o) {
        return null;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Registo> map) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<Integer> keySet() {
        return null;
    }

    @Override
    public synchronized Collection<Registo> values() {
        Collection<Registo> col = new HashSet<>();
        try {
            conn = Connect.connect();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Registo");
            for (;rs.next();) {
                col.add(
                        new Registo(rs.getInt(1),rs.getInt(2),rs.getInt(3),rs.getFloat(4),rs.getInt(5))
                );
            }

        }catch(SQLException e){
            throw new NullPointerException(e.getMessage());
        }finally {
            Connect.close(conn);
        }



        return col;
    }

    @Override
    public Set<Entry<Integer, Registo>> entrySet() {
        return null;
    }
}
