import com.data.currency.Currency;
import com.data.currency.CurrencyList;

import java.lang.reflect.Type;
import java.util.List;


public class CurrencyListTest {
    public static void main(String[] args) {
        String currency = new Currency("usd").getCurrency();
        System.out.println(currency);

        CurrencyList list = new CurrencyList();
        List<String> currencies = list.getAllCurrencies();
        System.out.println(currencies);
    }
}
