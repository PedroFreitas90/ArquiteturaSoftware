package Servidor;

import java.net.Socket;
import java.time.Period;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

public class Utilizador {
	private int id;
	private String username;
	private String password;
	private float plafom;
    private List<Pedido.Memento> pedidosPendentes;
    private Pedido pedidoAtual;
    private int posUltimoRespondido;// posicao no array do ultimo pedido respondido com sucesso




	public Utilizador( int id, String username, String password, float plafom) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.plafom = plafom;
		this.pedidosPendentes=new ArrayList<>();
		this.pedidoAtual= new Pedido();
		this.posUltimoRespondido=-1;

	}

    public Utilizador() {
        this.id=0;
        this.username="";
        this.password="";
        this.plafom=0;
        this.pedidosPendentes= new ArrayList<>();
        this.pedidoAtual= new Pedido();
        this.posUltimoRespondido=-1;
    }
// NEW
	public List<Pedido.Memento> getPedidosSave() {
	    return this.pedidosPendentes;
	}

	public void setPedidosSave(List<Pedido.Memento> pedidosSave) {
		this.pedidosPendentes=pedidosSave;
		if(this.pedidosPendentes.size()>0)
		this.pedidoAtual.restoreFromMemento(this.pedidosPendentes.get(0));
	}
	//Qual é o objetivo desta função?


	public Pedido getPedidoAtual() {
		if(!pedidosPendentes.isEmpty()) {
			pedidoAtual.restoreFromMemento(pedidosPendentes.get(posUltimoRespondido + 1));
			if (!pedidoAtual.getEstado().getEstado())
				return this.pedidoAtual;
		}
			return null;
	}

	public void setPedido(Pedido p) {
		pedidosPendentes.add(p.saveToMemento());
	//	ess.updateEstadoPedido(p);
	}

	public void pedidoFinalizado(){
		this.pedidosPendentes.get(posUltimoRespondido+1).getSavedEstado().setEstado(true);
		posUltimoRespondido++;
	}



	//OLD
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public float getPlafom() {
		return plafom;
	}

	public void setPlafom(float plafom) {
		this.plafom = plafom;
	}

}