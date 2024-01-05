package com.data.market;

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
 * The MarketDataList class manages a list of MarketData objects.
 * It provides methods to initialize the list from a cache file, retrieve specific market data, and perform searches.
 */
public class MarketDataList {
    private final List<MarketData> marketDataList;
    private static final Logger logger = LogManager.getLogger(MarketDataList.class);
    private static final String MARKET_LIST_CACHE = "src/cache/market_list_cache.json";

    /**
     * Constructs a new MarketDataList with the given list of MarketData objects
     *
     * @param marketDataList    The list of MarketData objects.
     */
    public MarketDataList(List<MarketData> marketDataList) {
        this.marketDataList = marketDataList;
    }

    /**
     * Searches for a MarketData object in the list based a given predicate.
     * @param predicate The predicate to apply to each MarketData object.
     * @return An optional containing the MarketData object if found, or an empty Optional otherwise.
     */
    private Optional<MarketData> search(Predicate<MarketData> predicate) {
        return marketDataList.stream().filter(predicate).findFirst();
    }

    /**
     * Retrieves a MarketData object from the list by its unique identifier.
     * Uses a case-insensitive comparison to find the matching MarketData.
     *
     * @param Id The unique identifier of the market to search for.
     * @return  An Optional containing the MarketData object if found, or an empty Optional otherwise.
     */
    public Optional<MarketData> getById(String Id) {
        return search(marketData -> marketData.getId().equalsIgnoreCase(Id));
    }

    /**
     * Retrieves a MarketData object from the list by its symbol.
     * Uses a case-insensitive comparison to find the matching MarketData.
     *
     * @param Symbol The symbol of the market to search for.
     * @return  An Optional containing the MarketData object if found, or an empty Optional otherwise.
     */
    public Optional<MarketData> getBySymbol(String Symbol){
        return search(marketData -> marketData.getSymbol().equalsIgnoreCase(Symbol));
    }

    /**
     * Retrieves a MarketData object from the list by its name.
     * Uses a case-insensitive comparison to find the matching MarketData.
     *
     * @param Name  The name of the market to search for.
     * @return  An Optional containing the MarketData object if found, or an empty Optional otherwise.
     */
    public Optional<MarketData> getByName(String Name) {
        return search(marketData -> marketData.getName().equalsIgnoreCase(Name));
    }

    /**
     * Converts the JSON data stored in the cache file into a list of MarketData objects.
     *
     * @return  A list of MarketData objects if successful, null otherwise.
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
                        jsonObject.getDouble("high_24h"),
                        jsonObject.getString("image"),
                        jsonObject.getDouble("ath"),
                        jsonObject.getDouble("atl"),
                        jsonObject.getDouble("market_cap"),
                        jsonObject.getInt("market_cap_rank")
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
     * Reads the cached JSON data of markets from a file.
     *
     * @return  A string containing the JSON data if successful, null otherwise
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
