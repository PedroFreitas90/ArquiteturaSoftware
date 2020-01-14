package persistence;

import business.CFD;
import business.Long;
import business.Short;
import business.Negociador;
import business.Observer;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class CFDAtivoDAO implements List<Observer> {
    private String idAtivo;

    public CFDAtivoDAO(String id) {
        this.idAtivo = id;
    }

    @Override
    public int size() {
        Connection c = Connect.connect();
        if (c == null) {
            return 0;
        }

        PreparedStatement s;
        int result = 0;

        try {
            s = c.prepareStatement("select count(*) from cfd where idativo = ?;");
            s.setString(1, this.idAtivo);

            ResultSet resultSet = s.executeQuery();
            resultSet.next();
            result = resultSet.getInt(1);

            s = c.prepareStatement("select count(*) from negociadorativo where idativo = ?");
            s.setString(1, this.idAtivo);

            resultSet = s.executeQuery();
            resultSet.next();
            result += resultSet.getInt(1);

            resultSet.close();

        } catch (SQLException e) {
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
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<Observer> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        return null;
    }

    private boolean addNegociador(Negociador n, Connection c) {
        if (n.isSeguindoAtivo(this.idAtivo)) {
            Connect.close(c);
            return false;
        }
        boolean success = false;

        PreparedStatement s;
        try {
            s = c.prepareStatement("delete from negociadorativo where idnegociador = ? and idativo = ?");
            s.setInt(1, n.getNif());
            s.setString(2, this.idAtivo);

            s.executeUpdate();
            success = true;

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        Connect.close(c);
        return success;
    }

    private boolean addCFD(CFD cfd, Connection c) {
        if (cfd.isAberto()) {
            Connect.close(c);
            return false;
        }

        boolean success = false;
        PreparedStatement s;
        try {
            s = c.prepareStatement("update cfd set aberto = ?, valorporunidadenofim = ? where cfd.id = ?; ");
            s.setBoolean(1, cfd.isAberto());
            s.setDouble(2, cfd.getValorPorUnidadeFinal());
            s.setInt(3, cfd.getId());

            s.executeUpdate();

            s = c.prepareStatement("select saldo from negociador where nif = ?");
            s.setInt(1, cfd.getNifNegociador());

            ResultSet rs = s.executeQuery();
            rs.next();
            double saldo = rs.getDouble(1);
            double novoSaldo = cfd.getGanhoDoFecho() + saldo;

            s = c.prepareStatement("update negociador set saldo = ? where nif = ?");
            s.setDouble(1, novoSaldo);
            s.setInt(2, cfd.getNifNegociador());

            s.executeUpdate();


            success = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Connect.close(c);

        return success;
    }

    @Override
    public boolean add(Observer observer) {
        Connection c = Connect.connect();
        if (c == null) {
            return false;
        }
        if (observer instanceof Negociador)
            return addNegociador((Negociador) observer, c);
        else if (observer instanceof CFD)
            return addCFD((CFD) observer, c);

        Connect.close(c);
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends Observer> collection) {
        for(Observer o : collection) {
            this.add(o);
        }
        return true;
    }

    @Override
    public boolean addAll(int i, Collection<? extends Observer> collection) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return false;
    }

    @Override
    public void clear() {
        // do nothing
    }

    private Observer getNegociador(Connection c, int i) {
        try {
            PreparedStatement s = c.prepareStatement("select * from negociadorativo where idativo = ?");
            s.setString(1, this.idAtivo);

            ResultSet rs = s.executeQuery();

            while (i >= 0) {
                rs.next();
                i--;
            }

            int nif = rs.getInt("idnegociador");
            Connect.close(c);
            return new NegociadorDAO().get(nif);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        Connect.close(c);
        return null;
    }

    @Override
    public Observer get(int i) {
        Connection c = Connect.connect();
        if (c == null) {
            return null;
        }

        PreparedStatement s;
        try {
            s = c.prepareStatement("select * from cfd where idativo = ?");
            s.setString(1,this.idAtivo);

            ResultSet resultSet = s.executeQuery();

            boolean moreRows;
            while(i >= 0) {
                moreRows = resultSet.next();
                if (!moreRows)
                    return getNegociador(c,i);
                i--;
            }
            int id = resultSet.getInt("id");
            LocalDateTime data = resultSet.getTimestamp("data").toLocalDateTime();
            double udativo = resultSet.getDouble("unidadesdeativo");
            double vpuc = resultSet.getDouble("valorporunidadenacompra");
            Double limitesup = resultSet.getDouble("limitesup");
            if (resultSet.wasNull())
                limitesup = null;
            Double limiteinf = resultSet.getDouble("limiteinf");
            if (resultSet.wasNull())
                limiteinf = null;
            String idAtivo = this.idAtivo;
            int nif = resultSet.getInt("nifnegociador");
            boolean aberto = resultSet.getBoolean("aberto");
            boolean longCFD = resultSet.getBoolean("long");

            Observer n;
            if (longCFD)
                n = new Long(id, data, udativo, vpuc, limiteinf, limitesup, idAtivo, nif, aberto);
            else
                n = new Short(id, data, udativo, vpuc, limiteinf, limitesup, idAtivo, nif, aberto);

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
    public Observer set(int i, Observer observer) {
        return null;
    }

    @Override
    public void add(int i, Observer observer) {

    }

    @Override
    public Observer remove(int i) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<Observer> listIterator() {
        return null;
    }

    @Override
    public ListIterator<Observer> listIterator(int i) {
        return null;
    }

    @Override
    public List<Observer> subList(int i, int i1) {
        return null;
    }
}
