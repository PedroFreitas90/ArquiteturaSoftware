package persistence;

import business.Negociador;

import java.sql.*;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class NegociadorDAO implements Map<Integer, Negociador> {

    @Override
    public int size() {
        Connection c = Connect.connect();
        if (c == null) {
            return 0;
        }
        Statement s;
        int result = 0;
        try {
            s = c.createStatement();
            ResultSet resultSet = s.executeQuery("select count(*) from negociador");
            resultSet.next();
            result = resultSet.getInt(1);
            resultSet.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        Connect.close(c);

        return result;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object o) {
        Connection c = Connect.connect();
        if (c == null) {
            return false;
        }

        if (!(o instanceof Integer))
            return false;

        Integer key = (Integer) o;

        PreparedStatement s;
        boolean result = false;

        try {
            s = c.prepareStatement("select nif from negociador where nif = ?");
            s.setInt(1,key);

            ResultSet resultSet = s.executeQuery();
            result = resultSet.isBeforeFirst();
            resultSet.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        Connect.close(c);

        return result;
    }

    @Override
    public boolean containsValue(Object o) {
        return false;
    }

    @Override
    public Negociador get(Object o) {
        Connection c = Connect.connect();
        if (c == null) {
            return null;
        }

        if (!(o instanceof Integer))
            return null;

        Integer key = (Integer) o;

        PreparedStatement s;
        try {
            s = c.prepareStatement("select * from negociador where nif = ?");
            s.setInt(1,key);

            ResultSet resultSet = s.executeQuery();
            if (!resultSet.isBeforeFirst())
                return null;

            resultSet.next();
            int nif = resultSet.getInt("nif");
            String nome = resultSet.getString("nome");
            String email = resultSet.getString("email");
            String password = resultSet.getString("password");
            double saldo = resultSet.getDouble("saldo");

            Negociador n = new Negociador(nif, nome, email, password, saldo);

            resultSet.close();
            Connect.close(c);
            return n;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        Connect.close(c);

        return null;
    }

    @Override
    public Negociador put(Integer key, Negociador negociador) {
        Connection c = Connect.connect();
        if (c == null) {
            return null;
        }

        if (key != negociador.getNif())
            return null;

        PreparedStatement s ;
        try {
            if (this.containsKey(key)) {
                s = c.prepareStatement("update negociador set nif = ?, nome = ?, email = ?, password = ?, saldo = ? where nif = ?;");
                s.setInt(6,negociador.getNif());
            }
            else
                s = c.prepareStatement("insert into negociador values (?, ?, ?, ?, ?);");

            s.setInt(1,negociador.getNif());
            s.setString(2,negociador.getNome());
            s.setString(3,negociador.getEmail());
            s.setString(4,negociador.getPassword());
            s.setDouble(5,negociador.getSaldo());

            int updated = s.executeUpdate();

            if (updated == 1)
                return negociador;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        Connect.close(c);

        return null;
    }

    @Override
    public Negociador remove(Object o) {
        return null;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Negociador> map) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<Integer> keySet() {
        return null;
    }

    @Override
    public Collection<Negociador> values() {
        return null;
    }

    @Override
    public Set<Entry<Integer, Negociador>> entrySet() {
        return null;
    }



}