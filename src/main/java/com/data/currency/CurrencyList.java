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
 * Manages a list of currency symbols.
 * The class is capable of reading currency data from a cache file and provides methods
 * to search and retrieve currency symbols based on certain criteria.
 */
public class CurrencyList {
    private List<String> currencies = null;
    private static final String CURRENCY_LIST_CACHE = "src/cache/currency_list_cache.json";
    private static final Logger logger = LogManager.getLogger(CurrencyList.class);

    /**
     * Constructs a CurrencyList and initializes it by reading currency data from a cache file.
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
     * Provides a string representation of the CurrencyList.
     *
     * @return A string representation of the CurrencyList.
     */
    @Override
    public String toString() {
        return "CurrencyList{" +
                "currencies=" + currencies +
                '}';
    }

    /**
     * Searches for a currency symbol in the list based on a given predicate.
     *
     * @param predicate A Predicate to apply to each currency symbol.
     * @return An Optional describing the first currency symbol that matches the predicate, or an empty Optional if none match.
     */
    private Optional<String> search(Predicate<String> predicate) {
        return currencies.stream().filter(predicate).findFirst();
    }

    /**
     * Retrieves a currency symbol by its identifier.
     *
     * @param id The identifier of the currency symbol to retrieve.
     * @return An Optional containing the currency symbol if found, or an empty Optional if not found.
     */
    public Optional<String> getBySymbol(String id) {
        return search(currencies -> currencies.equalsIgnoreCase(id));
    }

    /**
     * Gets the list of all currency symbols.
     *
     * @return The list of currency symbols.
     */
    public List<String> getAllCurrencies() {
        return currencies;
    }

    /**
     * Reads the currency data from the cache file.
     *
     * @return A String containing the currency data, or null if the file cannot be read.
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
