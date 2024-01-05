package com.data.market;

import com.data.api.ApiClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MarketDataPoint {
    private long timestamp;
    private double value;
    private static final String MARKET_CHART_CACHE = "src/cache/market_chart_cache.json";
    private static final Logger logger = LogManager.getLogger(MarketDataPoint.class);

    public MarketDataPoint(long timestamp, double value) {
        this.timestamp = timestamp;
        this.value = value;
    }
    @Override
    public String toString() {
        return "MarketDataPoint{" +
                "timestamp=" + timestamp +
                ", value=" + value +
                '}';
    }

    public MarketDataPoint(String market, String vs_currency, int days) {

        if(vs_currency == null) {
            vs_currency = "usd";        //default
        }
        vs_currency = vs_currency.toLowerCase();
        String MARKET_CHART_URL = "https://api.coingecko.com/api/v3/coins/" + market + "/market_chart?vs_currency=" + vs_currency + "&days=" + days;

        new ApiClient().fetchData(MARKET_CHART_URL,MARKET_CHART_CACHE,true);

        logger.debug("Initializing market chart for identifier: " + market);

    }
    public long getTimestamp() {
        return timestamp;
    }

    public double getValue() {
        return value;
    }
}
