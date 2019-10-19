package Servidor;
public class Contrato {

	private int id;
	private int idAtivo;
	private int idUtilizador;
	private float preco;
	private float takeProfit;
	private float stopLess;
	private int quantidade;
	private boolean compra;
	private boolean encerrado;


	public Contrato(int id, int idAtivo, int idUtilizador, float preco, float takeProfit, float stopLess, int quantidade, boolean compra,boolean encerrado) {
		this.id = id;
		this.idAtivo = idAtivo;
		this.idUtilizador = idUtilizador;
		this.preco = preco;
		this.takeProfit = takeProfit;
		this.stopLess = stopLess;
		this.quantidade = quantidade;
		this.compra = compra;
		this.encerrado = encerrado;
	}

	public Contrato(Contrato p) {
		this.id = p.getId();
		this.idAtivo = p.getIdAtivo();
		this.idUtilizador=p.getIdUtilizador();
		this.preco=p.getPreco();
		this.takeProfit=p.getTakeProfit();
		this.stopLess=p.getStopLess();
		this.quantidade = p.getQuantidade();
		this.compra=p.isCompra();
		this.encerrado=p.isEncerrado();
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdAtivo() {
		return idAtivo;
	}

	public void setIdAtivo(int idAtivo) {
		this.idAtivo = idAtivo;
	}

	public int getIdUtilizador() {
		return idUtilizador;
	}

	public void setIdUtilizador(int idUtilizador) {
		this.idUtilizador = idUtilizador;
	}

	public float getPreco() {
		return preco;
	}

	public void setPreco(int preco) {
		this.preco = preco;
	}

	public float getTakeProfit() {
		return takeProfit;
	}

	public void setTakeProfit(int takeProfit) {
		this.takeProfit = takeProfit;
	}

	public float getStopLess() {
		return stopLess;
	}

	public void setStopLess(int stopLess) {
		this.stopLess = stopLess;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public boolean isCompra() {
		return compra;
	}

	public void setCompra(boolean compra) {
		this.compra = compra;
	}

	public boolean isEncerrado() {
		return encerrado;
	}

	public void setEncerrado(boolean encerrado) {
		this.encerrado = encerrado;
	}

	public Contrato clone(){
		return new Contrato(this);
	}

	@Override
	public String toString() {
		return "Contrato{" +
				"id=" + id +
				", idAtivo=" + idAtivo +
				", idUtilizador=" + idUtilizador +
				", preco=" + preco +
				", takeProfit=" + takeProfit +
				", stopLess=" + stopLess +
				", quantidade=" + quantidade +
				", compra=" + compra +
				", encerrado=" + encerrado +
				'}';
	}
}