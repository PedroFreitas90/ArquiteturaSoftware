package persistence;

import business.ativos.*;

import java.sql.*;

public class DAOHelper {

    protected static Acao getAcao(ResultSet rs, String id, String nome, double valorPorUnidade){

        try {
            String empresa = rs.getString("empresa");
            Acao c = new Acao(id, nome,valorPorUnidade,empresa);
            return c;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    protected static Moeda getMoeda(ResultSet rs, String id, String nome, double valorPorUnidade){

        try {
            String moedaA = rs.getString("moedaa");
            String moedaB = rs.getString("moedab");
            Moeda m = new Moeda(id, nome,valorPorUnidade,moedaA,moedaB);
            return m;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    protected static Indice getIndice(ResultSet rs, String id, String nome, double valorPorUnidade){

        try {
            int numEmpresa = rs.getInt("numempresas");
            Indice i = new Indice(id, nome,valorPorUnidade,numEmpresa);
            return i;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    protected static Commodity getCommodity(ResultSet rs, String id, String nome, double valorPorUnidade){

        try {
            String pais = rs.getString("pais");
            Commodity c = new Commodity(id, nome,valorPorUnidade,pais);
            return c;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    protected static String getFullInformationForType(String tipo){
        return "select * from " + tipo +"ativo INNER JOIN ativo ON ativo.id = "+ tipo + "ativo.idativo INNER JOIN "+ tipo +" ON " + tipo +".id = "+ tipo +"ativo.id"+ tipo +";";
    }

    protected static int sizeAtivoPorTipo(Connection c, String tipo) {

        Statement s;
        int result = 0;

        try {

            s = c.createStatement();

            ResultSet resultSet = s.executeQuery("select count(*) from " + tipo+";");
            resultSet.next();
            result = resultSet.getInt(1);
            resultSet.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    protected static Ativo putAcao(Connection c, Ativo ativo){

        PreparedStatement s;

        try {
            int res;
            int id = sizeAtivoPorTipo(c,"acao");

            s = c.prepareStatement("insert into acao values (?,?);");
            s.setInt(1, id);
            s.setString(2, ((Acao) ativo).getEmpresa());
            s.executeUpdate();


            s = c.prepareStatement("insert into acaoativo values (?,?);");
            s.setInt(1, id);
            s.setString(2, ativo.getId());
            res = s.executeUpdate();

            if (res == 1)
                return ativo;

        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    protected static Ativo putMoeda(Connection c, Ativo ativo){

        PreparedStatement s;

        try {
            int res;
            int id = sizeAtivoPorTipo(c,"moeda");

            s = c.prepareStatement("insert into moeda values (?,?,?);");
            s.setInt(1, id);
            s.setString(2, ((Moeda) ativo).getMoedaA());
            s.setString(3, ((Moeda) ativo).getMoedaB());
            s.executeUpdate();

            s = c.prepareStatement("insert into moedaativo values (?,?);");
            s.setInt(1, id);
            s.setString(2, ativo.getId());
            res = s.executeUpdate();

            if (res ==1){
                return ativo;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    protected static Ativo putIndice(Connection c, Ativo ativo){

        PreparedStatement s;

        try{
            int res;
            int id = sizeAtivoPorTipo(c,"indice");
            s = c.prepareStatement("insert into indice values (?,?);");
            s.setInt(1, id);
            s.setInt(2, ((Indice) ativo).getNumEmpresas());
            s.executeUpdate();
            s = c.prepareStatement("insert into indiceativo values (?,?);");
            s.setInt(1, id);
            s.setString(2, ativo.getId());
            res = s.executeUpdate();

            if (res ==1){
                return ativo;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    protected static Ativo putCommodity(Connection c, Ativo ativo){

        PreparedStatement s;

        try{
            int res;
            int id = sizeAtivoPorTipo(c,"commodity");

            s = c.prepareStatement("insert into commodity values (?,?);");
            s.setInt(1, id);
            s.setString(2, ((Commodity) ativo).getPais());
            s.executeUpdate();

            s = c.prepareStatement("insert into commodityativo values (?,?);");
            s.setInt(1, id);
            s.setString(2, ativo.getId());
            res = s.executeUpdate();

            if (res ==1){
                return ativo;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    protected static String getClassAtivo( Connection c, String id){

        PreparedStatement s;
        String[] ativos = {"acao","indice","moeda","commodity"};

        try {
            for(int i = 0; i < ativos.length; i++) {
                s = c.prepareStatement("select * from " + ativos[i] +"ativo"+ " where idAtivo = ?;");
                s.setString(1,id);

                ResultSet resultSet = s.executeQuery();
                if(resultSet.isBeforeFirst())  {
                    resultSet.close();
                    return ativos[i];
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected static Acao getAcao(Connection c, String id, String nome, double valorPorUnidade){

        PreparedStatement s;

        try {
            s = c.prepareStatement("select * from acaoativo inner join acao ON acao.id = acaoativo.idacao where acaoativo.idativo = ?");
            s.setString(1,id);

            ResultSet resultSet = s.executeQuery();

            if (!resultSet.isBeforeFirst())
                return null;

            resultSet.next();
            String empresa = resultSet.getString("empresa");

            return (new Acao(id,nome,valorPorUnidade,empresa));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected static Moeda getMoeda(Connection c, String id, String nome, double valorPorUnidade){

        PreparedStatement s;

        try {
            s = c.prepareStatement("select * from moedaativo inner join moeda ON moeda.id = moedaativo.idmoeda where moedaativo.idativo = ?");
            s.setString(1,id);

            ResultSet resultSet = s.executeQuery();

            if (!resultSet.isBeforeFirst())
                return null;

            resultSet.next();
            String moedaA = resultSet.getString("moedaa");
            String moedaB = resultSet.getString("moedab");

            return (new Moeda(id,nome,valorPorUnidade,moedaA,moedaB));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected static Indice getIndice(Connection c, String id, String nome, double valorPorUnidade){

        PreparedStatement s;

        try {
            s = c.prepareStatement("select * from indiceativo inner join indice ON indice.id = indiceativo.idindice where indiceativo.idativo = ?");
            s.setString(1,id);
            ResultSet resultSet = s.executeQuery();
            if (!resultSet.isBeforeFirst())
                return null;
            resultSet.next();
            int num_empresas = resultSet.getInt("numempresas");
            return (new Indice(id,nome,valorPorUnidade,num_empresas));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected static Commodity getCommodity(Connection c, String id, String nome, double valorPorUnidade){

        PreparedStatement s;

        try {
            s = c.prepareStatement("select * from commodityativo inner join commodity ON commodity.id = commodityativo.idcommodity where commodityativo.idativo = ?");
            s.setString(1,id);
            ResultSet resultSet = s.executeQuery();
            if (!resultSet.isBeforeFirst())
                return null;
            resultSet.next();
            String pais = resultSet.getString("pais");
            return (new Commodity(id,nome,valorPorUnidade,pais));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
