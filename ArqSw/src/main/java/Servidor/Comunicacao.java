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
    private Comunicacao com;


    // ASSINCRONO

    public Comunicacao(AsynchronousSocketChannel s) {
        this.sc = s;
        this.inn = ByteBuffer.allocate(1024);
        this.msgQueue = new LinkedList<>();
        this.sc.read(inn, null, readHandler);

    }


    private CompletionHandler<Integer, Object> readHandler = new CompletionHandler<>() {
        @Override
        // caso dei sucesso
        public void completed(Integer bytesRead, Object o) {
            if (bytesRead > 0) {
                String pedido = getStringAtBuffer(inn);
                com.processaPedido(pedido);
                inn = ByteBuffer.allocate(1024);
                sc.read(inn, null, readHandler);
            }
        }

        @Override
        public void failed(Throwable throwable, Object o) {

        }
    };
// HANDLER DE ESCRITA

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
            // retira uma mensagem da queue
            ByteBuffer tosend = msgQueue.poll();
            // envia a mensagem para o cliente
            this.sc.write(tosend, null, writeHandler);
        }
    }

    public void string_to_ByteBuffer(String res) {
        synchronized (msgQueue) {
            String aux = res + '\n';
            ByteBuffer out = ByteBuffer.allocate(1024);
            byte[] data = aux.getBytes();
            // coloca os bytes no buffer out
            out.put(data);
            // mexe nos apontadores
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






