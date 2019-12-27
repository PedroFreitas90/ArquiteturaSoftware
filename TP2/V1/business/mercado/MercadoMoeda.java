package business.mercado;

import java.util.List;

public interface MercadoMoeda {
    double getCotacaoMoeda(String identifier);
    List<String> getMoedas();
    String getMoedaBase(String identifier);
    String getMoedaQuota(String identifier);
}
