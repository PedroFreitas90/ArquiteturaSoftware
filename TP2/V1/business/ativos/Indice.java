package business.ativos;

public class Indice extends Ativo {

	private int numEmpresas;

	public Indice(String id, String nome, double vpu) {
		super(id, nome, vpu);
	}

	public Indice(String id, String nome, double vpu, int numEmpresas) {
		super(id, nome, vpu);
		this.numEmpresas = numEmpresas;
	}

	public int getNumEmpresas() {
		return this.numEmpresas;
	}

	public double getValorPorUnidadeMaisRecente() {
		double val = this.getMercado().getCotacaoIndice(this.getId());
		if (val == 0)
			val = this.getValorPorUnidade();
		this.setValorPorUnidade(val);
		return val;
	}

	public String toString() {
		String s = super.toString();
		return s + "Número de empresas: " + this.numEmpresas;
	}
}