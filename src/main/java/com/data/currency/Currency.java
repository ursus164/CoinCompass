package com.data.currency;

import com.data.api.ApiClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

/**
 * The Currency class represents a currency used in the context of cryptocurrency markets and transactions.
 * It provides functionality to initialize and retrieve currency information.
 */
public class Currency {
    private final String val;
    private static final String CURRENCY_LIST_CACHE = "src/cache/currency_list_cache.json";
    private static final String CURRENCY_LIST_URL = "https://api.coingecko.com/api/v3/simple/supported_vs_currencies";
    private static final Logger logger = LogManager.getLogger(Currency.class);

    /**
     * Constructs a new Currency object for a given currency identifier.
     * It attempts to fetch currency information from a list of CoinGecko's supported currencies.
     * If the currency is not found, it defaults to "usd"
     *
     * @param currency  The identifier of the currency.
     */
    public Currency(String currency) {
        logger.debug("Initializing Currency object for identifier: " + currency);

        new ApiClient().fetchData(CURRENCY_LIST_URL,CURRENCY_LIST_CACHE,false);
        val = getInfo(currency);
    }

    /**
     * Returns a string representation of the Currency object.
     *
     * @return  A string representation of the Currency object.
     */
    @Override
    public String toString() {
        return "Currency{" +
                "Currency='" + val + '\'' +
                '}';
    }

    /**
     * Retrieves the currency value.
     *
     * @return  The currency value.
     */
    public String getCurrency() {
        return val;
    }

    /**
     * Retrieves currency information for the specified input.
     * If the currency is found, it is returned. Otherwise, it logs a warning and returns "usd" by default.
     *
     * @param input The identifier for the currency to search
     * @return      The currency if found, or "usd" by default if not found.
     */
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
