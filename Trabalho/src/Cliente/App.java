package Cliente;

import Servidor.ThreadCliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class App {

    private boolean logado;
    private Menu menu;
    private ThreadClienteEscreve escreve;
    private ThreadClienteLe le;
    BufferedReader teclado;

    public App (Socket s) throws IOException {
        this.logado=false;
        menu=  new Menu();
        escreve=new ThreadClienteEscreve(s);
        le=new ThreadClienteLe(s);
        teclado= new BufferedReader(new InputStreamReader(System.in));
    }


    public void mostraMenu(){
        if(logado)
            System.out.println(menu.getMenuPrincipal());
        else {
            System.out.println(menu.getMenuIncial());
            handlerMenuInicial();
        }
    }

    public void handlerMenuInicial(){
        try {
          String pedido=  teclado.readLine();
                int op = Integer.parseInt(pedido);
                switch (op) {
                    case 0:
                        fecharApp();
                        break;
                    case 1 :
                        login();
                        break;
                    case 2:
                        registo_input();
                        break;
                }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void registo_input() throws IOException {
        StringBuilder sb = new StringBuilder();
        String pedido = "REGISTAR";
        sb=sb.append(pedido);
        sb.append(" ");
        System.out.println("Username");
        String username= teclado.readLine();
        sb.append(username);
        sb.append(" ");
        System.out.println("Password");
        String password= teclado.readLine();
        sb.append(password);
        sb.append(" ");
        System.out.println("Saldo inicial");
        String saldo= teclado.readLine();
        sb.append(saldo);
        escreve.addPedido(sb.toString());
        String resposta= le.leResposta();
        System.out.println(resposta);
        mostraMenu();

    }

    public void login () throws IOException {
        StringBuilder sb = new StringBuilder();
        String pedido = "SESSAO";
        sb=sb.append(pedido);
        sb.append(" ");
        System.out.println("Username");
        String username= teclado.readLine();
        sb.append(username);
        sb.append(" ");
        System.out.println("Password");
        String password= teclado.readLine();
        sb.append(password);
        sb.append(" ");
        escreve.addPedido(sb.toString());
      String resposta= le.leResposta();
      System.out.println(resposta);
      if(resposta.equals("SUCESSO"))
          this.logado=true;
      mostraMenu();
    }

    public void fecharApp(){
        logado=false;
        //fechar threads
        // fechar sockets
    }
}
