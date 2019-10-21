package Servidor;
import DAOS.AtivoDAO;
import DAOS.ContratoDAO;
import DAOS.RegistoDAO;
import DAOS.UtilizadorDAO;
import Servidor.Utilizador;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.Lock;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class ESS_ltd {

	private UtilizadorDAO utilizadores;
	private AtivoDAO ativos;
	private ContratoDAO contratos;
	private RegistoDAO registos;


	public ESS_ltd() throws IOException {
		this.utilizadores = new UtilizadorDAO();
		this.ativos = new AtivoDAO();
		this.contratos = new ContratoDAO();
		this.registos = new RegistoDAO();

	}


	public synchronized Utilizador iniciarSessao(String username, String password) throws UtilizadorInvalidoException {
		Utilizador u = verficaUtilizador(username);
		if (u != null) {
			String pass = u.getPassword();
			if (pass.equals(password))
			return u;
		} else
			throw new UtilizadorInvalidoException("Username ou password errada");

		return u;

	}

	public synchronized void registarUtilizador(String username, String password, int saldo) throws UsernameInvalidoException {
		if ((verficaUtilizador(username)) != null)
			throw new UsernameInvalidoException("Username inválido");
		else {
			int id = utilizadores.size() + 1;
			Utilizador u = new Utilizador(id, username, password, saldo);
			utilizadores.put(id, u);
		}
	}

	public synchronized Utilizador verficaUtilizador(String nome) {
		Utilizador user = null;
		for (Utilizador u : this.utilizadores.values()) {
			if (u.getUsername().equals(nome)) {
				user = u;
				break;
			}
		}

		return user;
	}

	public synchronized float saldo(Utilizador u) {
		return u.getPlafom();
	}

	public synchronized List<Ativo> listarAtivos() {
		List<Ativo> ativos = new ArrayList<>();
		for (Ativo a : this.ativos.values())
			ativos.add(a.clone());

		return ativos;

	}


	public synchronized void criarContratoVenda(Utilizador u, int idAtivo, float takeprofit, float stoploss, int quantidade) throws AtivoInvalidoException, SaldoInsuficienteException {

		Ativo a = this.ativos.get(idAtivo).clone();
		if (a == null)
			throw new AtivoInvalidoException("Ativo nao existe");
		else {
			int size = this.contratos.size() + 1;
			float preco = a.getPrecoVenda();
			float valor_total = preco * quantidade;
			float saldo = u.getPlafom();
			if (saldo < valor_total)
				throw new SaldoInsuficienteException("Saldo Insuficiente");
			u.setPlafom(u.getPlafom() - valor_total);
			this.utilizadores.put(u.getId(),u);
			Contrato c = new Contrato(size, idAtivo, u.getId(), preco, takeprofit, stoploss, quantidade, false, false);
            u.addContrato(c);//poe no portefolio
            this.contratos.put(size, c);//poe na lista total de contratos
		}
	}

	public synchronized void criarContratoCompra(Utilizador u, int idAtivo, float takeprofit, float stoploss, int quantidade) throws AtivoInvalidoException, SaldoInsuficienteException {

		Ativo a = this.ativos.get(idAtivo).clone();
		if (a == null)
			throw new AtivoInvalidoException("Ativo nao existe");
		else {
			int size = this.contratos.size() + 1;
			float preco = a.getPrecoVenda();
			float valor_total = preco * quantidade;
			float saldo = u.getPlafom();
			if (saldo < valor_total)
				throw new SaldoInsuficienteException("Saldo Insuficiente");
			u.setPlafom(u.getPlafom() - valor_total);
			this.utilizadores.put(u.getId(),u);
			Contrato c = new Contrato(size, idAtivo, u.getId(), preco, takeprofit, stoploss, quantidade, true, false);
			u.addContrato(c);//poe no portefolio
			this.contratos.put(size, c);//poe na lista total de contratos
		}
	}

	public synchronized List<Contrato> listarPortefolio(Utilizador u) {
		List<Contrato> contratos = u.getPortefolio();
		return contratos;
	}

	public synchronized void fecharContrato(Utilizador u, int idContrato) throws ContratoInvalidoException {
		boolean sucess = false;
		List<Contrato> contratos = u.getPortefolio();
		Contrato c = this.contratos.get(idContrato);
			for( Contrato cc :contratos)
				if(c!=null && c.getId()==cc.getId()) {
					sucess=true;
					if (c.isCompra())
						fecharContratoCompra(u, cc);
					else
						fecharContratoVenda(u, cc);
					return;
				}
			if(!sucess)
			throw new ContratoInvalidoException("Este contrato nao existe ou nao pertence ao utilizador");


	}


	public synchronized Ativo criarAtivo(String ativo,int id) throws IOException {
		float compra, venda;
		Stock stock = YahooFinance.get(ativo);
		BigDecimal precoVenda = stock.getQuote().getBid();
		BigDecimal precoCompra = stock.getQuote().getAsk();
		if (precoCompra != null)
			compra = precoCompra.floatValue();
		else
			compra = 0;
		if (precoVenda != null)
			venda = precoVenda.floatValue();
		else
			venda = 0;
		Ativo a = new Ativo(id, venda, compra, ativo);
		if(!this.ativos.containsValue(a))
		this.ativos.put(id, a);
		return a;


	}


	public synchronized void fecharContratoCompra(Utilizador u, Contrato c) {
		int size = this.registos.size() + 1;
		int id_ativo = c.getIdAtivo();
		Ativo a = ativos.get(id_ativo).clone();// temos que ir ver o valor atual do ativo
		float valor_Atual = a.getPrecoCompra() * c.getQuantidade();
		float valor_compra = c.getPreco() * c.getQuantidade();
		float lucro = valor_Atual - valor_compra;
		u.setPlafom(u.getPlafom() + valor_compra + lucro);
		this.utilizadores.put(u.getId(),u);
		Registo r = new Registo(size, u.getId(), a.getId(), lucro, c.getQuantidade());
		this.registos.put(size, r);
		c.setEncerrado(true);
		this.contratos.put(c.getId(),c);
	}


	public synchronized void fecharContratoVenda(Utilizador u, Contrato c) {
		int size = this.registos.size() + 1;
		int id_ativo = c.getIdAtivo();
		Ativo a = ativos.get(id_ativo).clone();// temos que ir ver o valor atual do ativo
		float valor_Atual = a.getPrecoVenda() * c.getQuantidade();
		float valor_venda = c.getPreco() * c.getQuantidade();
		float lucro = valor_venda - valor_Atual;
		u.setPlafom(u.getPlafom() + valor_venda + lucro);
		this.utilizadores.put(u.getId(),u);
		Registo r = new Registo(size, u.getId(), a.getId(), lucro, c.getQuantidade());
		this.registos.put(size, r);
		c.setEncerrado(true);
		this.contratos.put(c.getId(),c);
	}

	public synchronized   List<Registo> listaRegistos(Utilizador u) {
		List<Registo> registos = new ArrayList<>();

		for (Registo r : this.registos.values())
			if (r.getIdUtilizador() == u.getId())
				registos.add(r);
		return registos;
	}

	public synchronized void fecharContratosComLimites(Ativo a) throws ContratoInvalidoException {
		Utilizador u = null;
		float valor_atual;
		for (Contrato c : this.contratos.values()) {
			if (c.getIdAtivo() == a.getId() && !(c.isEncerrado()))
				if (c.isCompra()) {// se é de compra
					valor_atual = a.getPrecoCompra();
					if (valor_atual <= c.getStopLoss() || valor_atual >= c.getTakeProfit()) {
						u = this.utilizadores.get(c.getIdUtilizador());
						fecharContrato(u, c.getId());
					}

				} else {
					valor_atual = a.getPrecoVenda();
					if (valor_atual <= c.getStopLoss() || valor_atual >= c.getTakeProfit()) {
						u = this.utilizadores.get(c.getIdUtilizador());
						fecharContrato(u, c.getId());
					}
				}

		}

	}
}


