package business;

import java.time.LocalDateTime;

public abstract class CFD implements Observer {
	private int id;
	private LocalDateTime data;
	private double unidadesDeAtivo;
	private double valorPorUnidadeNaCompra;
	private Double limitSup;
	private Double limiteInf;
	private String idAtivo;
	private int nifNegociador;
	private boolean aberto;
	private double valorPorUnidadeFinal;

	public CFD(int id, LocalDateTime data, double unidadesDeCompra, double valor, Double limiteMin, Double limiteMax, String ativoId, int nifNegociador, boolean aberto) {
		this.id = id;
		this.data = data;
		this.unidadesDeAtivo = unidadesDeCompra;
		this.valorPorUnidadeNaCompra = valor;
		this.limiteInf = limiteMin;
		this.limitSup = limiteMax;
		this.idAtivo = ativoId;
		this.nifNegociador = nifNegociador;
		this.aberto = aberto;
		this.valorPorUnidadeFinal = 0;
	}

	public CFD(int id, LocalDateTime data, double unidadesDeCompra, double valor, Double limiteMin, Double limiteMax, String ativoId, int nifNegociador, boolean aberto, double vfinal) {
		this.id = id;
		this.data = data;
		this.unidadesDeAtivo = unidadesDeCompra;
		this.valorPorUnidadeNaCompra = valor;
		this.limiteInf = limiteMin;
		this.limitSup = limiteMax;
		this.idAtivo = ativoId;
		this.nifNegociador = nifNegociador;
		this.aberto = aberto;
		this.valorPorUnidadeFinal = vfinal;
	}

	public double getValorInvestido() {
		return this.unidadesDeAtivo * this.valorPorUnidadeNaCompra;
	}

	public int getId() {
		return this.id;
	}

	public LocalDateTime getData() {
		return this.data;
	}

	public double getUnidadesDeAtivo() {
		return this.unidadesDeAtivo;
	}

	public double getValorPorUnidadeNaCompra() {
		return this.valorPorUnidadeNaCompra;
	}

	public Double getLimitSup() {
		return this.limitSup;
	}

	public Double getLimiteInf() {
		return this.limiteInf;
	}

	public String getIdAtivo() {
		return this.idAtivo;
	}

	public int getNifNegociador() {
		return this.nifNegociador;
	}

	public boolean isAberto() {
		return aberto;
	}

	public double getValorPorUnidadeFinal() {
		return valorPorUnidadeFinal;
	}

	/**
	 * @param valorAtivo valor atual por unidade do ativo
	 * @return True se o CFD se encontra fechado, false se encontra aberto
	 */
	public boolean fecharCFD(double valorAtivo) {
		if (this.aberto) {
			this.aberto = false;
			this.valorPorUnidadeFinal = valorAtivo;
			return true;
		}
		return false;
	}

	/**
	 * @return quanto deve ser atribuído ao saldo do negociador, caso termine o CFD
	 */
	public abstract double getGanhoDoFecho();

	/**
	 * @param valorAtivo base para gerar valorização de CFD
	 * @return valor de um CFD para um dado valor de ativo
	 */
	public abstract double getValorCFD(double valorAtivo);

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Id: ").append(this.id).append("\n");
		sb.append("NIF: ").append(this.nifNegociador).append("\n");
		sb.append("Data: ").append(this.data.toString()).append("\n");
		sb.append("Unidades de Ativo: ").append(this.unidadesDeAtivo).append("\n");
		sb.append("Valor por Unidade na Compra: ").append(this.valorPorUnidadeNaCompra).append("\n");
		sb.append("Take Profit: ").append(this.limitSup).append("\n");
		sb.append("Stop Loss: ").append(this.limiteInf).append("\n");
		sb.append("Id do Ativo: ").append(this.idAtivo).append("\n");
		sb.append("Aberto?: ").append(this.aberto).append("\n");
		sb.append("Valor Por Unidade no Final: ").append(this.valorPorUnidadeFinal).append("\n");
		return sb.toString();
	}


	public void setId(int id){
		this.id=id;
	}

	public boolean update(double valorAtivo, String idAtivo) {
		if (!this.isAberto())
			return false;

		double valorDoCFD = this.getValorCFD(valorAtivo);
		boolean atualizou = false;
		if (isUltrapassouLimites(valorDoCFD)){
			this.fecharCFD(valorAtivo);
			atualizou = true;
		}
		return atualizou;
	}

	private boolean isUltrapassouLimites(double valorDoCFD){
		return (this.limitSup!=null && valorDoCFD >= this.limitSup )|| (this.limiteInf != null && valorDoCFD <= limiteInf);
	}

	/**
	 * @return retorna o nome do tipo de CFD (useful para UI)
	 */
	public abstract String getTipo();
}