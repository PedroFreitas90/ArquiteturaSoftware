package Servidor;

import java.net.Socket;
import java.sql.Timestamp;
import java.time.Period;
import java.util.*;

public class Utilizador implements Observer{
	private ESS_ltd ess;
	private int id;
	private String username;
	private String password;
	private float plafom;
    private List<Pedido.Memento> pedidosPendentes;
    private Pedido pedidoAtual;
    private int posUltimoRespondido;// posicao no array do ultimo pedido respondido com sucesso
	/********************* NOVO REQUISITO********/
	private Map<Integer,Ativo> aSeguir;




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

	@Override
	public String toString() {
		return "Utilizador{" +
				"id=" + id +
				", username='" + username + '\'' +
				", password='" + password + '\'' +
				", plafom=" + plafom +
				", pedidosPendentes=" + pedidosPendentes +
				", pedidoAtual=" + pedidoAtual +
				", posUltimoRespondido=" + posUltimoRespondido +
				'}';
	}
	public  void setEss(ESS_ltd ess){
		this.ess=ess;
	}




	/************************* NOVO REQUISITO**************/

	public Map<Integer,Ativo> getaSeguir() {
		return aSeguir;
	}

	public void setaSeguir(Map<Integer,Ativo> aSeguir) {
		this.aSeguir = aSeguir;
	}

	@Override
	//pegar no valor do ativo e verificar se houve variação acima dos 5%
	public void update(Ativo a) {
		Ativo seguir = aSeguir.get(a.getId()); // procurar o ativo no map
		if (seguir != null) {// se encontrou
			float valorCompraSeguir = seguir.getPrecoCompra();
			float valorVendaSeguir = seguir.getPrecoVenda();
			float percentagemCompra = Math.abs(valorCompraSeguir - a.getPrecoCompra()) / valorCompraSeguir;
			float percentagemVenda = Math.abs(valorVendaSeguir - a.getPrecoVenda()) / valorVendaSeguir;

			if (percentagemCompra > 0.1 || percentagemVenda > 0.1) {
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				ess.novaNotificacao(this, " ativo "+ a.getDescricao()+ "  teve uma mudança brusca às " + timestamp,a);
			}
		}

	}
}
