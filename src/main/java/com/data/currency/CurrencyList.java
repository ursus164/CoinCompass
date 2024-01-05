package com.data.currency;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * The CurrencyList class handles the collection of supported currency strings.
 * It provides methods to initialize the list from a cache file and retrieve specific currency information.
 */
public class CurrencyList {
    private List<String> currencies = null;
    private static final String CURRENCY_LIST_CACHE = "src/cache/currency_list_cache.json";
    private static final Logger logger = LogManager.getLogger(CurrencyList.class);

    /**
     * Constructs a new CurrencyList object.
     * Initializes the list of currencies from a cached JSON file.
     */
    public CurrencyList() {
        logger.debug("Initializing CurrencyList class");
        String json = readCache();

        if(json != null) {
            JSONArray array = new JSONArray(json);
            this.currencies = new ArrayList<>();

            for (int i = 0; i < array.length(); i++) {
                this.currencies.add(array.getString(i));
            }
            logger.info("Sucesfully created list of supported currencies");
        } else {
            logger.warn("Cache file might be null");
        }
    }

    /**
     * Returns a string representation of the CurrencyList object.
     *
     * @return  A string representation of the CurrencyList object.
     */
    @Override
    public String toString() {
        return "CurrencyList{" +
                "currencies=" + currencies +
                '}';
    }

    /**
     * Searches for a currency in the list based on a given predicate.
     *
     * @param predicate The predicate to apply to each currency String
     * @return  An Optional containing the currency string if found, or an empty Optional otherwise.
     */
    private Optional<String> search(Predicate<String> predicate) {
        return currencies.stream().filter(predicate).findFirst();
    }

    /**
     * Retrieves a currency string from the list by its symbol.
     * Uses a case-insensitive comparison to find the matching currency.
     *
     * @param id    The symbol of the currency to search for.
     * @return      An Optional containing the currency string if found, or empty Optional otherwise.
     */
    public Optional<String> getBySymbol(String id) {
        return search(currencies -> currencies.equalsIgnoreCase(id));
    }

    /**
     * Retrieves the list of all supported currencies.
     *
     * @return  A list containing all currency strings.
     */
    public List<String> getAllCurrencies() {
        return currencies;
    }

    /**
     * Reads the cached JSON data of currencies from a file.
     *
     * @return  A string containing the JSON data if successful, null otherwise.
     */
    private static String readCache() {
        try {
            File file = new File(CURRENCY_LIST_CACHE);
            if(file.exists()) {
                logger.info("Reading: " + CURRENCY_LIST_CACHE);
                return new String(Files.readAllBytes(Paths.get(CURRENCY_LIST_CACHE)));
            }
        } catch (IOException e) {
            logger.fatal("File: " + CURRENCY_LIST_CACHE + "does not exist!");
        }
        return null;
    }
}
