package Servidor;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
;

public class Servidor {
    private static final int port =4545;

    public static void main(String[] args) throws IOException, InterruptedException {
        ESS_ltd ess = new ESS_ltd();
        RealTime rt = new RealTime(ess);
        rt.update();
        AsynchronousChannelGroup g = AsynchronousChannelGroup.withFixedThreadPool(5, Executors.defaultThreadFactory());
        AsynchronousServerSocketChannel ssc = AsynchronousServerSocketChannel.open(g);

        ssc.bind(new InetSocketAddress(port));

        ssc.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
            @Override
            public void completed(final AsynchronousSocketChannel sc, Object o) {
                new ThreadCliente(sc,ess);
                ssc.accept(null,this);
            }

            @Override
            public void failed(Throwable throwable, Object o) {

            }
        });

        g.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
    }

// VERSAO SINCRONA
        /*
        ServerSocket srv = new ServerSocket(port);
        ESS_ltd ess = new ESS_ltd();
        RealTime rt = new RealTime(ess);
        rt.update();
        while(true) {
            Socket cliSocket = srv.accept();
            ThreadCliente cli = new ThreadCliente(cliSocket,ess);
            cli.start();
        }
    }
    */

}
