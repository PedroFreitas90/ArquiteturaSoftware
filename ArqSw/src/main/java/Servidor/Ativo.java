package Servidor;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.math.BigDecimal;

public class Ativo {
	private int id;
	private float precoVenda;
	private float precoCompra;
	private String descricao;


	public Ativo( int id,float precoVenda, float precoCompra, String descricao) {
		this.id=id;
		this.precoVenda = precoVenda;
		this.precoCompra = precoCompra;
		this.descricao = descricao;
	}
	public Ativo(Ativo a) {
		this.id = a.getId();
		this.precoCompra = a.getPrecoCompra();
		this.precoVenda = a.getPrecoVenda();
		this.descricao = a.getDescricao();
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
		return "Ativo{" +
				"id=" + id +
				", precoVenda=" + precoVenda +
				", precoCompra=" + precoCompra +
				", descricao='" + descricao + '\'' +
				'}';
	}
}