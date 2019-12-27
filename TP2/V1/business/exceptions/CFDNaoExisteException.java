package business.exceptions;

public class CFDNaoExisteException extends Exception {
    public CFDNaoExisteException(int id) {
        super("Não existe um CFD com o id " + id);
    }
}
