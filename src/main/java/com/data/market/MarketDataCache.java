package com.data.market;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * The MarketDataCache class provides a caching mechanism for MarketData objects.
 * It utilizes a static Map to store and retrieve MarketData based on a composite key.
 */
public class MarketDataCache {
    private static final Map<String, MarketData> cache = new HashMap<>();
    private static final Logger logger = LogManager.getLogger(MarketDataCache.class);

    /**
     * Retrieves MarketData from the cache or creates it if not present.
     * The method constructs a key based on the searchText, currency, and autoRefresh parameters.
     * If the MarketData for the key is not in the cache, it creates and caches a new MarketData object.
     *
     * @param searchText    The search text used to identify the market.
     * @param currency      The currency used in the market data.
     * @param autoRefresh   Boolean flag indicating whether data should be auto-refreshed.
     * @return  The cached or newly created MarketData object.
     */
    public static MarketData getMarketData(String searchText, String currency, Boolean autoRefresh) {
        String key = searchText + "_" + currency + "_" + autoRefresh;

        if (!cache.containsKey(key)) {
            cache.put(key, new MarketData(searchText, currency, autoRefresh));

            logger.debug("Initializing new MarketData object for key: " + key);
        }
        return cache.get(key);
    }

    /**
     * Clears the cache for a specific set of parameters.
     * Constructs a cache key using searchText, currency, and autoRefresh parameters
     * and removes the corresponding entry from the cache.
     *
     * @param searchText    The search text used to identify the market.
     * @param currency      The currency used in the market data.
     * @param autoRefresh   Boolean flag indicating whether the data should be auto-refreshed.
     */
    public static void clearCacheFor(String searchText, String currency, Boolean autoRefresh) {
        String key = searchText + "_" + currency + "_" + autoRefresh;
        cache.remove(key);

        logger.debug("Deleting entry from cache corresponding with key: " + key);
    }
}
