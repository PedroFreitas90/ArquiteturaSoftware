package Servidor;

public class Registo {

	private int id;
	private int idUtilizador;
	private int idAtivo;
	private float lucro;
	private int quantidade;


	public Registo(int id, int idUtilizador, int idAtivo, float lucro, int quantidade) {
		this.id = id;
		this.idUtilizador = idUtilizador;
		this.idAtivo = idAtivo;
		this.lucro = lucro;
		this.quantidade = quantidade;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdUtilizador() {
		return idUtilizador;
	}

	public void setIdUtilizador(int idUtilizador) {
		this.idUtilizador = idUtilizador;
	}

	public int getIdAtivo() {
		return idAtivo;
	}

	public void setIdAtivo(int idAtivo) {
		this.idAtivo = idAtivo;
	}

	public float getLucro() {
		return lucro;
	}

	public void setLucro(float lucro) {
		this.lucro = lucro;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	@Override
	public String toString() {
		return  "********************************"+
				"\n id=" + id +
				"\n idUtilizador=" + idUtilizador +
				"\n idAtivo=" + idAtivo +
				"\n lucro=" + lucro +
				"\n quantidade=" + quantidade +
				"\n********************************";
	}
}