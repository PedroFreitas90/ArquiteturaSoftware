package business.mercado;

import java.util.List;

public interface MercadoCommodity {
    double getCotacaoCommodity(String identifier);
    List<String> getCommodities();
    String getNomeCommodity(String identifier);
    String getPaisCommodity(String identifier);
}
