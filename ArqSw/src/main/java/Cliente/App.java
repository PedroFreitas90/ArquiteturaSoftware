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
    private Socket cliente;

    public App (Socket s) throws IOException {
        this.logado=false;
        menu=  new Menu();
        escreve=new ThreadClienteEscreve(s);
        le=new ThreadClienteLe(s,this);
        teclado= new BufferedReader(new InputStreamReader(System.in));
        cliente=s;
    }




    public void mostraMenu(){
        if(logado) {
            System.out.println(menu.getMenuPrincipal());
            handlerMenuPrincipal();
        }
        else {
            System.out.println(menu.getMenuIncial());
            handlerMenuInicial();
        }
    }


    public void handlerMenuPrincipal(){
        try{
            String pedido = teclado.readLine();
            int op = Integer.parseInt(pedido);
            switch (op) {
                case 0:
                    terminaSessao();
                    break;
                case 1:
                    consultar_Saldo();
                    break;
                case 2:
                    listaAtivos();
                    break;
                case 3:
                    venderContrato();
                    break;
                case 4 :
                    comprarContrato();
                    break;
                case 5 :
                        verPortefolio();
                        break;
                case 6: fecharContrato();
                        break;
                case 7:
                    verRegistos();
                    break;
            }
            Thread.sleep(1000);
            mostraMenu();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
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
                        Thread.sleep(1000);
                        mostraMenu();
                        break;
                    case 2:
                        registo_input();
                        Thread.sleep(1000);
                        mostraMenu();
                        break;
                }

        } catch (IOException | InterruptedException e) {
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
      if(resposta.equals("SUCESSO")) {
          this.logado = true;
          Thread t = new Thread(le);
        t.start();
      }

    }

    public void fecharApp() throws IOException {
        logado=false;
        cliente.close();

    }

    public void  terminaSessao() throws InterruptedException {
        logado=false;
        StringBuilder sb = new StringBuilder();
        String pedido = "TERMINAR";
        sb=sb.append(pedido);
        escreve.addPedido(sb.toString());


    }

    public void consultar_Saldo() throws InterruptedException {
        StringBuilder sb = new StringBuilder();
        String pedido = "SALDO";
        sb=sb.append(pedido);
        escreve.addPedido(sb.toString());

    }

    public void listaAtivos(){
        StringBuilder sb = new StringBuilder();
        String pedido = "LISTARATIVOS";
        sb=sb.append(pedido);
        escreve.addPedido(sb.toString());

    }

    public void venderContrato() throws IOException {
        StringBuilder sb = new StringBuilder();
        String pedido = "CONTRATOVENDA";
        sb=sb.append(pedido);
        sb.append(" ");
        System.out.println("Id do ativo");
        String idAtivo= teclado.readLine();
        sb.append(idAtivo);
        sb.append(" ");
        System.out.println("Take Profit");
        String takeprofit= teclado.readLine();
        sb.append(takeprofit);
        sb.append(" ");
        System.out.println("StopLoss");
        String stopLoss= teclado.readLine();
        sb.append(stopLoss);
        sb.append(" ");
        System.out.println("Quantidade a adquirir");
        String quantidade= teclado.readLine();
        sb.append(quantidade);
        sb.append(" ");
        escreve.addPedido(sb.toString());
    }

    public void comprarContrato() throws IOException {
        StringBuilder sb = new StringBuilder();
        String pedido = "CONTRATOCOMPRA";
        sb=sb.append(pedido);
        sb.append(" ");
        System.out.println("Id do ativo");
        String idAtivo= teclado.readLine();
        sb.append(idAtivo);
        sb.append(" ");
        System.out.println("Take Profit");
        String takeprofit= teclado.readLine();
        sb.append(takeprofit);
        sb.append(" ");
        System.out.println("StopLoss");
        String stopLoss= teclado.readLine();
        sb.append(stopLoss);
        sb.append(" ");
        System.out.println("Quantidade a adquirir");
        String quantidade= teclado.readLine();
        sb.append(quantidade);
        sb.append(" ");
        escreve.addPedido(sb.toString());


    }

    public void verPortefolio()  {
        StringBuilder sb = new StringBuilder();
        String pedido = "PORTEFOLIO";
        sb = sb.append(pedido);
        escreve.addPedido(sb.toString());
    }

    public void fecharContrato() throws IOException {
        StringBuilder sb = new StringBuilder();
        String pedido = "FECHARCONTRATO";
        sb=sb.append(pedido);
        sb.append(" ");
        System.out.println("Id do Contrato");
        String idContrato= teclado.readLine();
        sb.append(idContrato);
        sb.append(" ");
        escreve.addPedido(sb.toString());

    }
    public void verRegistos() {
        StringBuilder sb = new StringBuilder();
        String pedido = "REGISTOS";
        sb = sb.append(pedido);
        escreve.addPedido(sb.toString());
    }

}
