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
	private List<Observer> observers;




	public Ativo( int id,float precoVenda, float precoCompra, String descricao) {
		this.id=id;
		this.precoVenda = precoVenda;
		this.precoCompra = precoCompra;
		this.descricao = descricao;
		this.observers = new ArrayList<>();
	}
	public Ativo(Ativo a) {
		this.id = a.getId();
		this.precoCompra = a.getPrecoCompra();
		this.precoVenda = a.getPrecoVenda();
		this.descricao = a.getDescricao();
		this.observers = new ArrayList<>();
	}

	public Ativo() {
		this.id=0;
		this.precoVenda=0;
		this.precoCompra=0;
		this.descricao="";
		this.observers = new ArrayList<>();
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

	public Ativo clone(){
		return  new Ativo(this);
	}

	@Override
	public String toString() {
		return " " + id + "  | " + descricao + "  | " + precoCompra + "  | " + precoVenda + "\n";
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
	public void registerObserver(Observer o){
		this.observers.add(o);
	}

	@Override
	public void removeObserver(Observer o){
		if(this.observers.contains(o))
			this.observers.remove(o);
	}

	@Override
	public void notifyObserver(){
		for(int i = 0; i < this.observers.size() ;i++){
			Observer o = (Observer) this.observers.get(i);
			o.update(this);
		}
	}
    /*****************************************************************************/

	public void setValores(float precoVenda,float precoCompra) {
			this.precoCompra=precoCompra;
			this.precoVenda=precoVenda;
			notifyObserver();
	}







}