package Servidor;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Ativo implements Subject {
	private int id;
	private float precoVenda;
	private float precoCompra;
	private String descricao;
	private ArrayList<Observer> observers_ContratoCompra;
	private ArrayList<Observer> observers_ContratoVenda;
	private ArrayList<Observer> observers_Utilizadores;

	/****************************************************************************************************/
	/*
											CONSTRUTORES
	 */
	/****************************************************************************************************/
	public Ativo( int id,float precoVenda, float precoCompra, String descricao)  {
		this.id=id;
		this.precoVenda = precoVenda;
		this.precoCompra = precoCompra;
		this.descricao = descricao;
		this.observers_ContratoCompra = new ArrayList<Observer>();
		this.observers_ContratoVenda = new ArrayList<Observer>();
		this.observers_Utilizadores = new ArrayList<Observer>();
	}

	public Ativo(Ativo a) {
		this.id = a.getId();
		this.precoCompra = a.getPrecoCompra();
		this.precoVenda = a.getPrecoVenda();
		this.descricao = a.getDescricao();
	}

	public Ativo() {
		this.id=0;
		this.precoVenda=0;
		this.precoCompra=0;
		this.descricao="";
	}

	public float getPrecoVenda() {
		return precoVenda;
	}

	public float getPrecoCompra() {
		return precoCompra;
	}

	public String getDescricao() {
		return descricao;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setPrecoVenda(float precoVenda) {
		this.precoVenda = precoVenda;
	}

	public void setPrecoCompra(float precoCompra) {
		this.precoCompra = precoCompra;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}


	public void setObservers_ContratoCompra(ArrayList<Observer> observers_ContratoCompra) {
		this.observers_Utilizadores = observers_Utilizadores;
	}

	public void setObservers_ContratoVenda(ArrayList<Observer> observers_ContratoVenda) {
		this.observers_Utilizadores = observers_Utilizadores;
	}

	public void setObservers_Utilizadores(ArrayList<Observer> observers_Utilizadores) {
		this.observers_Utilizadores = observers_Utilizadores;
	}

	public ArrayList<Observer> getObservers_ContratoCompra() {
		return observers_ContratoCompra;
	}

	public ArrayList<Observer> getObservers_ContratoVenda() {
		return observers_ContratoVenda;
	}

	public ArrayList<Observer> getObservers_Utilizadores() {
		return observers_Utilizadores;
	}

	public Ativo clone(){
		return  new Ativo(this);
	}

	@Override
	public String toString() {
		return "Ativo{" +
				"id=" + id +
				", precoVenda=" + precoVenda +
				", precoCompra=" + precoCompra +
				", descricao='" + descricao + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Ativo)) return false;
		Ativo ativo = (Ativo) o;
		return getId() == ativo.getId() &&
				Float.compare(ativo.getPrecoVenda(), getPrecoVenda()) == 0 &&
				Float.compare(ativo.getPrecoCompra(), getPrecoCompra()) == 0 &&
				Objects.equals(getDescricao(), ativo.getDescricao());
	}


	/*****************************************************************************/
	/*
										INTERFACE
	*/
	/*****************************************************************************/
	@Override
	public void registerObserverUtilizador(Observer o){
		this.observers_Utilizadores.add(o);
	}
	@Override
	public void registerObserverCompra(Observer o){
		this.observers_ContratoCompra.add(o);
	}
	@Override
	public void registerObserverVenda(Observer o){
		this.observers_ContratoVenda.add(o);
	}

	@Override
	public void removeObserverCompra(Observer o){
		if(this.observers_ContratoCompra.contains(o))
			this.observers_ContratoCompra.remove(o);
	}

	@Override
	public void removeObserverVenda(Observer o){
		if(this.observers_ContratoVenda.contains(o))
			this.observers_ContratoVenda.remove(o);
	}

	@Override
	public void removeObserverUtilizador(Observer o){
		if(this.observers_Utilizadores.contains(o))
			this.observers_Utilizadores.remove(o);
	}

	@Override
	public void notifyObserverCompra(){
		for(int i = 0; i < this.observers_ContratoCompra.size() ;i++){
			Observer o = (Observer) this.observers_ContratoCompra.get(i);
			o.update(this);
		}
	}

	@Override
	public void notifyObserverVenda(){
		for(int i = 0; i < this.observers_ContratoVenda.size() ;i++){
			Observer o = (Observer) this.observers_ContratoVenda.get(i);
			o.update(this);
		}
	}
	@Override
	public void notifyObserverUtilizador(){
		for(int i = 0; i < this.observers_Utilizadores.size() ;i++){
			Observer o = (Observer) this.observers_Utilizadores.get(i);
			o.update(this);
		}
	}

	public void getNewValues() {
		float compra,venda;
		Stock stock = null;
		try {
			stock = YahooFinance.get(this.getDescricao());
			BigDecimal precoVenda = stock.getQuote().getBid();
			BigDecimal precoCompra = stock.getQuote().getAsk();

			if(precoVenda != null){
				this.setPrecoVenda(precoVenda.floatValue());
			}

			if(precoCompra != null){
				this.setPrecoCompra(precoCompra.floatValue());
			}

			notifyObserverCompra();
			notifyObserverVenda();
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
}