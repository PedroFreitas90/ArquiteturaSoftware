package Servidor;
import Servidor.Utilizador;

import java.util.*;
import java.util.concurrent.locks.Lock;

public class ESS_ltd {

	private Map<String,Utilizador> utilizadores;
	private Utilizador utilizador;
	//private Collection<Ativo> ativos;
	//private Collection<Registo> registos;


	public ESS_ltd(){
		this.utilizador=null;
		this.utilizadores= new HashMap<>();
	}

	/**
	 * 
	 * @param String
	 */
	public void iniciarSessao(String username,String password) throws UtilizadorInvalidoException {

		 if(utilizadores.containsKey(username)){
		 	Utilizador u= utilizadores.get(username);
		 	String pass= u.getPassword();
		 	if(pass.equals(password))
		 		this.utilizador=u;
		 }
		else{
			throw new UtilizadorInvalidoException("Username ou password errada");
		 }


	}
	public synchronized void registarUtilizador(String username,String password,int saldo) throws UsernameInvalidoException {
			if(utilizadores.containsKey(username))
				throw new UsernameInvalidoException("Username inválido");
			else{
				int id=utilizadores.size()+1;
				Utilizador u = new Utilizador(id,username,password,saldo);
				utilizadores.put(username,u);
			}


	}


}