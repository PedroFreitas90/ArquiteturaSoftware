package business.exceptions;

public class NegociadorNaoExisteException extends Exception {
    public NegociadorNaoExisteException(int nif) {
        super("O Negociador com NIF " + nif + " não existe!");
    }
}
