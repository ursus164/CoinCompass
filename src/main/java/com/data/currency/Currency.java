package com.data.currency;

import com.data.api.ApiClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class Currency {
    private String val;
    private static final String CURRENCY_LIST_CACHE = "src/cache/currency_list_cache.json";
    private static final String CURRENCY_LIST_URL = "https://api.coingecko.com/api/v3/simple/supported_vs_currencies";
    private static final Logger logger = LogManager.getLogger(Currency.class);

    public Currency(String currency) {
        logger.debug("Initializing Currency object for identifier: " + currency);
        new ApiClient().fetchData(CURRENCY_LIST_URL,CURRENCY_LIST_CACHE,false);
        val = getInfo(currency);
    }

    @Override
    public String toString() {
        return "Currency{" +
                "Currency='" + val + '\'' +
                '}';
    }
    public String getCurrency() {
        return val;
    }

    private String getInfo(String input) {
        logger.debug("Initializing Currency list for identifier: " + input);

        CurrencyList list = new CurrencyList();
        Optional<String> data = list.getBySymbol(input);

        if(data.isPresent()) {
            logger.info("Currency: " + input + " found!");
            return data.get();
        } else {
            logger.warn("Currency: " + input + " not found in supported Currencies list. Using default: USD");
            return "usd";       // default value if not found
        }
    }
}
