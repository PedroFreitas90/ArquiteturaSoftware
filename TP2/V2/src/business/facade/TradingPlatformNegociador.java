package business.facade;

import business.*;
import business.Long;
import business.Short;
import business.ativos.Ativo;
import business.exceptions.CFDNaoExisteException;
import business.exceptions.NegociadorNaoExisteException;
import business.exceptions.NegociadorNaoPossuiSaldoSuficienteException;
import persistence.CFDDAO;
import persistence.NegociadorDAO;

import java.time.LocalDateTime;
import java.util.*;

public class TradingPlatformNegociador implements FacadeNegociador {
	
    private AtivoManager ativos;
    private Map<Integer, CFD> cfds;
    private Map<Integer, Negociador> negociadores;

    public TradingPlatformNegociador() {
        this.ativos = new AtivoManager();
        this.cfds = new CFDDAO();
        this.negociadores = new NegociadorDAO();
    }

    public boolean registarNegociador(int nif, String nome, String email, String password, double saldo) {
        Negociador n = new Negociador(nif, nome, email, password, saldo);
        if (this.negociadores.containsKey(nif))
            return false;
        this.negociadores.put(nif, n);
        return true;
    }

    public List<Ativo> getAtivos() {
        return new ArrayList<>(this.ativos.getAll());
    }

    public List<Ativo> getAtivos(String tipo) {
        return this.ativos.getPorTipo(tipo);
    }

    public Ativo getAtivo(String id) {
        return this.ativos.get(id);
    }

    public CFD registarCFD(String idAtivo, int nifNegociador, double unidadesDeCompra, Double limiteMin, Double limiteMax, String tipo)
            throws NegociadorNaoExisteException, NegociadorNaoPossuiSaldoSuficienteException {
        if (!this.negociadores.containsKey(nifNegociador))
            throw new NegociadorNaoExisteException(nifNegociador);

        Ativo ativo = this.ativos.get(idAtivo);
        Negociador n = this.negociadores.get(nifNegociador);
        double investimento = unidadesDeCompra * ativo.getValorPorUnidade();
        if (!n.podeGastar(investimento))
            throw new NegociadorNaoPossuiSaldoSuficienteException(investimento, nifNegociador);

        CFD c = inserirCFD(ativo,nifNegociador,unidadesDeCompra,limiteMin,limiteMax,tipo,investimento);
        return c;
    }

    private CFD inserirCFD(Ativo ativo, int nifNegociador, double unidadesDeCompra, Double limiteMin, Double limiteMax, String tipo,double investimento) throws NegociadorNaoExisteException {
        int id = cfds.size();
        CFD c;
        if (tipo.equals("Long")) {
            c = new Long(id, LocalDateTime.now(), unidadesDeCompra, ativo.getValorPorUnidade(), limiteMin, limiteMax, ativo.getId(), nifNegociador, true);
        } else
            c = new Short(id, LocalDateTime.now(), unidadesDeCompra, ativo.getValorPorUnidade(), limiteMin, limiteMax, ativo.getId(), nifNegociador, true);
        this.atualizarSaldo(nifNegociador, -investimento);
        this.cfds.put(c.getId(), c);

        ativo.registerObserver(c);
        return  c;
    }

    public double fecharCFD(int id) throws CFDNaoExisteException {
        if (!this.cfds.containsKey(id))
            throw new CFDNaoExisteException(id);
        CFD c = this.cfds.get(id);
        Ativo a = this.ativos.get(c.getIdAtivo());
        c.fecharCFD(a.getValorPorUnidade());
        this.cfds.put(id, c);

        double saldoAAdicionar = c.getGanhoDoFecho();

        try {
            this.atualizarSaldo(c.getNifNegociador(), saldoAAdicionar);
        } catch (NegociadorNaoExisteException e) {
            saldoAAdicionar = 0;
        }

        return saldoAAdicionar;

    }

    public List<CFD> getCFDs(int nifNegociador) throws NegociadorNaoExisteException {
        if (!this.negociadores.containsKey(nifNegociador))
            throw new NegociadorNaoExisteException(nifNegociador);

        Negociador n = this.negociadores.get(nifNegociador);
        return n.getCFDsAbertos();
    }

    public double atualizarSaldo(int nif, double quantia) throws NegociadorNaoExisteException {
        if (!this.negociadores.containsKey(nif))
            throw new NegociadorNaoExisteException(nif);

        Negociador n = this.negociadores.get(nif);
        double saldo = n.adicionarSaldo(quantia);
        this.negociadores.put(nif, n);

        return saldo;
    }

    public boolean verificarCredenciais(int nif, String password) {
        if (!this.negociadores.containsKey(nif))
            return false;

        Negociador n = this.negociadores.get(nif);
        return n.verificarCredenciais(nif, password);
    }

    @Override
    public double getSaldo(int nif) {
        Negociador n = this.negociadores.get(nif);
        if (n == null)
            return 0;
        else
            return n.getSaldo();
    }

    public double getValorAtualCFD(int idCFD) {
        CFD c = this.cfds.get(idCFD);
        Ativo a = this.ativos.get(c.getIdAtivo());
        return c.getValorCFD(a.getValorPorUnidade());
    }

    public void seguirAtivo(int nif, String idAtivo) {
        Negociador n = this.negociadores.get(nif);
        Ativo a = this.ativos.get(idAtivo);
        n.seguirAtivo(a);
    }

    public List<Ativo> getAtivosSubscritos(int nif) {
        Negociador n = this.negociadores.get(nif);
        List<Ativo> ativos = n.getAtivos();
        return ativos;

    }

}