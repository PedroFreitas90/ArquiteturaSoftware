package DAOS;

import Servidor.Contrato;


import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ContratoDAO implements Map<Integer, Contrato> {

    private Connection conn;

    @Override
    public synchronized int size() {
        int i = 0;
        try {
            conn = Connect.connect();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Contrato");
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
    public  synchronized Contrato get(Object key) {

        Contrato c = new Contrato();
        try{
            conn = Connect.connect();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Contrato WHERE idContrato= ?");
            ps.setString(1,Integer.toString((Integer) key));
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                c.setId(rs.getInt("idContrato"));
                c.setIdAtivo(rs.getInt("idAtivo"));
                c.setIdUtilizador(rs.getInt("idUtilizador"));
                c.setPreco(rs.getInt("preco"));
                c.setTakeProfit(rs.getLong("takeprofit"));
                c.setStopLoss(rs.getLong("stoploss"));
                c.setQuantidade(rs.getInt("quantidade"));
                int compra = rs.getInt("compra");
                if(compra==0)
                    c.setCompra(false);
                else
                    c.setCompra(true);
                int encerrado = rs.getInt("encerrado");
                if(encerrado==0)
                    c.setEncerrado(false);
                else
                    c.setEncerrado(true);

            }
            else c = null;
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
        return c;

    }

    @Override
    public synchronized Contrato put(Integer key, Contrato contrato) {
        try{
            conn = Connect.connect();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM Contrato WHERE idContrato = ?");
            ps.setString(1,Integer.toString((Integer) key));
            ps.executeUpdate();

            ps = conn.prepareStatement(
                    "INSERT INTO Contrato (idContrato,idAtivo,idUtilizador,preco,takeprofit,stoploss,quantidade,compra,encerrado) " +
                            "VALUES (?,?,?,?,?,?,?,?,?)");
            ps.setString(1,Integer.toString(key));
            ps.setString(2, Integer.toString(contrato.getIdAtivo()));
            ps.setString(3, Integer.toString(contrato.getIdUtilizador()));
            ps.setString(4, Float.toString(contrato.getPreco()));
            ps.setString(5, Float.toString(contrato.getTakeProfit()));
            ps.setString(6, Float.toString(contrato.getStopLoss()));
            ps.setString(7, Integer.toString(contrato.getQuantidade()));
            if(contrato.isCompra())
                ps.setString(8, Integer.toString(1));
            else
                ps.setString(8, Integer.toString(0));
            if(contrato.isEncerrado())
                ps.setString(9, Integer.toString(1));
            else
                ps.setString(9, Integer.toString(0));

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
        return contrato;
    }

    @Override
    public Contrato remove(Object o) {
        return null;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Contrato> map) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<Integer> keySet() {
        return null;
    }

    @Override
    public synchronized Collection<Contrato> values() {
        Collection<Contrato> col = new HashSet<>();
        try {
            conn = Connect.connect();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Contrato where encerrado = 0");
            for (;rs.next();) {
                Contrato c = new Contrato();
                c.setId(rs.getInt("idContrato"));
                c.setIdAtivo(rs.getInt("idAtivo"));
                c.setIdUtilizador(rs.getInt("idUtilizador"));
                c.setPreco(rs.getInt("preco"));
                c.setTakeProfit(rs.getLong("takeprofit"));
                c.setStopLoss(rs.getLong("stoploss"));
                c.setQuantidade(rs.getInt("quantidade"));
                int compra = rs.getInt("compra");
                if(compra==0)
                    c.setCompra(false);
                else
                    c.setCompra(true);
                int encerrado = rs.getInt("encerrado");
                if(encerrado==0)
                    c.setEncerrado(false);
                else
                    c.setEncerrado(true);
                col.add(c);
            }
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



        return col;
    }

    @Override
    public Set<Entry<Integer, Contrato>> entrySet() {
        return null;
    }

    public  synchronized Contrato get(Object key, Object idUtilizador) {

        Contrato c = new Contrato();
        try{
            conn = Connect.connect();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Contrato WHERE idContrato= ? AND idUtilizador= ?");
            ps.setString(1,Integer.toString((Integer) key));
            ps.setString(2,Integer.toString((Integer) idUtilizador));
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                c.setId(rs.getInt("idContrato"));
                c.setIdAtivo(rs.getInt("idAtivo"));
                c.setIdUtilizador(rs.getInt("idUtilizador"));
                c.setPreco(rs.getInt("preco"));
                c.setTakeProfit(rs.getLong("takeprofit"));
                c.setStopLoss(rs.getLong("stoploss"));
                c.setQuantidade(rs.getInt("quantidade"));
                int compra = rs.getInt("compra");
                if(compra==0)
                    c.setCompra(false);
                else
                    c.setCompra(true);
                int encerrado = rs.getInt("encerrado");
                if(encerrado==0)
                    c.setEncerrado(false);
                else
                    c.setEncerrado(true);

            }
            else c = null;
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
        return c;

    }

}
