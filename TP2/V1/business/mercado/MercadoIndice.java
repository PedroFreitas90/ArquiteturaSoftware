package business.mercado;

import java.util.List;

public interface MercadoIndice {
    double getCotacaoIndice(String identifier);
    List<String> getIndices();
    String getNomeIndice(String identifier);
}
