package business.facade;

import business.ativos.Ativo;
import business.CFD;
import business.exceptions.CFDNaoExisteException;
import business.exceptions.NegociadorNaoExisteException;
import business.exceptions.NegociadorNaoPossuiSaldoSuficienteException;

import java.util.List;

public interface FacadeNegociador {
    boolean registarNegociador(int nif, String nome, String email, String password, double saldo);
    List<Ativo> getAtivos();
    List<Ativo> getAtivos(String tipo);
    Ativo getAtivo(String id);
    CFD registarCFD(String idAtivo, int nifNegociador, double unidadesDeCompra, Double limiteMin, Double limiteMax, String tipo) throws NegociadorNaoExisteException, NegociadorNaoPossuiSaldoSuficienteException;
    double fecharCFD(int id) throws CFDNaoExisteException;
    List<CFD> getCFDs(int nifNegociador) throws NegociadorNaoExisteException;
    double atualizarSaldo(int nif, double quantia) throws NegociadorNaoExisteException;
    boolean verificarCredenciais(int nif, String pass);
    double getSaldo(int nif);
    double getValorAtualCFD(int idCFD);
    void seguirAtivo(int nif, String idAtivo);
    List<Ativo> getAtivosSubscritos(int nif);
}
