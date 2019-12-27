package business;

import java.time.LocalDateTime;

public class Long extends CFD {

    public Long(int id, LocalDateTime data, double unidadesDeCompra, double valor, Double limiteMin, Double limiteMax,
                String idAtivo, int nifNegociador, boolean aberto) {

        super(id, data, unidadesDeCompra, valor, limiteMin, limiteMax, idAtivo, nifNegociador, aberto);
    }

    public Long(int id, LocalDateTime data, double unidadesDeCompra, double valor, Double limiteMin, Double limiteMax,
                String idAtivo, int nifNegociador, boolean aberto, double valorPorUnidadeFinal ) {

        super(id, data, unidadesDeCompra, valor, limiteMin, limiteMax, idAtivo, nifNegociador, aberto, valorPorUnidadeFinal);
    }

    public double getGanhoDoFecho() {
        if (this.isAberto()) // if CFD still open, no valorization
            return 0;
        return this.getValorPorUnidadeFinal() * this.getUnidadesDeAtivo();
    }

    public double getValorCFD(double valorAtivo) {
        return this.getUnidadesDeAtivo() * valorAtivo;
    }

    public String toString() {
        String s = super.toString();
        return s + "Long";
    }

    public String getTipo() {
        return "Long";
    }
}