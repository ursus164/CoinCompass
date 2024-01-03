package com.data.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Manages a collection of MarketData objects.
 * Provides functionality to search for a MarketData object by its ID, name, or symbol,
 * and to convert JSON data from a cache into MarketData objects.
 */
public class MarketDataList {
    private final List<MarketData> marketDataList;
    private static final Logger logger = LogManager.getLogger(MarketDataList.class);
    private static final String MARKET_LIST_CACHE = "src/cache/market_list_cache.json";

    /**
     * Constructs a MarketDataList with a provided list of MarketData objects.
     *
     * @param marketDataList A list of MarketData objects.
     */
    public MarketDataList(List<MarketData> marketDataList) {
        this.marketDataList = marketDataList;
    }

    /**
     * Searches for a MarketData object in the list using a custom predicate.
     *
     * @param predicate A Predicate to apply to each MarketData object.
     * @return An Optional describing the first MarketData object that matches the predicate, or an empty Optional if none match.
     */
    private Optional<MarketData> search(Predicate<MarketData> predicate) {
        return marketDataList.stream().filter(predicate).findFirst();
    }

    /**
     * Searches for a MarketData object by its ID.
     *
     * @param Id The ID of the MarketData object to search for.
     * @return An Optional describing the MarketData object with the given ID, or an empty Optional if none found.
     */
    public Optional<MarketData> getById(String Id) {
        return search(marketData -> marketData.getId().equalsIgnoreCase(Id));
    }

    /**
     * Searches for a MarketData object by its symbol.
     *
     * @param Symbol The symbol of the MarketData object to search for.
     * @return An Optional describing the MarketData object with the given symbol, or an empty Optional if none found.
     */
    public Optional<MarketData> getBySymbol(String Symbol){
        return search(marketData -> marketData.getSymbol().equalsIgnoreCase(Symbol));
    }

    /**
     * Searches for a MarketData object by its name.
     *
     * @param Name The name of the MarketData object to search for.
     * @return An Optional describing the MarketData object with the given name, or an empty Optional if none found.
     */
    public Optional<MarketData> getByName(String Name) {
        return search(marketData -> marketData.getName().equalsIgnoreCase(Name));
    }

    /**
     * Converts JSON data from a cache file into a list of MarketData objects.
     *
     * @return A list of MarketData objects created from the JSON data, or null if unable to perform the conversion.
     */
    public static List<MarketData> convert() {
        String json = readCache();
        if(json != null) {
            JSONArray jsonArray = new JSONArray(json);
            List<MarketData> marketDataList = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                MarketData marketData = new MarketData(
                        jsonObject.getString("id"),
                        jsonObject.getString("symbol"),
                        jsonObject.getString("name"),
                        jsonObject.getDouble("current_price"),
                        jsonObject.getDouble("price_change_24h"),
                        jsonObject.getDouble("price_change_percentage_24h"),
                        jsonObject.getDouble("low_24h"),
                        jsonObject.getDouble("high_24h")
                );
                marketDataList.add(marketData);
            }
            logger.info("Successfully created list of MarketData objects");
            return marketDataList;
        }
        logger.error("Unable to convert data from: " + MARKET_LIST_CACHE + " to list of MarketData objects");
        return null;
    }

    /**
     * Reads data from the cache file.
     *
     * @return A String containing the contents of the cache file, or null if the file cannot be read.
     */
    private static String readCache() {
        try {
            File file = new File(MARKET_LIST_CACHE);
            if(file.exists()) {
                logger.info("Reading: " + MARKET_LIST_CACHE);
                return new String(Files.readAllBytes(Paths.get(MARKET_LIST_CACHE)));
            }
        } catch (IOException e) {
            logger.fatal("File: " + MARKET_LIST_CACHE + " does not exist!");
        }
        return null;
    }
}
