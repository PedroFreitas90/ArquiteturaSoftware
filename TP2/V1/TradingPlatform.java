import business.facade.TradingPlatformNegociador;
import presentation.TextUINegociador;
import presentation.UINegociador;

public class TradingPlatform {
    public static void main(String[] args) {
        UINegociador neg = new TextUINegociador(new TradingPlatformNegociador(), "PT");
        neg.start();
    }
}
