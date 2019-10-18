package Servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ThreadCliente extends Thread {

    private Socket socket;
    private ESS_ltd ess;
    private PrintWriter out;
    private BufferedReader in;


   public ThreadCliente(Socket s,ESS_ltd ess) throws IOException {
       this.socket=s;
       this.ess=ess;
       this.out=new PrintWriter(socket.getOutputStream(), true);
       this.in= new BufferedReader(new InputStreamReader(socket.getInputStream()));
   }

   public  void run(){
       String pedido;
        try{
       while((pedido = in.readLine()) != null) {
                   String resposta=  interpretaPedido(pedido);
                   out.println(resposta);
        }
           } catch (IOException e) {
               e.printStackTrace();
           }

       }

       public String interpretaPedido(String pedido)  {
            String [] campos;
           campos = pedido.split(" ");
            switch (campos[0]) {
                case "SESSAO":
                    return iniciar_Sessao(campos[1], campos[2]);
                case "REGISTAR":
                    return registar_utilizador(campos[1], campos[2], campos[3]);

                default:
                    return  "NAO É UM COMANDO VÁLIDO";

            }

       }


       public String iniciar_Sessao(String username,String password ){
           try {
               ess.iniciarSessao(username,password);
           } catch (UtilizadorInvalidoException e) {
               e.printStackTrace();
           }
           return "SUCESSO";
       }

       public String registar_utilizador(String username,String password,String saldo )throws NumberFormatException {

           try {
               int plafom = Integer.parseInt(saldo);
               ess.registarUtilizador(username,password,plafom);
               return "UTILIZADOR REGISTADO COM SUCESSO";
           } catch (UsernameInvalidoException e) {
               e.printStackTrace();
           }
        return "Username ou saldo invalido";
       }
       }


