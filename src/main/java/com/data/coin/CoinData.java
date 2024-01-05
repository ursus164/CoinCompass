package com.data.coin;

import com.data.api.ApiClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;
import java.util.Optional;

public class CoinData {
    /**
     * The CoinData class represents information about a cryptocurrency for e.g "ID", "Symbol" or "Name".
     * It provides methods to initialize the data from an API and retrieve specific coin informations.
     */
    private static final Logger logger = LogManager.getLogger(CoinData.class);
    private static final String COIN_LIST_CACHE = "src/cache/coin_list_cache.json";
    private static final String COIN_LIST_URL = "https://api.coingecko.com/api/v3/coins/list";
    private String id;
    private String symbol;
    private String name;

    /**
     * Constructs a new CoinData object with specified id, symbol and name.
     * @param id        The unique (CoinGecko's) identifier of the coin
     * @param symbol    The symbol of the coin.
     * @param name      The name of the coin.
     */
    public CoinData(String id, String symbol, String name) {
        this.id = id;
        this.symbol = symbol;
        this.name = name;
    }

    /**
     * Constructs a new CoinData object by fetching data from an API.
     * If the data for the specified input is found, it initializes the CoinData object with provided data.
     * Otherwise, logs a warning.
     *
     * @param input     The identifier (id,symbol or name) of the coin.
     * @param update    Boolean flag indicating whether to update the cache when fetching data or not.
     */
    public CoinData(String input, Boolean update) {
        new ApiClient().fetchData(COIN_LIST_URL,COIN_LIST_CACHE,update);

        logger.debug("Initializing CoinData for identifier:" + input);

        // Looks for a specified coin in list of CoinData objects by provided input, and maps it into CoinData class.
        CoinData data = getInfo(input);

        if(data != null) {
            this.id = data.getId();
            this.name = data.getName();
            this.symbol = data.getSymbol();
        } else {
            logger.warn("No data found for identifier: " + input);
        }
    }

    /**
     * Returns a string representation of the CoinData object.
     *
     * @return  A string representation of CoinData object.
     */
    @Override
    public String toString() {
        return "CoinData{" +
                "id='" + id + '\'' +
                ", symbol='" + symbol + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    /**
     * Retrieves coin information based on the provided input (id, name or symbol).
     * Searches in a list of CoinData and returns the matching CoinData object if found.
     * Otherwise, logs a warning and returns null.
     *
     * @param input     The identifier (id, name or symbol) to search for.
     * @return          The CoinData object if found, null otherwise.
     */
    private CoinData getInfo(String input) {

        List<CoinData> jsonList = CoinDataList.convert();
        CoinDataList list = new CoinDataList(jsonList);
        Optional<CoinData> data;

        if ((data = list.getByName(input)).isPresent() || (data = list.getById(input)).isPresent() || (data = list.getBySymbol(input)).isPresent()) {
            logger.info("Coin: " + input + " found");
            return data.get();
        } else {
            logger.warn("Coin: " + input + " not found");
            return null;
        }
    }
    // Getters for id, symbol and name of coin.
    public String getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

}

