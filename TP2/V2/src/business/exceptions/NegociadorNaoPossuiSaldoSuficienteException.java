package business.exceptions;

public class NegociadorNaoPossuiSaldoSuficienteException extends Exception {
    public NegociadorNaoPossuiSaldoSuficienteException(double investimento, int nifNegociador) {
        super("O Negociador com NIF " + nifNegociador + " não pode gastar " + investimento + "€" );
    }
}
