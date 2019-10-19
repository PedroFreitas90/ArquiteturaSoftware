package Cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ThreadClienteLe {
    private Socket cliente;
    private BufferedReader in;

    public ThreadClienteLe(Socket s) throws IOException {
        this.cliente = s;
        this.in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
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
}

