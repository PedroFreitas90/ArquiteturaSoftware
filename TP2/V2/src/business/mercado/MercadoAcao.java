package business.mercado;

import java.util.List;

public interface MercadoAcao {
    double getCotacaoAcao(String identifier);
    List<String> getAcoes();
    String getNomeAcao(String identifier);
    String getEmpresaAcao(String identifier);
}
