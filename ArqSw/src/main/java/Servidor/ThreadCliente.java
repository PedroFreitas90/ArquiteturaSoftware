package Servidor;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.List;
import java.util.Queue;

public class ThreadCliente {
    private Utilizador user;
    private ESS_ltd ess;
    private Comunicacao comunicacao;

    public ThreadCliente(AsynchronousSocketChannel s,ESS_ltd ess){
        this.user = null;
        this.ess= ess;
        this.comunicacao = new Comunicacao(s,this);

    }


    public void processaPedido(String pedido) {

        String[] campos = pedido.split(" ");
        if (!(campos[0].equals("SESSAO") || campos[0].equals("REGISTAR"))) {
            int size = ess.sizePedidos() + 1;
            Pedido p = new Pedido();
            p.set(pedido, false, size, user.getId());
            ess.updateEstadoPedido(p);
            user.setPedido(p);
            proximoPedido();
        } else {// CASO ESPECIAL DE LOGIN E REGISTO
            String resposta = interpretaPedido(pedido);
            comunicacao.adicionaQueue(resposta);
            comunicacao.send();
            // enviar todos os pedidos pendentes
            if (user != null) {
                int tamanho = user.getPedidosSave().size();
                int i = 0;
                if (tamanho > 0) {
                    comunicacao.adicionaQueue("O servidor foi abaixo mas o seus pedidos foram salvos.");
                    comunicacao.send();
                    while (i < tamanho) {
                        proximoPedido();
                        i++;
                    }
                }
            }
        }

    }


    public String interpretaPedido(String pedido)  {
        String [] campos;
        campos = pedido.split(" ");
        System.out.println(pedido);
        switch (campos[0]) {
            case "SESSAO":
                return iniciar_Sessao(campos[1], campos[2]);
            case "REGISTAR":
                return registar_utilizador(campos[1], campos[2], campos[3]);
            case "SALDO":
                return saldo_utilizador(user);
            case "LISTARATIVOS":
                return lista_Ativos();
            case "CONTRATOVENDA":
                // criarContratoVenda(idUser,idAtivo,takeprofit,stoploss,quantidade)
                return criarContratoVenda(campos[1],campos[2],campos[3],campos[4]);
            case "CONTRATOCOMPRA":
                return criarContratoCompra(campos[1],campos[2],campos[3],campos[4]);
            case "PORTEFOLIO":
                return verPortefolio();
            case "FECHARCONTRATO":
                return fecharContrato(campos[1]);
            case "REGISTOS":
                return verRegistos();
            case "TERMINAR":
                return terminarSessao();
            default:
                return pedido + " NAO É UM COMANDO VÁLIDO\n";

        }

    }


    public String iniciar_Sessao(String username,String password ){
        boolean sucess = false;
        try {
            user= ess.iniciarSessao(username,password);
            sucess=true;

        } catch (UtilizadorInvalidoException e) {
            e.printStackTrace();
        }
        finally {
            if(sucess)
                return "SUCESSO";
            else
                return "Login não efetuado";
        }

    }

    public String registar_utilizador(String username,String password,String saldo )throws NumberFormatException {
        boolean sucess=false;
        try {
            int plafom = Integer.parseInt(saldo);
            ess.registarUtilizador(username,password,plafom);
            sucess=true;

        } catch (UsernameInvalidoException e) {
            e.printStackTrace();

        }
        finally {
            if(sucess)
                return "UTILIZADOR REGISTADO COM SUCESSO";
            else
                return "Username ou saldo invalido";
        }

    }

    public String saldo_utilizador(Utilizador u){
        float saldo= ess.saldo(u);
        String plafom = Float.toString(saldo);
        return  "O seu saldo : "+ plafom;
    }
    public String lista_Ativos(){
        List<Ativo> ativos = ess.listarAtivos();
        StringBuilder sb = new StringBuilder();
        sb.append("  Idenficação  | Nome  | preco de compra  | preco de um venda\n");
        for(Ativo a : ativos) {
            sb.append(a);
            sb.append(" ");
        }
        return sb.toString();
    }

    public String criarContratoVenda(String idAtivo,String tkp,String stl,String quant) throws NumberFormatException {
        boolean sucess = false;
        try {
            int id_ativo = Integer.parseInt(idAtivo);
            float tk = Float.parseFloat(tkp);
            float sl = Float.parseFloat(stl);
            int quantidade = Integer.parseInt(quant);

            ess.criarContratoVenda(this.user, id_ativo, tk, sl, quantidade);
            sucess = true;

        } catch (AtivoInvalidoException e) {
            e.printStackTrace();
        } finally {
            if (sucess)
                return "Contrato criado com sucesso";
            else
                return "Dados Inválidos ou saldo insuficiente";

        }
    }


    public String criarContratoCompra(String idAtivo,String tkp,String stl,String quant) throws NumberFormatException {
        boolean sucess = false;
        try {
            int id_ativo = Integer.parseInt(idAtivo);
            float tk = Float.parseFloat(tkp);
            float sl = Float.parseFloat(stl);
            int quantidade = Integer.parseInt(quant);

            ess.criarContratoCompra(this.user, id_ativo, tk, sl, quantidade);
            sucess = true;

        } catch (AtivoInvalidoException e) {
            e.printStackTrace();
        } finally {
            if (sucess)
                return "Contrato criado com sucesso";
            else
                return "Dados Inválidos ou saldo insuficiente";

        }
    }


    public String verPortefolio(){
        List<Contrato> contratos= ess.listarPortefolio(this.user);
        StringBuilder sb = new StringBuilder();
        sb.append("         PORTEFOLIO         \n");
        for(Contrato c : contratos) {
            sb.append(c);
            sb.append("\n");
        }
        return sb.toString();
    }


    public String fecharContrato(String idContrato) throws NumberFormatException{
        boolean sucess= false;
        try{
            int id_Contrato = Integer.parseInt(idContrato);
            ess.fecharContrato(this.user,id_Contrato);
            sucess =true;
        } finally {
            if (sucess)
                return "Contrato fechado com sucesso";
            else
                return "Id invalido ";

        }
    }

    public String verRegistos(){
        List<Registo> registos = ess.listaRegistos(this.user);
        StringBuilder sb = new StringBuilder();
        sb.append("         REGISTOS         \n");
        for(Registo r : registos) {
            sb.append(r);
            sb.append("\n");
        }
        return sb.toString();

    }

    public String terminarSessao(){
        this.user=null;
        return "TERMINADA";
    }


    public void proximoPedido() {
        if (user != null) {
            Pedido p = user.getPedidoAtual();
            if (p != null) {
                String req = p.getEstado().getPedido();
                String resposta = interpretaPedido(req);
                comunicacao.adicionaQueue(resposta);
                comunicacao.send();
                p.getEstado().setEstado(true);
                ess.updateEstadoPedido(p);
                //out.println(resposta);//VERSAO SINCRONA
                if (!req.equals("TERMINAR")) {
                    user.pedidoFinalizado();
                }
                //  p.getEstado().setEstado(true);
                // this.ess.updateEstadoPedido(p);
            }
        }
    }
}




