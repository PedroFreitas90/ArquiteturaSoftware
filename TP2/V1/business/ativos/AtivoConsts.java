package business.ativos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AtivoConsts {
    public static final List<String> TIPOS_DE_ATIVOS = new ArrayList<>(Arrays.asList("Actions", "Commodities", "Indexes", "Currency"));
    public static final int TOTAL_TIPOS_ATIVOS = TIPOS_DE_ATIVOS.size();
    public static final String[] ALL_ATIVOS = {"acao", "commodity", "indice", "moeda",};
}
