package business;

import java.time.LocalDateTime;

public class Short extends CFD {

    private static double formula(double investimentoInicial, double valorCFD) {
        return Math.max(investimentoInicial * 2 - valorCFD, 0);
    }

    public Short(int id, LocalDateTime data, double unidadesDeCompra, double valor, Double limiteMin, Double limiteMax, String ativoId, int nifNegociador, boolean aberto) {
        super(id, data, unidadesDeCompra, valor, limiteMin, limiteMax, ativoId, nifNegociador, aberto);
    }

    public Short(int id, LocalDateTime data, double unidadesDeCompra, double valor, Double limiteMin, Double limiteMax,
                String idAtivo, int nifNegociador, boolean aberto, double valorPorUnidadeFinal) {

        super(id, data, unidadesDeCompra, valor, limiteMin, limiteMax, idAtivo, nifNegociador, aberto, valorPorUnidadeFinal);
    }

    public double getGanhoDoFecho() {
        if (this.isAberto())
            return 0;
        double valorInicial = this.getValorInvestido();
        double valorFinal = this.getValorPorUnidadeFinal() * this.getUnidadesDeAtivo();
        return formula(valorInicial, valorFinal);
    }

    public double getValorCFD(double valorAtivo) {
        return formula(this.getValorInvestido(), this.getUnidadesDeAtivo() * valorAtivo);
    }

    public String toString() {
        String s = super.toString();
        return s + "Short";
    }

    public String getTipo() {
        return "Short";
    }
}