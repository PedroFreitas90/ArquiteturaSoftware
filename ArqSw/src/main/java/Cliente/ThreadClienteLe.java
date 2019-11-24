package Cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ThreadClienteLe implements Runnable {
    private Socket cliente;
    private BufferedReader in;
    private App aplicacao;

    public ThreadClienteLe(Socket s,App app)  throws IOException {
        this.cliente = s;
        this.in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
        this.aplicacao=app;
    }

    public String leResposta() {
        String resposta = null;
        try {
            resposta = in.readLine();
            return resposta;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return resposta;
    }

    public void run(){
        String resposta ;

        try {
            while((resposta = in.readLine()) != null){
                System.out.println(resposta);
                    if(resposta.equals("TERMINADA"))
                        break;

            }

            } catch (IOException e) {
            e.printStackTrace();
            }

    }
}

