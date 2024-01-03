<<<<<<< HEAD
import com.data.currency.Currency;
import com.data.market.MarketData;
=======
import com.data.api.*;

import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
>>>>>>> acca7d816116538d9b3c20abe92bd139e78cf19c

public class MarketDataTest {
    public static void main(String[] args) {
        String vs_currency = new Currency("USD").getCurrency();

<<<<<<< HEAD
        MarketData marketData = new MarketData("eth",vs_currency,false);
=======
        MarketData marketData = new MarketData("eth",vs_currency,true);
>>>>>>> acca7d816116538d9b3c20abe92bd139e78cf19c
        System.out.println(marketData.getCurrent_price());
    }
}
