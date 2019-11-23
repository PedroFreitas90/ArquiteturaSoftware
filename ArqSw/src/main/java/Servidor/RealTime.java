package Servidor;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.math.BigDecimal;

class UpdateAtivo extends Thread{

    private ESS_ltd ess ;
    private String nomeAtivo;

    public UpdateAtivo(ESS_ltd ess,String nomeAtivo) {
        this.ess = ess;
        this.nomeAtivo=nomeAtivo;
    }
        public void run(){
        Ativo ativo;
            try {
                System.out.println("eu sou a thread "+Thread.activeCount());
        while(true) {
            ativo=ess.getAtivo(nomeAtivo);
            float compra,venda;
            Stock stock = null;
            stock = YahooFinance.get(nomeAtivo);
            BigDecimal precoVenda = stock.getQuote().getBid();
            BigDecimal precoCompra = stock.getQuote().getAsk();
            if(precoCompra!=null)
                compra = precoCompra.floatValue();
            else
                compra=0;
            if(precoVenda!=null)
                venda = precoVenda.floatValue();
            else
                venda=0;

            ativo.setValores(venda,compra);
            ess.getAtivos().put(ativo.getId(),ativo);
            sleep(2000);

        }
        } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }

    }

}

public class RealTime {

    private ESS_ltd ess ;

    public RealTime(ESS_ltd ees) {
        this.ess = ees;
    }
    public void update() throws IOException {
       ess.criarAtivo("INTC",1);
       ess.criarAtivo("TSLA",2);
       ess.criarAtivo("BABA",3);
       ess.criarAtivo("GOOG",4);
       ess.criarAtivo("NVDA",5);

       UpdateAtivo up1 = new UpdateAtivo(ess,"INTC");
       UpdateAtivo up2 = new UpdateAtivo(ess,"TSLA");
       UpdateAtivo up3 = new UpdateAtivo(ess,"BABA");
       UpdateAtivo up4 = new UpdateAtivo(ess,"GOOG");
       UpdateAtivo up5 = new UpdateAtivo(ess,"NVDA");
       up1.start();
       up2.start();
       up3.start();
       up4.start();
       up5.start();

    }


// Alteracao no metodo UpdateAtivo porque temos que instanciar ESS_ltd no contrato e estes sao instanciados como Observers no ativo
    // logo é preciso ir ao DAO para associar ESS_ltd aos contratos


    }

