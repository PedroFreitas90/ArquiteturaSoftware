package Servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.nio.*;
import java.nio.channels.*;
import java.util.concurrent.*;

public class Comunicacao extends Thread {

    private AsynchronousSocketChannel sc;
    private ByteBuffer inn = ByteBuffer.allocate(1024);
    private Queue<ByteBuffer> msgQueue; // queue de mensagens para enviar
    private ThreadCliente cliente;


    // ASSINCRONO

    public Comunicacao(AsynchronousSocketChannel s,ThreadCliente cliente) {
        this.sc = s;
        this.cliente =cliente;
        this.inn = ByteBuffer.allocate(1024);
        this.msgQueue = new LinkedList<>();
        this.sc.read(inn, null, readHandler);

    }


    private CompletionHandler<Integer, Object> readHandler = new CompletionHandler<>() {
        @Override
        public void completed(Integer bytesRead, Object o) {
            if (bytesRead > 0) {
                String pedido = getStringAtBuffer(inn);
                System.out.println(pedido);
                cliente.processaPedido(pedido);
                inn = ByteBuffer.allocate(1024);
                sc.read(inn, null, readHandler);
            }
        }

        @Override
        public void failed(Throwable throwable, Object o) {

        }
    };

    private CompletionHandler<Integer, Object> writeHandler = new CompletionHandler<>() {
        @Override
        public void completed(Integer bytesRead, Object o) {
            synchronized (msgQueue) {
                ByteBuffer nextmsg = msgQueue.poll();
                if (nextmsg != null) {
                    sc.write(nextmsg, null, writeHandler);
                }
            }
        }

        @Override
        public void failed(Throwable throwable, Object o) {

        }
    };


    public void send() {
        // synchronized na queue
        synchronized (msgQueue) {
            ByteBuffer tosend = msgQueue.poll();
            this.sc.write(tosend, null, writeHandler);
        }
    }

    public void string_to_ByteBuffer(String res) {
        synchronized (msgQueue) {
            String aux = res + '\n';
            ByteBuffer out = ByteBuffer.allocate(1024);
            byte[] data = aux.getBytes();
            out.put(data);
            out.flip();
            msgQueue.add(out);
        }
    }


    private String getStringAtBuffer(ByteBuffer line) {
        line.flip();
        byte[] data = new byte[line.remaining() - 1];
        line.get(data);
        String s = new String(data);
        line.clear();
        return s;
    }

    public void adicionaQueue(String resposta) {
        if (resposta.length() > 1024) {
            String[] strings = resposta.split("\n");
            for (String s : strings)
                string_to_ByteBuffer(s);
        } else {
            string_to_ByteBuffer(resposta);
        }
    }
}






