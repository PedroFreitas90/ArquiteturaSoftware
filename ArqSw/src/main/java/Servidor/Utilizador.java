package Servidor;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Utilizador {

	private List<Contrato> portefolio;
	private int id;
	private String username;
	private String password;
	private float plafom;


	public Utilizador( int id, String username, String password, float plafom) {
		this.portefolio = new ArrayList<>();
		this.id = id;
		this.username = username;
		this.password = password;
		this.plafom = plafom;
	}

    public Utilizador() {
        this.portefolio= new ArrayList<>();
        this.id=0;
        this.username="";
        this.password="";
        this.plafom=0;
    }

    public List<Contrato> getPortefolio() {
		return portefolio;
	}

	public void setPortefolio(List<Contrato> portefolio) {
		this.portefolio = portefolio;
	}

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

	public void addContrato(Contrato c ){

		this.portefolio.add(c);
	}
}