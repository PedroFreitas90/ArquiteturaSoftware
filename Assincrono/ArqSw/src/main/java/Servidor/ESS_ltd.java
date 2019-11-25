package Servidor;
import DAOS.*;


import java.io.IOException;
import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.util.*;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class ESS_ltd {

	private UtilizadorDAO utilizadores;
	private AtivoDAO ativos;
	private ContratoDAO contratos;
	private RegistoDAO registos;
	private PedidoDAO pedidos;


	public ESS_ltd() {
		this.utilizadores = new UtilizadorDAO();
		this.ativos = new AtivoDAO();
		this.contratos = new ContratoDAO();
		this.registos = new RegistoDAO();
		this.pedidos = new PedidoDAO();

	}


	public  Utilizador iniciarSessao(String username, String password) throws UtilizadorInvalidoException {
		Utilizador u = verficaUtilizador(username);
		if (u != null) {
			String pass = u.getPassword();
			if (pass.equals(password))
				return u;
			else
				throw new UtilizadorInvalidoException("Username ou password errada");
		} else
			throw new UtilizadorInvalidoException("Username ou password errada");


	}

	public  void registarUtilizador(String username, String password, int saldo) throws UsernameInvalidoException {
		if ((verficaUtilizador(username)) != null)
			throw new UsernameInvalidoException("Username inválido");
		else {
			int id = utilizadores.size() + 1;
			List<Pedido.Memento> mementos = new ArrayList<>();
			Utilizador u = new Utilizador(id, username, password, saldo);
			utilizadores.put(id, u);
		}
	}

	public  Utilizador verficaUtilizador(String nome) {
		Utilizador user = null;
		for (Utilizador u : this.utilizadores.values()) {
			if (u.getUsername().equals(nome)) {
				user = u;
				break;
			}
		}

		return user;
	}

	public  float saldo(Utilizador u) {
		return this.utilizadores.get(u.getId()).getPlafom();
	}

	public  List<Ativo> listarAtivos() {
		List<Ativo> ativos = new ArrayList<>();
		for (Ativo a : this.ativos.values())
			ativos.add(a.clone());

		return ativos;

	}


	public  void criarContratoVenda(Utilizador u, int idAtivo, float takeprofit, float stoploss, int quantidade) throws AtivoInvalidoException, SaldoInsuficienteException {

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
			this.utilizadores.put(u.getId(), u);
			Contrato c = new Contrato(size, idAtivo, u.getId(), preco, takeprofit, stoploss, quantidade, false, false);
			this.contratos.put(size, c);//poe na lista total de contratos
		}
	}

	public  void criarContratoCompra(Utilizador u, int idAtivo, float takeprofit, float stoploss, int quantidade) throws AtivoInvalidoException, SaldoInsuficienteException {

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
			this.utilizadores.put(u.getId(), u);
			Contrato c = new Contrato(size, idAtivo, u.getId(), preco, takeprofit, stoploss, quantidade, true, false);
			this.contratos.put(size, c);//poe na lista total de contratos
		}
	}

	public  List<Contrato> listarPortefolio(Utilizador u) {
		List<Contrato> contratosUtilizador = new ArrayList<>();
		Collection<Contrato> contratos = this.contratos.values();
		for(Contrato c : contratos)
			if(c.getIdUtilizador()==u.getId())
				contratosUtilizador.add(c);
		return contratosUtilizador;
	}

	public  void fecharContrato(Utilizador u, int idContrato) throws ContratoInvalidoException {
		boolean sucess = false;
		Contrato c = this.contratos.get(idContrato,u.getId());
			if (c != null && !c.isEncerrado()) {
				sucess = true;
				if (c.isCompra())
					fecharContratoCompra(u, c);
				else
					fecharContratoVenda(u, c);
				return;
			}
		if (!sucess)
			throw new ContratoInvalidoException("Este contrato nao existe ou nao pertence ao utilizador");


	}


	public  Ativo criarAtivo(String ativo, int id) throws IOException, SocketTimeoutException {
		float compra, venda;
		BigDecimal  zero= new BigDecimal("0.0");
		Stock stock = YahooFinance.get(ativo);
		BigDecimal precoVenda = stock.getQuote().getBid();
		BigDecimal precoCompra = stock.getQuote().getAsk();
		if(precoCompra!=null && precoCompra.compareTo(zero)!=0)
			compra = precoCompra.floatValue();
		else
			return null;
		if(precoVenda!=null &&  precoVenda.compareTo(zero)!= 0)
			venda = precoVenda.floatValue();
		else
			return null;
		Ativo a = new Ativo(id, venda, compra, ativo);
		if (!this.ativos.containsValue(a))
			this.ativos.put(id, a);
		return a;


	}


	public  void fecharContratoCompra(Utilizador u, Contrato c) {
		int size = this.registos.size() + 1;
		int id_ativo = c.getIdAtivo();
		Ativo a = ativos.get(id_ativo).clone();// temos que ir ver o valor atual do ativo
		float valor_Atual = a.getPrecoCompra() * c.getQuantidade();
		float valor_compra = c.getPreco() * c.getQuantidade();
		float lucro = valor_Atual - valor_compra;
		u.setPlafom(u.getPlafom() + valor_compra + lucro);
		this.utilizadores.put(u.getId(), u);
		Registo r = new Registo(size, u.getId(), a.getId(), lucro, c.getQuantidade());
		this.registos.put(size, r);
		c.setEncerrado(true);
		this.contratos.put(c.getId(), c);
	}


	public  void fecharContratoVenda(Utilizador u, Contrato c) {
		int size = this.registos.size() + 1;
		int id_ativo = c.getIdAtivo();
		Ativo a = ativos.get(id_ativo).clone();// temos que ir ver o valor atual do ativo
		float valor_Atual = a.getPrecoVenda() * c.getQuantidade();
		float valor_venda = c.getPreco() * c.getQuantidade();
		float lucro = valor_venda - valor_Atual;
		u.setPlafom(u.getPlafom() + valor_venda + lucro);
		this.utilizadores.put(u.getId(), u);
		Registo r = new Registo(size, u.getId(), a.getId(), lucro, c.getQuantidade());
		this.registos.put(size, r);
		c.setEncerrado(true);
		this.contratos.put(c.getId(), c);
	}

	public List<Registo> listaRegistos(Utilizador u) {
		List<Registo> registos = new ArrayList<>();

		for (Registo r : this.registos.values())
			if (r.getIdUtilizador() == u.getId())
				registos.add(r);
		return registos;
	}

	public  void fecharContratosComLimites(Contrato c ) throws ContratoInvalidoException {
		Utilizador u;
		u = this.utilizadores.get(c.getIdUtilizador());
		fecharContrato(u, c.getId());
		ativos.get(c.getIdAtivo()).removeObserver(c);



	}


	public  void updateEstadoPedido(Pedido p) {

		int id = p.getEstado().getIdentificador();
		this.pedidos.put(id, p);
	}

	public int sizePedidos(){
		return this.pedidos.size();
	}

    public  Ativo getAtivo(String descricao){
	        return this.ativos.get(descricao,this);
    }

    public AtivoDAO getAtivos() {
        return ativos;
    }
}



