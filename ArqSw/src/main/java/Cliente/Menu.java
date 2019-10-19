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
       this.menuPrincipal="************* MENU ****************\n"+
                          "* 1 - Consultar Saldo             *\n"+
                          "* 2 - Listar Ativos               *\n"+
                          "* 3 - Vender Contrato             *\n"+
                          "* 4 - Comprar Contrato            *\n"+
                          "* 5 - Consultar portfólio         *\n"+
                          "* 6 - Fechar contrato             *\n"+
                          "* 7 - Ver Registos                *\n"+
                          "* 0 - Terminar Sessão             *\n"+
                          "***********************************\n";
    }


    public String getMenuIncial() {
        return menuIncial;
    }

    public String getMenuPrincipal() {
        return menuPrincipal;
    }
}
