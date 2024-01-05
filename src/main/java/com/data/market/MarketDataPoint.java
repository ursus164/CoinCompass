package com.data.market;

import com.data.api.ApiClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The MarketDataPoint class represents a single data point in market data
 * typically consisting of a timestamp and a corresponding price.
 */
public class MarketDataPoint {
    private long timestamp;
    private double value;
    private static final String MARKET_CHART_CACHE = "src/cache/market_chart_cache.json";
    private static final Logger logger = LogManager.getLogger(MarketDataPoint.class);

    /**
     * Constructs a new MarketDataPoint with the specified timestamp, and value (price).
     *
     * @param timestamp The timestamp of the data point, representing a specific moment in time.
     * @param value     The value associated with this data point - specific price.
     */
    public MarketDataPoint(long timestamp, double value) {
        this.timestamp = timestamp;
        this.value = value;
    }

    /**
     * Returns a string representation of the MarketDataPoint object.
     *
     * @return A string representation of the MarketDataPoint object.
     */
    @Override
    public String toString() {
        return "MarketDataPoint{" +
                "timestamp=" + timestamp +
                ", value=" + value +
                '}';
    }

    /**
     * Constructs a MarketDataPoint by fetching market chart data for a specific market
     * and currency over a given number of days
     *
     * @param market        The market identifier for which to fetch chart data.
     * @param vs_currency   The currency against which market data is compared.
     * @param days          The number of days for which to fetch market data.
     */
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
