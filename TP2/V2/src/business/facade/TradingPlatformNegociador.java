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

    /**
     * @param nif      Nif do negociador
     * @param nome     Nome do negociador
     * @param email    Email do negociador
     * @param password Password do negociador
     * @param saldo    Saldo inicial do negociador
     * @return True se foi possível registar o negociador, false se não
     */
    public boolean registarNegociador(int nif, String nome, String email, String password, double saldo) {
        Negociador n = new Negociador(nif, nome, email, password, saldo);
        if (this.negociadores.containsKey(nif))
            return false;
        this.negociadores.put(nif, n);
        return true;
    }

    /**
     * @return Todos os ativos disponíveis no sistema
     */
    public List<Ativo> getAtivos() {
        return new ArrayList<>(this.ativos.getAll());
    }

    /**
     * @param tipo Tipo de ativo
     * @return Lista de Ativos desse tipo
     */
    public List<Ativo> getAtivos(String tipo) {
        return this.ativos.getPorTipo(tipo);
    }

    /**
     * @param id Id do ativo
     * @return O Ativo pretendido, ou null se não existir
     */
    public Ativo getAtivo(String id) {
        return this.ativos.get(id);
    }

    /**
     * @param idAtivo          id do ativo ao qual deve estar associado o CFD
     * @param nifNegociador    nif de quem estabelece o CFD
     * @param unidadesDeCompra numero de unidades do ativo que pretende adquirir
     * @param limiteMin        stoploss, pode ser null
     * @param limiteMax        maxprofit, pode ser null
     * @param tipo             Long ou short
     * @return o CFD registado
     * @throws NegociadorNaoExisteException                caso se tente registar um CFD de um negociador que não está
     *                                                     registado na plataforma
     * @throws NegociadorNaoPossuiSaldoSuficienteException caso se tente estabelecer um CFD mas o negociador
     *                                                     não possui saldo suficiente
     */
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

    /**
     * @param id do CFD
     * @return o valor de fecho do CFD, isto é, quanto recebeu o negociador na sua carteira
     * @throws CFDNaoExisteException caso se tente encerrar um CFD não existente
     */
    public double fecharCFD(int id) throws CFDNaoExisteException {
        if (!this.cfds.containsKey(id))
            throw new CFDNaoExisteException(id);


        CFD c = this.cfds.get(id);
        Ativo a = this.ativos.get(c.getIdAtivo());
        c.fecharCFD(a.getValorPorUnidade());
        this.cfds.put(id, c); // to update state

        double saldoAAdicionar = c.getGanhoDoFecho();

        try {
            this.atualizarSaldo(c.getNifNegociador(), saldoAAdicionar);
        } catch (NegociadorNaoExisteException e) {
            saldoAAdicionar = 0;
        }

        return saldoAAdicionar;

    }

    /**
     * @param nifNegociador nif do negociador que deseja obter a informação
     * @return retorna a lista de CFDs abertos do negociador
     */
    public List<CFD> getCFDs(int nifNegociador) throws NegociadorNaoExisteException {
        if (!this.negociadores.containsKey(nifNegociador))
            throw new NegociadorNaoExisteException(nifNegociador);

        Negociador n = this.negociadores.get(nifNegociador);
        return n.getCFDsAbertos();
    }

    /**
     * @param nif     nif do utilizador
     * @param quantia quantia a adicionar
     * @return saldo atual do negociador
     * @throws NegociadorNaoExisteException
     */
    public double atualizarSaldo(int nif, double quantia) throws NegociadorNaoExisteException {
        if (!this.negociadores.containsKey(nif))
            throw new NegociadorNaoExisteException(nif);

        Negociador n = this.negociadores.get(nif);
        double saldo = n.adicionarSaldo(quantia);
        this.negociadores.put(nif, n);

        return saldo;
    }

    /**
     * @param nif      nif do negociador
     * @param password pass a verificar
     * @return se as credenciais do negociador em causa correspondem aos valores dados
     */
    public boolean verificarCredenciais(int nif, String password) {
        if (!this.negociadores.containsKey(nif))
            return false;

        Negociador n = this.negociadores.get(nif);
        return n.verificarCredenciais(nif, password);
    }

    /**
     * @param nif nif do negociador
     * @return saldo atual do negociador (ou 0 se este não existir)
     */
    @Override
    public double getSaldo(int nif) {
        Negociador n = this.negociadores.get(nif);
        if (n == null)
            return 0;
        else
            return n.getSaldo();
    }

    /**
     * @param idCFD id do CFD
     * @return valorização atual do CFD
     */
    public double getValorAtualCFD(int idCFD) {
        CFD c = this.cfds.get(idCFD);
        Ativo a = this.ativos.get(c.getIdAtivo());
        return c.getValorCFD(a.getValorPorUnidade());
    }

    /**
     * @param nif     Negociador
     * @param idAtivo id do ativo que pretende seguir
     */
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