package persistence;

import business.CFD;
import business.Long;
import business.Short;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class CFDDAO implements Map<Integer, CFD> {

    @Override
    public int size() {

        Connection c = Connect.connect();
        if (c == null){
            System.out.println("Can't connect!");
            return 0;
        }
        Statement s;
        int result=0;

        try{
            s= c.createStatement();

            ResultSet resultset = s.executeQuery("select count(*) from cfd;");
            resultset.next();
            result = resultset.getInt(1);
            resultset.close();

        }
        catch (SQLException e){
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
        if (c == null){
            System.out.println("Can't connect!");
            return false;
        }

        Integer key= (Integer) o;

        PreparedStatement s;
        boolean result= false;

        try {
            s= c.prepareStatement("select id from cfd where id = ?;");
            s.setInt(1,key);

            ResultSet resultset = s.executeQuery();
            result = resultset.isBeforeFirst();
            resultset.close();
        }
        catch (SQLException e){
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
    public CFD get(Object o) {

        Connection c = Connect.connect();
        if (c == null){
            System.out.println("Can't connect!");
            return null;
        }

        Integer key = (Integer) o;

        PreparedStatement s;

        try{
            s = c.prepareStatement("select * from cfd where id = ?;");
            s.setInt(1,key);

            ResultSet resultSet = s.executeQuery();
            // não há CFD com aquele id
            if(!resultSet.isBeforeFirst())
                return null;

            resultSet.next();

            int id = resultSet.getInt("id");
            LocalDateTime data = resultSet.getTimestamp("data").toLocalDateTime();
            double uniAtivo = resultSet.getDouble("unidadesdeativo");
            double valorUnidadeCompra = resultSet.getDouble("valorporunidadenacompra");
            Double limiteS = resultSet.getDouble("limiteSup");
            if (resultSet.wasNull())
                limiteS = null;
            Double limiteI = resultSet.getDouble("limiteInf");
            if (resultSet.wasNull())
                limiteI = null;
            boolean aberto = resultSet.getBoolean("aberto");
            int nifNeg = resultSet.getInt("nifNegociador");
            String idAtivo = resultSet.getString("idAtivo");
            boolean isLong = resultSet.getBoolean("long");

            CFD cfd;
            if (!aberto) {
                double valorUnidFim = resultSet.getDouble("valorporunidadenofim");
                if (isLong)
                    cfd = new Long(id, data, uniAtivo, valorUnidadeCompra, limiteI, limiteS, idAtivo, nifNeg, aberto, valorUnidFim);
                else
                    cfd = new Short(id, data, uniAtivo, valorUnidadeCompra, limiteI, limiteS, idAtivo, nifNeg, aberto, valorUnidFim);
            }
            else {
                if (isLong)
                    cfd = new Long(id, data, uniAtivo, valorUnidadeCompra, limiteI, limiteS, idAtivo, nifNeg, aberto);
                else
                    cfd = new Short(id, data, uniAtivo, valorUnidadeCompra, limiteI, limiteS, idAtivo, nifNeg, aberto);

            }

            resultSet.close();
            return cfd;


        }
        catch (SQLException e){
            e.printStackTrace();
        }

        Connect.close(c);

        return null;
    }


    @Override
    public CFD put(Integer integer, CFD cfd) {
        Connection c = Connect.connect();
        if (c == null){
            System.out.println("Can't connect!");
            return null;
        }

        if (integer != cfd.getId()){
            return null;
        }

        PreparedStatement s;
        try{
            if(this.containsKey(integer)){
                s = c.prepareStatement("update cfd set id = ?, data = ?, unidadesdeativo = ?, valorporunidadenacompra = ?, limiteInf = ?, limiteSup = ?, idAtivo = ? ,nifnegociador = ?,  aberto = ?, valorporunidadenofim = ?, long = ? where id = " + cfd.getId());
            }
            else
                s = c.prepareStatement("insert into cfd values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            s.setInt(1,cfd.getId());
            s.setTimestamp(2,Timestamp.valueOf(cfd.getData()));
            s.setDouble(3,cfd.getUnidadesDeAtivo());
            s.setDouble(4,cfd.getValorPorUnidadeNaCompra());
            if (cfd.getLimiteInf() != null)
                s.setDouble(5,cfd.getLimiteInf());
            else
                s.setNull(5, Types.DOUBLE);
            if (cfd.getLimitSup() != null)
                s.setDouble(6,cfd.getLimitSup());
            else
                s.setNull(6, Types.DOUBLE);
            s.setString(7,cfd.getIdAtivo());
            s.setInt(8,cfd.getNifNegociador());
            s.setBoolean(9,cfd.isAberto());
            s.setDouble(10,cfd.getValorPorUnidadeFinal());
            boolean isLong = cfd instanceof Long;
            s.setBoolean(11, isLong);

            int update = s.executeUpdate();

            if (update == 1)
                return cfd;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        Connect.close(c);

        return null;
    }

    @Override
    public CFD remove(Object o) {
        return null;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends CFD> map) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<Integer> keySet() {
        return null;
    }

    @Override
    public Collection<CFD> values() {
        return null;
    }

    @Override
    public Set<Entry<Integer, CFD>> entrySet() {
        return null;
    }
}
