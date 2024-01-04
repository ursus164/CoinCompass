import com.data.currency.Currency;
import com.data.market.MarketData;

public class MarketDataTest {
    public static void main(String[] args) {
        String vs_currency = new Currency("USD").getCurrency();


        MarketData marketData = new MarketData("etdfgdh",vs_currency,false);

        System.out.println(marketData.getSymbol() != null);
    }
}
