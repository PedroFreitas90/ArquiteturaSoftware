package DAOS;

import Servidor.Contrato;
import Servidor.Estado;
import Servidor.Pedido;
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
        Statement stm = null;
        ResultSet rs = null;
        try {
            conn = Connect.connect();
            stm = conn.createStatement();
            rs = stm.executeQuery("SELECT * FROM Utilizador");
            for (; rs.next(); i++) ;

        } catch (SQLException e) {
            throw new NullPointerException(e.getMessage());
        } finally {
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
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = Connect.connect();
            ps = conn.prepareStatement("SELECT * FROM Utilizador WHERE idUtilizador= ?");
            ps.setString(1, Integer.toString((Integer) key));
            rs = ps.executeQuery();
            if (rs.next()) {
                u.setId((Integer)key);
                u.setUsername(rs.getString(2));
                u.setPassword(rs.getString(3));
                u.setPlafom(rs.getLong("plafom"));
            }
            else u = null;
        } catch (SQLException e) {
            System.out.printf(e.getMessage());
        } finally {
            try {
                ps.close();
                rs.close();
                Connect.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return u;

    }

    @Override
    public synchronized   Utilizador put(Integer key, Utilizador utilizador) {
        PreparedStatement ps = null;
        try {
            conn = Connect.connect();
            ps = conn.prepareStatement("DELETE FROM Utilizador WHERE idUtilizador = ?");
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
                ps.close();
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
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            conn = Connect.connect();
            System.out.println(conn.isClosed());
            stm = conn.prepareStatement("SELECT * FROM Utilizador");
             rs = stm.executeQuery();
            for (; rs.next(); ) {
                Utilizador u = new Utilizador(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getLong(4));
                List<Pedido.Memento> mementos = mementosUtilizador(rs.getInt(1));
              u.setPedidosSave(mementos);

                col.add(u);
            }

        } catch (SQLException e) {
            System.out.println("nao fez a query");
            throw new NullPointerException("nao fez a query");
        } finally {
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
    public Set<Entry<Integer, Utilizador>> entrySet() {
        return null;
    }


    public  List<Contrato> contratosUtilizador(int id) {
        List<Contrato> contratos = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = Connect.connect();

             ps = conn.prepareStatement("SELECT * FROM Contrato WHERE idUtilizador= ?");
            ps.setString(1, Integer.toString( id));
             rs = ps.executeQuery();
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
            try {
                ps.close();
                rs.close();
                Connect.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return contratos;
    }


    public  List<Pedido.Memento> mementosUtilizador(int id){
        List<Pedido.Memento> mementos = new ArrayList<>();
        PreparedStatement ps =null;
        ResultSet rs = null;
        try {
            conn = Connect.connect();

            ps = conn.prepareStatement("SELECT * FROM Pedido WHERE idUtilizador= ? AND estado= 0" );
            ps.setString(1, Integer.toString( id));
            rs = ps.executeQuery();
            Pedido p = new Pedido();
            for (; rs.next(); )  {
                String descricao = rs.getString("descricao");
                int estado = rs.getInt("estado");
                int idPedido = rs.getInt("idPedido");
                int idUtilizador = rs.getInt("idUtilizador");
                Estado e = null;
                if (estado == 0)
                    e  =  new Estado(descricao,false,idPedido,idUtilizador);

                else
                    e  =  new Estado(descricao,true,idPedido,idUtilizador);
                p.set(e);
                mementos.add(p.saveToMemento());

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                ps.close();
                rs.close();
                Connect.close(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return mementos;
    }

}
