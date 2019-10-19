package Cliente;

import Servidor.ThreadCliente;

import java.io.IOException;
import java.net.Socket;

public class Main {

    public static void main(String [] args) throws IOException {
        Socket cliente = new Socket("127.0.0.1",9090);

           App app= new App(cliente);
           app.mostraMenu();

    }
}
