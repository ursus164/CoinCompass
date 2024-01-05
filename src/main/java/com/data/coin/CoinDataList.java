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

/**
 * The CoinDataList class represents a list of CoinData objects.
 * It provides functionality to search for coins based on various attributes like symbol, id or name,
 * also it converts JSON data into a list of CoinData objects.
 *
 */

public class CoinDataList {
    private static final Logger logger = LogManager.getLogger(CoinDataList.class);
    private static final String COIN_LIST_CACHE = "src/cache/coin_list_cache.json";
    private final List<CoinData> coinDataList;

    /**
     * Constructs a new CoinDataList with the given list of CoinData.
     * @param coinDataList
     */
    public CoinDataList(List<CoinData> coinDataList) {
        this.coinDataList = coinDataList;
    }

    /**
     * Searches for a CoinData object in the list based on a given predicate.
     * @param predicate The predicate to apply to each CoinData object.
     * @return          An Optional containing the CoinData object if found, or an empty Optional otherwise
     */
    private Optional<CoinData> search(Predicate<CoinData> predicate) {
        return coinDataList.stream().filter(predicate).findFirst();
    }

    /**
     * Retrieves a CoinData object from the list by its unique identifier (ID).
     * It uses a case-insensitive comparison to find the matching CoinData.
     *
     * @param id    The unique identifier of the coin to search for.
     * @return      An Optional containing the CoinData object if found, or an empty Optional otherwise.
     */
    public Optional<CoinData> getById(String id) {
        return search(data -> data.getId().equalsIgnoreCase(id));
    }

    /**
     * Retrieves a CoinData object from the list by its name.
     * It uses a case-insensitive comparison to find the matching CoinData.
     *
     * @param name The name of the coin to search for.
     * @return An Optional containing the CoinData object if found, or an empty Optional otherwise.
     */
    public Optional<CoinData> getByName(String name) {
        return search(data -> data.getName().equalsIgnoreCase(name));
    }

    /**
     * Retrieves a CoinData object from the list by its symbol.
     * It uses a case-insensitive comparison to find the matching CoinData.
     *
     * @param symbol The symbol of the coin to search for.
     * @return An Optional containing the CoinData object if found, or an empty Optional otherwise.
     */
    public Optional<CoinData> getBySymbol(String symbol) {
        return search(data -> data.getSymbol().equalsIgnoreCase(symbol));
    }

    /**
     * Converts the JSON data stored in the cache file into a list of CoinData objects.
     *
     * @return A list of CoinData objects if successful, null otherwise.
     */
    public static List<CoinData> convert() {
        String json = readCache();

        if(json != null) {
            JSONArray jsonArray = new JSONArray(json);              // API response contains array of JSON objects.
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
     * Reads the cached JSON data from a file.
     *
     * @return  A string containing JSON data if successful, null otherwise.
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
