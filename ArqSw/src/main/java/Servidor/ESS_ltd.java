package Servidor;
import Servidor.Utilizador;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.Lock;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class ESS_ltd {

	private Map<Integer,Utilizador> utilizadores;
	private Utilizador utilizador;
	private Map<Integer,Ativo> ativos;
	private Map<Integer,Contrato> contratos;
	private Map<Integer,Registo> registos;


	public ESS_ltd() throws IOException {
		this.utilizador=null;
		this.utilizadores= new HashMap<>();
		this.ativos= new HashMap<>();
		this.contratos=new HashMap<>();
		this.registos = new HashMap<>();
		Utilizador u = new Utilizador(1,"ze","ze",20000);
		this.utilizadores.put(1,u);

	}


	public Utilizador iniciarSessao(String username,String password) throws UtilizadorInvalidoException {
			Utilizador u = verficaUtilizador(username);
		 if(u!=null){
		 	String pass= u.getPassword();
		 	if(pass.equals(password))
		 		this.utilizador=u;
		 	return u;
		 }
		else{
			throw new UtilizadorInvalidoException("Username ou password errada");
		 }


	}
	public synchronized void registarUtilizador(String username,String password,int saldo) throws UsernameInvalidoException {
		if ((verficaUtilizador(username))!=null)
			throw new UsernameInvalidoException("Username inválido");
		else {
			int id = utilizadores.size() + 1;
			Utilizador u = new Utilizador(id, username, password, saldo);
			utilizadores.put(id, u);
		}
	}

public Utilizador verficaUtilizador(String nome){
				Utilizador user = null;
			for( Utilizador u :this.utilizadores.values()) {
					if (u.getUsername().equals(nome)) {
						user=u;
						break;
					}
				}

		return user;
	}

	public synchronized float saldo (Utilizador u){
		return u.getPlafom();
	}

	public synchronized  List<Ativo> listarAtivos(){
		List<Ativo> ativos = new ArrayList<>();
		for(Ativo a: this.ativos.values())
				ativos.add(a.clone());

		return ativos;

	}


	public synchronized void criarContratoVenda(Utilizador u, int idAtivo, float takeprofit,float stoploss,int quantidade) throws AtivoInvalidoException, SaldoInsuficienteException {

		Ativo a = this.ativos.get(idAtivo).clone();
		if(a==null)
			throw new AtivoInvalidoException("Ativo nao existe");
		else {
			int size = this.contratos.size()+1;
			 float preco = a.getPrecoVenda();
			float valor_total = preco*quantidade;
			float saldo = u.getPlafom();
			if(saldo<valor_total)
				throw new SaldoInsuficienteException("Saldo Insuficiente");
			u.setPlafom(u.getPlafom()-valor_total);
			Contrato c = new Contrato(size,idAtivo,u.getId(),preco,takeprofit,stoploss,quantidade,false,false);
			this.utilizadores.get(u.getId()).addContrato(c);//poe no portefolio
			this.contratos.put(size,c);//poe na lista total de contratos
		}
	}

	public synchronized void criarContratoCompra(Utilizador u, int idAtivo, float takeprofit,float stoploss,int quantidade) throws AtivoInvalidoException, SaldoInsuficienteException {

		Ativo a = this.ativos.get(idAtivo).clone();
		if(a==null)
			throw new AtivoInvalidoException("Ativo nao existe");
		else {
			int size = this.contratos.size()+1;
			float preco = a.getPrecoVenda();
			float valor_total = preco*quantidade;
			float saldo = u.getPlafom();
			if(saldo<valor_total)
				throw new SaldoInsuficienteException("Saldo Insuficiente");
			u.setPlafom(u.getPlafom()-valor_total);
			Contrato c = new Contrato(size,idAtivo,u.getId(),preco,takeprofit,stoploss,quantidade,true,false);
			this.utilizadores.get(u.getId()).addContrato(c);//poe no portefolio
			this.contratos.put(size,c);//poe na lista total de contratos
		}
	}

	public List<Contrato> listarPortefolio(Utilizador u){
		List<Contrato> contratos = u.getPortefolio();
		return contratos;
	}

	public synchronized void fecharContrato(Utilizador u,int idContrato ) throws ContratoInvalidoException {
			List<Contrato> contratos = u.getPortefolio();
			Contrato c = this.contratos.get(idContrato);
		if(c==null || !(contratos.contains(c)))
			 	throw new ContratoInvalidoException("Este contrato nao existe ou nao pertence ao utilizador");
			 	if(c.isCompra())
			 		fecharContratoCompra(u,c);
			 	else
			 		fecharContratoVenda(u,c);


	}


	public Ativo criarAtivo(String ativo) throws IOException {
		float compra,venda;
		int size = this.ativos.size();
		Stock stock = YahooFinance.get(ativo);
		BigDecimal precoVenda=stock.getQuote().getBid();
		BigDecimal precoCompra =stock.getQuote().getAsk();
		if(precoCompra!=null)
			compra = precoCompra.floatValue();
		else
			compra=0;
		if(precoVenda!=null)
			venda = precoVenda.floatValue();
		else
			venda=0;


		Ativo a = new Ativo(size,venda,compra,ativo);
		this.ativos.put(size,a);
		return a;


	}



public void fecharContratoCompra(Utilizador u ,Contrato c) {
	int size = this.registos.size()+1;
	int id_ativo = c.getIdAtivo();
	Ativo a = ativos.get(id_ativo).clone();// temos que ir ver o valor atual do ativo
	float valor_Atual = a.getPrecoCompra() * c.getQuantidade();
	float valor_compra = c.getPreco() * c.getQuantidade();
	float lucro = valor_Atual - valor_compra;
	u.setPlafom(u.getPlafom() + valor_compra + lucro);
	Registo r  = new Registo(size,u.getId(),a.getId(),lucro,c.getQuantidade());
	this.registos.put(size,r);
	c.setEncerrado(true);
}


	public void fecharContratoVenda(Utilizador u ,Contrato c) {
		int size = this.registos.size()+1;
		int id_ativo = c.getIdAtivo();
		Ativo a = ativos.get(id_ativo).clone();// temos que ir ver o valor atual do ativo
		float valor_Atual = a.getPrecoVenda() * c.getQuantidade();
		float valor_venda = c.getPreco() * c.getQuantidade();
		float lucro = valor_venda - valor_Atual;
		u.setPlafom(u.getPlafom() +valor_venda + lucro);
		Registo r  = new Registo(size,u.getId(),a.getId(),lucro,c.getQuantidade());
		this.registos.put(size,r);
		c.setEncerrado(true);
	}

	public List<Registo> listaRegistos(Utilizador u){
		List<Registo> registos  = new ArrayList<>();

		for( Registo r :this.registos.values())
				if(r.getIdUtilizador()==u.getId())
					registos.add(r);
	return registos;
	}


}

