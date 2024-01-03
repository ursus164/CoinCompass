package com.data.currency;

import com.data.api.ApiClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

/**
 * Represents a currency and provides functionality to fetch its details from an external API.
 * The class supports fetching a list of supported currencies and finding a specific currency based on an identifier.
 */
public class Currency {
    private String val;
    private static final String CURRENCY_LIST_CACHE = "src/cache/currency_list_cache.json";
    private static final String CURRENCY_LIST_URL = "https://api.coingecko.com/api/v3/simple/supported_vs_currencies";
    private static final Logger logger = LogManager.getLogger(Currency.class);

    /**
     * Constructs a Currency object for the given currency identifier.
     * Fetches the currency data from the API (or uses cached data if told to do so) and initializes the object with it.
     *
     * @param currency The identifier of the currency to be fetched.
     */
    public Currency(String currency) {
        logger.debug("Initializing Currency object for identifier: " + currency);
        new ApiClient().fetchData(CURRENCY_LIST_URL,CURRENCY_LIST_CACHE,false);
        val = getInfo(currency);
    }

    /**
     * Provides a string representation of the Currency object.
     *
     * @return A string representation of the Currency object.
     */
    @Override
    public String toString() {
        return "Currency{" +
                "Currency='" + val + '\'' +
                '}';
    }

    /**
     * Gets the value of the currency.
     *
     * @return The value of the currency.
     */
    public String getCurrency() {
        return val;
    }

    /**
     * Retrieves information about a currency based on the given identifier.
     * If the currency is not found, defaults to "USD".
     *
     * @param input The identifier of the currency to be retrieved.
     * @return The currency value if found, or "usd" as a default.
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
