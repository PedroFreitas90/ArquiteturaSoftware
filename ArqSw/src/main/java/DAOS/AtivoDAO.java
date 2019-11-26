package DAOS;

import Servidor.*;
import Servidor.Observer;

import java.sql.*;
import java.util.*;

public class AtivoDAO implements Map<Integer, Ativo> {

    private Connection conn;
    @Override
    public synchronized int size() {
        int i = 0;
        Statement stm = null;
        ResultSet rs= null;
        try {
            conn = Connect.connect();
            stm = conn.createStatement();
            rs = stm.executeQuery("SELECT * FROM Ativo");
            for (;rs.next();i++);

        }catch(SQLException e){
            throw new NullPointerException(e.getMessage());
        }finally {
            try {
                stm.close();
                rs.close();
                Connect.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return i;

    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        boolean res = false;
        PreparedStatement ps= null;
        ResultSet rs=null;
        try{
            conn = Connect.connect();
            ps = conn.prepareStatement("SELECT * FROM Ativo WHERE descricao = ?");
            ps.setString(1, (String) key);
            rs = ps.executeQuery();
            res = rs.next();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        finally {
            try {
                try {
                    ps.close();
                    rs.close();
                    Connect.close(conn);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return res;
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
    public synchronized Ativo get(Object key) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Ativo a = new Ativo();
        try{
            conn = Connect.connect();
            ps = conn.prepareStatement("SELECT * FROM Ativo WHERE idAtivo= ?");
            ps.setString(1,Integer.toString((Integer) key));
            rs = ps.executeQuery();
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
            try {
                ps.close();
                rs.close();
                Connect.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return a;

    }


    @Override
    public synchronized Ativo put(Integer key, Ativo ativo) {
        PreparedStatement ps = null;
        try{
            conn = Connect.connect();
            ps = conn.prepareStatement("DELETE FROM Ativo WHERE idAtivo = ?");
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
            try {
                ps.close();
                Connect.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
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
        Statement stm = null;
        ResultSet rs = null;

        try {
            conn = Connect.connect();
            stm = conn.createStatement();
            rs = stm.executeQuery("SELECT * FROM Ativo");
            for (;rs.next();) {
                col.add(
                        new Ativo(rs.getInt(1),rs.getFloat(2),rs.getFloat(3),rs.getString(4))
                );
            }

        }catch(SQLException e){
            throw new NullPointerException(e.getMessage());
        }finally {
            try {
                stm.close();
                rs.close();
                Connect.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }



        return col;
    }

    @Override
    public Set<Entry<Integer, Ativo>> entrySet() {
        return null;
    }



    public synchronized Ativo get(Object descricao, Object ess){
        PreparedStatement ps = null;
        PreparedStatement cc = null;
        PreparedStatement cv = null;

        ResultSet rs = null;
        ResultSet c = null;
        ResultSet k = null;
        Ativo a = new Ativo();
        try{
            conn = Connect.connect();
            ps = conn.prepareStatement("SELECT * FROM Ativo WHERE descricao= ?");
            ps.setString(1, (String) descricao);
            rs = ps.executeQuery();

            if(rs.next()){
                a.setId(rs.getInt("idAtivo"));
                a.setPrecoCompra(rs.getFloat("precoCompra"));
                a.setPrecoVenda(rs.getFloat("precoVenda"));
                a.setDescricao(rs.getString("descricao"));
            }
            cc = conn.prepareStatement("SELECT * FROM Contrato WHERE idAtivo = ? AND encerrado = 0");
            cc.setString(1,Integer.toString((Integer) rs.getInt("idAtivo")));
            c = cc.executeQuery();

            while(c.next()) {
                Contrato contrato = new Contrato();
                contrato.setId(c.getInt("idContrato"));
                contrato.setIdAtivo(c.getInt("idAtivo"));
                contrato.setIdUtilizador(c.getInt("idUtilizador"));
                contrato.setPreco(c.getFloat("preco"));
                contrato.setTakeProfit(c.getFloat("takeprofit"));
                contrato.setStopLoss(c.getFloat("stoploss"));
                contrato.setQuantidade(c.getInt("quantidade"));
                int compra = c.getInt("compra");
                if(compra==0)
                    contrato.setCompra(false);
                else
                    contrato.setCompra(true);
                int encerrado = c.getInt("encerrado");
                if(encerrado==0)
                    contrato.setEncerrado(false);
                else
                    contrato.setEncerrado(true);
                contrato.setEss((ESS_ltd) ess);
                a.registerObserver(contrato);
            }

        }
        catch(SQLException e){
            System.out.printf(e.getMessage());
        }
        finally{
            Connect.close(conn);

        }
        return a;
    }
}