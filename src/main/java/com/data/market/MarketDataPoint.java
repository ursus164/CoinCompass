package com.data.market;

public class MarketDataPoint {
    private long timestamp;
    private double value;
    private static final String MARKET_CHART_CACHE = "src/cache/coin_list_cache.json";
    private static final String MARKET_CHART_URL = "https://api.coingecko.com/api/v3/coins/list";

    public MarketDataPoint(long timestamp, double value) {
        this.timestamp = timestamp;
        this.value = value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public double getValue() {
        return value;
    }
}
