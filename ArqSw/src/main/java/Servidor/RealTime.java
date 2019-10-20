package Servidor;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.math.BigDecimal;

class UpdateAtivo extends Thread{

    private ESS_ltd ess ;
    private Ativo ativo;

    public UpdateAtivo(ESS_ltd ess,Ativo ativo) {
        this.ess = ess;
        this.ativo = ativo;
    }
        public void run(){
            try {
        while(true) {
            float compra,venda;
            Stock stock = null;
            stock = YahooFinance.get(ativo.getDescricao());
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

            ativo.setPrecoCompra(compra);
            ativo.setPrecoVenda(venda);
            ess.fecharContratosComLimites(ativo);

            sleep(2);
        }
        } catch (IOException | InterruptedException |ContratoInvalidoException e) {
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
       Ativo intc= ess.criarAtivo("INTC");
       Ativo tsla= ess.criarAtivo("TSLA");
       Ativo baba= ess.criarAtivo("BABA");
       Ativo air =ess.criarAtivo("GOOG");
       Ativo yhoo =ess.criarAtivo("NVDA");

       UpdateAtivo up1 = new UpdateAtivo(ess,intc);
       UpdateAtivo up2 = new UpdateAtivo(ess,tsla);
       UpdateAtivo up3 = new UpdateAtivo(ess,baba);
       UpdateAtivo up4 = new UpdateAtivo(ess,air);
       UpdateAtivo up5 = new UpdateAtivo(ess,yhoo);
       up1.start();
       up2.start();
       up3.start();
       up4.start();
       up5.start();

    }





    }

