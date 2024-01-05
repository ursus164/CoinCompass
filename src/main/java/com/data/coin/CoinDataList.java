package com.data.coin;

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

public class CoinDataList {
    private static final Logger logger = LogManager.getLogger(CoinDataList.class);
    private static final String COIN_LIST_CACHE = "src/cache/coin_list_cache.json";
    private final List<CoinData> coinDataList;

    public CoinDataList(List<CoinData> coinDataList) {
        this.coinDataList = coinDataList;
    }

    private Optional<CoinData> search(Predicate<CoinData> predicate) {
        return coinDataList.stream().filter(predicate).findFirst();
    }

    public Optional<CoinData> getById(String id) {
        return search(data -> data.getId().equalsIgnoreCase(id));
    }

    public Optional<CoinData> getByName(String name) {
        return search(data -> data.getName().equalsIgnoreCase(name));
    }

    public Optional<CoinData> getBySymbol(String symbol) {
        return search(data -> data.getSymbol().equalsIgnoreCase(symbol));
    }

    public static List<CoinData> convert() {
        String json = readCache();

        if(json != null) {
            JSONArray jsonArray = new JSONArray(json);
            List<CoinData> coinDataList = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                CoinData coinData = new CoinData(
                        jsonObject.getString("id"),
                        jsonObject.getString("symbol"),
                        jsonObject.getString("name")
                );
                coinDataList.add(coinData);
            }
            logger.info("Sucessfully created list of CoinData objects");
            return coinDataList;

        }
        logger.error("Unable to convert data from: " + COIN_LIST_CACHE + " to list of CoinData objects ");
        return null;
    }

    private static String readCache() {
        try {
            File file = new File(COIN_LIST_CACHE);
            if(file.exists()) {
                logger.info("Reading: " + COIN_LIST_CACHE);

                return new String(Files.readAllBytes(Paths.get(COIN_LIST_CACHE)));
            }
        } catch (IOException e) {
            logger.fatal("File: " + COIN_LIST_CACHE + " does not exist!");
        }
        return null;
    }
}
