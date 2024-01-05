package com.data.market;

import java.util.HashMap;
import java.util.Map;

public class MarketDataCache {
    private static final Map<String, MarketData> cache = new HashMap<>();

    public static MarketData getMarketData(String searchText, String currency, Boolean autoRefresh) {
        String key = searchText + "_" + currency + "_" + autoRefresh;

        if (!cache.containsKey(key)) {
            cache.put(key, new MarketData(searchText, currency, autoRefresh));
        }
        return cache.get(key);
    }
    public static void clearCacheFor(String searchText, String currency, Boolean autoRefresh) {
        String key = createCacheKey(searchText, currency, autoRefresh);

        cache.remove(key);
    }

    private static String createCacheKey(String searchText, String currency, Boolean autoRefresh) {
        return searchText + "_" + currency + "_" + autoRefresh;
    }
}
