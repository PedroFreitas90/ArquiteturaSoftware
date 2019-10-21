package DAOS;

import Servidor.Contrato;
import Servidor.Utilizador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;


public class UtilizadorDAO implements Map<Integer, Utilizador> {

    private Connection conn;

    @Override
    public synchronized int size() {
        int i = 0;
        try {
            conn = Connect.connect();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Utilizador");
            for (; rs.next(); i++) ;

        } catch (SQLException e) {
            throw new NullPointerException(e.getMessage());
        } finally {
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
    public synchronized Utilizador get(Object key) {

        Utilizador u = new Utilizador();
        List<Contrato> contratos = new ArrayList<>();
        try {
            conn = Connect.connect();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Utilizador WHERE idUtilizador= ?");
            ps.setString(1, Integer.toString((Integer) key));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                u.setId(rs.getInt("Utilizador.idUtlizador"));
                u.setUsername(rs.getNString("username"));
                u.setUsername(rs.getNString("password"));
                u.setPlafom(rs.getLong("plafom"));
             PreparedStatement   pa = conn.prepareStatement("SELECT * FROM Contrato WHERE idUtilizador= ?");
                pa.setString(1, Integer.toString((Integer) key));
                ResultSet ra = pa.executeQuery();
                if (ra.next()) {
                    Contrato c = new Contrato();
                    c.setId(ra.getInt("idContrato"));
                    c.setIdAtivo(ra.getInt("idAtivo"));
                    c.setIdUtilizador(ra.getInt("Utilizador.idUtlizador"));
                    c.setPreco(ra.getInt("preco"));
                    c.setTakeProfit(ra.getLong("takeprofit"));
                    c.setStopLoss(ra.getLong("stoploss"));
                    c.setQuantidade(ra.getInt("quantidade"));
                    int compra = ra.getInt("compra");
                    if (compra == 0)
                        c.setCompra(false);
                    else
                        c.setCompra(true);
                    int encerrado = ra.getInt("encerrado");
                    if (encerrado == 0)
                        c.setEncerrado(false);
                    else
                        c.setEncerrado(true);
                    contratos.add(c);

                }
            } else u = null;
            u.setPortefolio(contratos);
        } catch (SQLException e) {
            System.out.printf(e.getMessage());
        } finally {
            try {
                Connect.close(conn);
            } catch (Exception e) {
                System.out.printf(e.getMessage());
            }
        }
        return u;

    }

    @Override
    public synchronized Utilizador put(Integer key, Utilizador utilizador) {
        try {
            conn = Connect.connect();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM Utilizador WHERE idUtilizador = ?");
            ps.setString(1,Integer.toString((Integer) key));
            ps.executeUpdate();
            ps = conn.prepareStatement("INSERT INTO Utilizador (IdUtilizador,username,password,plafom) VALUES (?,?,?,?)");
            ps.setString(1, Integer.toString(key));
            ps.setString(2, utilizador.getUsername());
            ps.setString(3, utilizador.getPassword());
            ps.setString(4, Float.toString(utilizador.getPlafom()));
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.printf(e.getMessage());
        } finally {
            try {
                Connect.close(conn);

            } catch (Exception e) {
                System.out.printf(e.getMessage());
            }
        }
        return utilizador;

    }

    @Override
    public Utilizador remove(Object o) {
        return null;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Utilizador> map) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<Integer> keySet() {
        return null;
    }

    @Override
    public synchronized Collection<Utilizador> values() {
        Collection<Utilizador> col = new HashSet<Utilizador>();
        try {
            conn = Connect.connect();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Utilizador");
            for (; rs.next(); ) {
                Utilizador u = new Utilizador(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getLong(4));
                List<Contrato> c =contratosUtilizador(rs.getInt(1));
                u.setPortefolio(c);
                col.add(u);
            }

        } catch (SQLException e) {
            throw new NullPointerException(e.getMessage());
        } finally {
            Connect.close(conn);
        }


        return col;
    }

    @Override
    public Set<Entry<Integer, Utilizador>> entrySet() {
        return null;
    }


    public synchronized List<Contrato> contratosUtilizador(int id) {
        List<Contrato> contratos = new ArrayList<>();
        try {
            conn = Connect.connect();

            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Contrato WHERE idUtilizador= ?");
            ps.setString(1, Integer.toString( id));
            ResultSet rs = ps.executeQuery();
            for (; rs.next(); )  {
                Contrato c = new Contrato();
                c.setId(rs.getInt("idContrato"));
                c.setIdAtivo(rs.getInt("idAtivo"));
                c.setIdUtilizador(rs.getInt("idUtilizador"));
                c.setPreco(rs.getInt("preco"));
                c.setTakeProfit(rs.getLong("takeprofit"));
                c.setStopLoss(rs.getLong("stoploss"));
                c.setQuantidade(rs.getInt("quantidade"));
                int compra = rs.getInt("compra");
                if (compra == 0)
                    c.setCompra(false);
                else
                    c.setCompra(true);
                int encerrado = rs.getInt("encerrado");
                if (encerrado == 0)
                    c.setEncerrado(false);
                else
                    c.setEncerrado(true);
                contratos.add(c);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return contratos;
    }
}