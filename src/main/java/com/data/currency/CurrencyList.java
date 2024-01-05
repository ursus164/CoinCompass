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
public class CurrencyList {
    private List<String> currencies = null;
    private static final String CURRENCY_LIST_CACHE = "src/cache/currency_list_cache.json";
    private static final Logger logger = LogManager.getLogger(CurrencyList.class);

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

    @Override
    public String toString() {
        return "CurrencyList{" +
                "currencies=" + currencies +
                '}';
    }

    private Optional<String> search(Predicate<String> predicate) {
        return currencies.stream().filter(predicate).findFirst();
    }

    public Optional<String> getBySymbol(String id) {
        return search(currencies -> currencies.equalsIgnoreCase(id));
    }

    public List<String> getAllCurrencies() {
        return currencies;
    }

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
