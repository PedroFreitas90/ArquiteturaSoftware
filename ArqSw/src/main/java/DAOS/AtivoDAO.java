package DAOS;

import Servidor.Ativo;
import Servidor.Utilizador;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AtivoDAO implements Map<Integer, Ativo> {

    private Connection conn;
    @Override
    public synchronized int size() {
        int i = 0;
        try {
            conn = Connect.connect();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Ativo");
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
    public synchronized boolean containsValue(Object o) {
        boolean res = false;
            Ativo r = (Ativo) o;
            int id = r.getId();
            Ativo re = this.get(id);
            if(re==null)
                return res;
            if(re.equals(r)) {
                res = true;
            }
        return res;
    }

    @Override
    public Ativo get(Object key) {

        Ativo a = new Ativo();
        try{
            conn = Connect.connect();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Ativo WHERE idAtivo= ?");
            ps.setString(1,Integer.toString((Integer) key));
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                a.setId(rs.getInt("idAtivo"));
                a.setPrecoCompra(rs.getFloat("precoCompra"));
                a.setPrecoVenda(rs.getFloat("precoVenda"));
                a.setDescricao(rs.getString("descricao"));
            }
            else a = null;
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
        return a;

    }

    @Override
    public synchronized Ativo put(Integer key, Ativo ativo) {
        try{
            conn = Connect.connect();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM Ativo WHERE idAtivo = ?");
            ps.setString(1,Integer.toString((Integer) key));
            ps.executeUpdate();

            ps = conn.prepareStatement("INSERT INTO Ativo (idAtivo,precoVenda,precoCompra,descricao) VALUES (?,?,?,?)");
            ps.setString(1,Integer.toString(key));
            ps.setString(2, Float.toString(ativo.getPrecoVenda()));
            ps.setString(3, Float.toString(ativo.getPrecoCompra()));
            ps.setString(4, ativo.getDescricao());
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
        return ativo;
    }

    @Override
    public Ativo remove(Object o) {
        return null;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Ativo> map) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<Integer> keySet() {
        return null;
    }

    @Override
    public synchronized Collection<Ativo> values() {
        Collection<Ativo> col = new HashSet<Ativo>();
        try {
            conn = Connect.connect();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Ativo");
            for (;rs.next();) {
                col.add(
                        new Ativo(rs.getInt(1),rs.getFloat(2),rs.getFloat(3),rs.getString(4))
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
    public Set<Entry<Integer, Ativo>> entrySet() {
        return null;
    }
}
