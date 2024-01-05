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

public class MarketDataList {
    private final List<MarketData> marketDataList;
    private static final Logger logger = LogManager.getLogger(MarketDataList.class);
    private static final String MARKET_LIST_CACHE = "src/cache/market_list_cache.json";

    public MarketDataList(List<MarketData> marketDataList) {
        this.marketDataList = marketDataList;
    }

    private Optional<MarketData> search(Predicate<MarketData> predicate) {
        return marketDataList.stream().filter(predicate).findFirst();
    }

    public Optional<MarketData> getById(String Id) {
        return search(marketData -> marketData.getId().equalsIgnoreCase(Id));
    }

    public Optional<MarketData> getBySymbol(String Symbol){
        return search(marketData -> marketData.getSymbol().equalsIgnoreCase(Symbol));
    }
    public Optional<MarketData> getByName(String Name) {
        return search(marketData -> marketData.getName().equalsIgnoreCase(Name));
    }

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
