package Cliente;

public class Menu {

    private String menuIncial;
    private String menuPrincipal;


    public Menu(){
       this.menuIncial="************* MENU ****************\n"+
                   "* 1 - Iniciar Sessao              *\n"+
                   "* 2 - Registar                    *\n"+
                   "* 0 - Sair                        *\n"+
                   "***********************************\n";
       this.menuPrincipal=   "Listar ativos\n"+
               "Consultar saldo\n"+
               "Listar ativos a vender\n"+
               "Comprar ativo\n"+
               "Vender ativo\n"+
               "Consultar portfólio\n"+
               "Fechar contrato\n"+
               "Terminar Sessão\n";
    }


    public String getMenuIncial() {
        return menuIncial;
    }

    public String getMenuPrincipal() {
        return menuPrincipal;
    }
}
