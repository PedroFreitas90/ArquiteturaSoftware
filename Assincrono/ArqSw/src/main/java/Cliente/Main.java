package Cliente;

import Servidor.ESS_ltd;
import Servidor.ThreadCliente;

import java.io.IOException;
import java.net.Socket;

public class Main {

    public static void main(String [] args) throws IOException {
        Socket cliente = new Socket("127.0.0.1",4545);
           App app= new App(cliente);
           app.mostraMenu();

    }
}
