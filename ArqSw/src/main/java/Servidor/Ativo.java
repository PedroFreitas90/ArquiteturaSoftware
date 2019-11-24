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
	private List<Observer> observers_ContratoCompra;
	private List<Observer> observers_ContratoVenda;
	private ArrayList<Observer> observers_Utilizadores;



	public Ativo( int id,float precoVenda, float precoCompra, String descricao) {
		this.id=id;
		this.precoVenda = precoVenda;
		this.precoCompra = precoCompra;
		this.descricao = descricao;
		this.observers_ContratoCompra = new ArrayList<>();
		this.observers_ContratoVenda = new ArrayList<>();
		this.observers_Utilizadores = new ArrayList<>();
	}
	public Ativo(Ativo a) {
		this.id = a.getId();
		this.precoCompra = a.getPrecoCompra();
		this.precoVenda = a.getPrecoVenda();
		this.descricao = a.getDescricao();
		this.observers_ContratoCompra = new ArrayList<>();
		this.observers_ContratoVenda = new ArrayList<>();
		this.observers_Utilizadores = new ArrayList<>();

	}

	public Ativo() {
		this.id=0;
		this.precoVenda=0;
		this.precoCompra=0;
		this.descricao="";
		this.observers_ContratoCompra = new ArrayList<>();
		this.observers_ContratoVenda = new ArrayList<>();
		this.observers_Utilizadores = new ArrayList<>();

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
		return " " + id + "  | " + precoVenda + "  | " + precoCompra + "  | " + descricao + "\n";
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

    /*****************************************************************************/

	public void setValores(float precoVenda,float precoCompra) {
			this.precoCompra=precoCompra;
			this.precoVenda=precoVenda;
			notifyObserverCompra();
			notifyObserverVenda();
	}


    public void setObservers_ContratoCompra(List<Observer> observers_ContratoCompra) {
        this.observers_ContratoCompra = observers_ContratoCompra;
    }

    public void setObservers_ContratoVenda(List<Observer> observers_ContratoVenda) {
        this.observers_ContratoVenda = observers_ContratoVenda;
    }

    public void setObservers_Utilizadores(ArrayList<Observer> observers_Utilizadores) {
        this.observers_Utilizadores = observers_Utilizadores;
    }



}