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
        PreparedStatement us = null;
        PreparedStatement as = null;

        ResultSet rs = null;
        ResultSet c = null;
        ResultSet k = null;
        ResultSet aa = null;
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
            us = conn.prepareStatement("SELECT * FROM Seguidores s ,Utilizador u WHERE idAtivo = ?  AND u.idUtilizador=s.idUtilizador");
            us.setString(1,Integer.toString((Integer) rs.getInt(1)));
            int x = rs.getInt(1);
            k = us.executeQuery();
            while(k.next()){
                Utilizador utilizador = new Utilizador();
                utilizador.setId((Integer)k.getInt(5));
                utilizador.setUsername(k.getString(6));
                utilizador.setPassword(k.getString(7));
                utilizador.setPlafom(k.getLong(8));

                as = conn.prepareStatement("SELECT * FROM Seguidores s ,Ativo a WHERE idUtilizador= ?  AND a.idAtivo=s.idAtivo");
                as.setString(1,Integer.toString((Integer) k.getInt(5)));
                aa = as.executeQuery();
                Map<Integer,Ativo > ativosSeguidos = new HashMap<>();
                while(aa.next()){
                    Ativo ativoSeguido =  new Ativo();
                    ativoSeguido.setId(aa.getInt(5));
                    ativoSeguido.setPrecoCompra(aa.getFloat(3));
                    ativoSeguido.setPrecoVenda(aa.getFloat(4));
                    ativoSeguido.setDescricao(aa.getString(8));
                    ativosSeguidos.put(ativoSeguido.getId(),ativoSeguido);
                }

                utilizador.setaSeguir(ativosSeguidos);
                utilizador.setEss((ESS_ltd)ess);
                a.registerObserver(utilizador);
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

/******************* NOVO REQUISITO*******************/
    public synchronized  void putSeguidor(Integer key,Ativo a){
        PreparedStatement ps = null;
        try{
            conn = Connect.connect();
            ps = conn.prepareStatement("DELETE FROM Seguidores WHERE idAtivo = ? AND idUtilizador= ?" );
            ps.setString(1,Integer.toString((Integer) a.getId()));
            ps.setString(2,Integer.toString((Integer) key));
            ps.executeUpdate();
            ps = conn.prepareStatement("INSERT INTO Seguidores(idAtivo,idUtilizador,valorCompra,valorVenda) VALUES (?,?,?,?)");
            ps.setString(1,Integer.toString(a.getId()));
            ps.setString(2, Integer.toString(key));
            ps.setString(3, Float.toString(a.getPrecoCompra()));
            ps.setString(4,Float.toString(a.getPrecoVenda()));
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

    }

}
