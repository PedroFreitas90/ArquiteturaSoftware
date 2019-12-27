package persistence;

import business.ativos.*;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class AtivoTipoDAO implements Map<String, List<Ativo>> {
    @Override
    public int size() {
        return AtivoConsts.TOTAL_TIPOS_ATIVOS;
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
    public List<Ativo> get(Object o) {
        Connection c = Connect.connect();
        if (c == null){
            System.out.println("Can't connect!");
            return null;
        }

        String tipo = (String) o;

        List<Ativo> ativos = new ArrayList<>();

        PreparedStatement s;

        try{
            s = c.prepareStatement(DAOHelper.getFullInformationForType(tipo));

            ResultSet resultSet = s.executeQuery();

            if (!resultSet.isBeforeFirst())
                return null;

            resultSet.next();

            while(!(resultSet.isAfterLast())){

                String id = resultSet.getString("idativo");
                String nome = resultSet.getString("nome");
                double valorPorUnidade = resultSet.getDouble("valorporunidade");

                if (tipo.equals("acao")){
                    ativos.add(DAOHelper.getAcao(resultSet,id,nome,valorPorUnidade));
                }

                if (tipo.equals("moeda")){
                    ativos.add(DAOHelper.getMoeda(resultSet,id,nome,valorPorUnidade));
                }

                if (tipo.equals("indice")){
                    ativos.add(DAOHelper.getIndice(resultSet,id,nome,valorPorUnidade));
                }

                if (tipo.equals("commodity")){
                    ativos.add(DAOHelper.getCommodity(resultSet,id,nome,valorPorUnidade));
                }

                resultSet.next();
            }

            Connect.close(c);
            return ativos;

        }
        catch (SQLException e){
            e.printStackTrace();
        }

        Connect.close(c);
        return null;
    }

    @Override
    public List<Ativo> put(String s, List<Ativo> ativos) {
        return null;
    }

    @Override
    public List<Ativo> remove(Object o) {
        return null;
    }

    @Override
    public void putAll(Map<? extends String, ? extends List<Ativo>> map) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<String> keySet() {
        return null;
    }

    @Override
    public Collection<List<Ativo>> values() {
        return null;
    }

    @Override
    public Set<Entry<String, List<Ativo>>> entrySet() {
        return null;
    }
}
