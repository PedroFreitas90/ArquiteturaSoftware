package Servidor;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
;

public class Servidor {
    private static final int port = 4545;

    public static void main(String[] args) throws IOException {
        ServerSocket srv = new ServerSocket(port);
        ESS_ltd ess = new ESS_ltd();

        while(true) {
            Socket cliSocket = srv.accept();
            ThreadCliente cli = new ThreadCliente(cliSocket,ess);
            cli.start();
        }
    }
}
