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
 * Manages a collection of CoinData objects. Provides functionality to search for a CoinData
 * object by its id, name, or symbol, and also to convert JSON data from a cache into CoinData objects.
 */
public class CoinDataList {
    private static final Logger logger = LogManager.getLogger(CoinDataList.class);
    private static final String COIN_LIST_CACHE = "src/cache/coin_list_cache.json";
    private final List<CoinData> coinDataList;

    /**
     * Constructs a CoinDataList with a provided list of CoinData objects.
     *
     * @param coinDataList A list of CoinData objects.
     */
    public CoinDataList(List<CoinData> coinDataList) {
        this.coinDataList = coinDataList;
    }

    /**
     * Searches for a CoinData object in the list using a custom predicate.
     *
     * @param predicate A Predicate to apply to each CoinData object.
     * @return An Optional describing the first CoinData object that matches the predicate, or an empty Optional if none match.
     */
    private Optional<CoinData> search(Predicate<CoinData> predicate) {
        return coinDataList.stream().filter(predicate).findFirst();
    }

    /**
     * Searches for a CoinData object by its ID.
     *
     * @param id The ID of the CoinData object to search for.
     * @return An Optional describing the CoinData object with the given ID, or an empty Optional if none found.
     */
    public Optional<CoinData> getById(String id) {
        return search(data -> data.getId().equalsIgnoreCase(id));
    }

    /**
     * Searches for a CoinData object by its name.
     *
     * @param name The name of the CoinData object to search for.
     * @return An Optional describing the CoinData object with the given name, or an empty Optional if none found.
     */
    public Optional<CoinData> getByName(String name) {
        return search(data -> data.getName().equalsIgnoreCase(name));
    }

    /**
     * Searches for a CoinData object by its symbol.
     *
     * @param symbol The symbol of the CoinData object to search for.
     * @return An Optional describing the CoinData object with the given symbol, or an empty Optional if none found.
     */
    public Optional<CoinData> getBySymbol(String symbol) {
        return search(data -> data.getSymbol().equalsIgnoreCase(symbol));
    }

    /**
     * Converts JSON data from a cache file into a list of CoinData objects.
     *
     * @return A list of CoinData objects created from the JSON data, or null if unable to perform the conversion.
     */
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

    /**
     * Reads data from the cache file.
     *
     * @return A String containing the contents of the cache file, or null if the file cannot be read.
     */
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
