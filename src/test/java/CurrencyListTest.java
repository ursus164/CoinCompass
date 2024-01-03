<<<<<<< HEAD
import com.data.currency.Currency;
=======
import com.data.api.ApiClient;
import com.data.api.Currency;
import com.data.api.CurrencyList;

import java.util.List;
import java.util.Optional;
>>>>>>> acca7d816116538d9b3c20abe92bd139e78cf19c

public class CurrencyListTest {
    public static void main(String[] args) {
        String currency = new Currency("usd").getCurrency();
        System.out.println(currency);
    }
}
