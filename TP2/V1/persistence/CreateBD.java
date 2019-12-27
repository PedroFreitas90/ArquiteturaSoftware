package persistence;

import business.AtivoManager;
import business.ativos.*;
import business.mercado.*;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CreateBD {
    private static void createRelations(Connection c) {
        Statement s = null;
        try {
            s = c.createStatement();

            s.executeUpdate("drop table if exists Ativo cascade;");
            s.executeUpdate("create table Ativo (Id varchar primary key, Nome varchar, ValorPorUnidade float);");

            s.executeUpdate("drop table if exists Acao cascade;");
            s.executeUpdate("create table Acao (Id int primary key, Empresa varchar);");

            s.executeUpdate("drop table if exists AcaoAtivo cascade;");
            s.executeUpdate("create table AcaoAtivo (IdAcao int references Acao(id), IdAtivo varchar references Ativo(id), primary key (IdAcao, IdAtivo));");


            s.executeUpdate("drop table if exists Indice cascade;");
            s.executeUpdate("create table Indice (Id int primary key, NumEmpresas int);");

            s.executeUpdate("drop table if exists IndiceAtivo cascade;");
            s.executeUpdate("create table IndiceAtivo (IdIndice int references Indice(id), IdAtivo varchar references Ativo(id), primary key (IdIndice, IdAtivo));");

            s.executeUpdate("drop table if exists Commodity cascade;");
            s.executeUpdate("create table Commodity (Id int primary key, Pais varchar);");

            s.executeUpdate("drop table if exists CommodityAtivo cascade;");
            s.executeUpdate("create table CommodityAtivo (IdCommodity int references Commodity(id), IdAtivo varchar references Ativo(id), primary key (IdCommodity, IdAtivo));");

            s.executeUpdate("drop table if exists Moeda cascade;");
            s.executeUpdate("create table Moeda (Id int primary key, MoedaA varchar, MoedaB varchar);");

            s.executeUpdate("drop table if exists MoedaAtivo cascade;");
            s.executeUpdate("create table MoedaAtivo (IdMoeda int references Moeda(id), IdAtivo varchar references Ativo(id), primary key (IdMoeda, IdAtivo));");

            s.executeUpdate("drop table if exists Negociador cascade;");
            s.executeUpdate("create table Negociador(Nif int primary key, Nome varchar, Email varchar, Password varchar, Saldo float);");

            s.executeUpdate("drop table if exists NegociadorAtivo cascade;");
            s.executeUpdate("create table NegociadorAtivo (IdNegociador int references Negociador(nif), IdAtivo varchar references Ativo(id), ValorOriginal float, primary key (IdNegociador, IdAtivo));");

            s.executeUpdate("drop table if exists CFD cascade;");
            s.executeUpdate("create table CFD (Id int primary key, Data timestamp, UnidadesDeAtivo float," +
                    "ValorPorUnidadeNaCompra float, LimiteSup float, LimiteInf float, " +
                    "IdAtivo varchar references ativo(id) on delete cascade, NifNegociador int references Negociador(nif)" +
                    ", Aberto boolean, ValorPorUnidadeNoFim float, long boolean);");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void populateRelations(Connection c) {
        Statement s;
        try {
            s = c.createStatement();
            s.executeUpdate("insert into negociador values('666666666', 'Gato das Botas', 'botas@gato.pt', 'miau', 0)");
            s.executeUpdate("insert into negociador values('999999999', 'Botas do Gato', 'gato@botas.pt', 'buts', 0)");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void populateAtivos() {
        Mercado m = new IntrinioAPI();
        Map<String, Ativo> ativos = new AtivoDAO();
        populateAcoes(m, ativos);
        System.out.println("Started populating assets!");
        populateIndices(m, ativos);
        populateMoedas(m, ativos);
        populateCommodities(m, ativos);
        System.out.println("Finished populating assets!");
    }

    private static void populateAcoes(MercadoAcao m, Map<String, Ativo> ativos) {
        List<String> acoesId = m.getAcoes();
        for(String id : acoesId) {
            String nome = m.getNomeAcao(id);
            double vpu = m.getCotacaoAcao(id);
            String empresa = m.getEmpresaAcao(id);
            Ativo a = new Acao(id, nome, vpu, empresa);
            ativos.put(id, a);
        }
    }

    private static void populateIndices(MercadoIndice m, Map<String, Ativo> ativos) {
        List<String> indicesId = m.getIndices();
        for(String id : indicesId) {
            String nome = m.getNomeIndice(id);
            double vpu = m.getCotacaoIndice(id);
            Ativo a = new Indice(id, nome, vpu);
            ativos.put(id, a);
        }
    }

    private static void populateMoedas(MercadoMoeda m, Map<String, Ativo> ativos) {
        List<String> moedasId = m.getMoedas();
        for(String id : moedasId) {
            String nome = id;
            double vpu = m.getCotacaoMoeda(id);
            String moedaA = m.getMoedaBase(id);
            String moedaB = m.getMoedaQuota(id);
            Ativo a = new Moeda(id, nome, vpu, moedaA, moedaB);
            ativos.put(id,a);
        }
    }

    private static void populateCommodities(MercadoCommodity m, Map<String, Ativo> ativos) {
        List<String> commoditiesId = m.getCommodities();
        for(String id : commoditiesId) {
            String nome = m.getNomeCommodity(id);
            double vpu = m.getCotacaoCommodity(id);
            String pais = m.getPaisCommodity(id);
            Ativo a = new Commodity(id, nome, vpu, pais);
            ativos.put(id, a);
        }
    }


    public static void main(String[] args) {
        Connection c = Connect.connect();
        if (c == null) {
            System.out.println("Can't connect!");
            return;
        }
        createRelations(c);
        populateRelations(c);
        populateAtivos();

        Connect.close(c);
    }
}
