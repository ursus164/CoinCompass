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

public class MarketDataSeries {
    private static final String MARKET_CHART_CACHE = "src/cache/market_chart_cache.json";
    private static final Logger logger = LogManager.getLogger(MarketDataSeries.class);
    private final List<MarketDataPoint> prices;

    public MarketDataSeries(List<MarketDataPoint> marketDataPoints) {
        this.prices = marketDataPoints;
    }

    public static List<MarketDataPoint> convert() {
        String json = readCache();

        List<MarketDataPoint> marketDataPoints = new ArrayList<>();

        if(json != null) {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray pricesArray = jsonObject.getJSONArray("prices");

            for(int i = 0; i < pricesArray.length(); i++) {
                JSONArray priceData = pricesArray.getJSONArray(i);

                MarketDataPoint marketDataPoint = new MarketDataPoint(
                  priceData.getLong(0),
                  priceData.getDouble(1)
                );
                marketDataPoints.add(marketDataPoint);
            }
            logger.info("Successfully created chart points list");
            return marketDataPoints;
        }
        logger.error("Unable to create list chart points from: " + MARKET_CHART_CACHE);
        return null;
    }

    private static String readCache() {
        try {
            File file = new File(MARKET_CHART_CACHE);

            if(file.exists()) {
                logger.info("Reading: " + MARKET_CHART_CACHE);
                return new String(Files.readAllBytes(Paths.get(MARKET_CHART_CACHE)));
            }
        } catch (IOException e) {
            logger.fatal("File: " + MARKET_CHART_CACHE + " does not exist!");
        }
        return null;
    }
}
