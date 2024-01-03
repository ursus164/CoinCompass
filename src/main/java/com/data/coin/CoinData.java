package com.data.coin;

import com.data.api.ApiClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;
/**
 * Represents information about a cryptocurrency. This class can initialize data from a provided identifier,
 * which can be an id, name, or symbol of the cryptocurrency. It also supports fetching data from an external API,
 * by anonymous instance of ApiClient class.
 */
public class CoinData {
    private static final Logger logger = LogManager.getLogger(CoinData.class);
    private static final String COIN_LIST_CACHE = "src/cache/coin_list_cache.json";
    private static final String COIN_LIST_URL = "https://api.coingecko.com/api/v3/coins/list";
    private String id;
    private String symbol;
    private String name;

    /**
     * Constructs a new CoinData instance with specified id, symbol, and name.
     *
     * @param id     The unique identifier of the cryptocurrency.
     * @param symbol The symbol of the cryptocurrency.
     * @param name   The name of the cryptocurrency.
     */
    public CoinData(String id, String symbol, String name) {
        this.id = id;
        this.symbol = symbol;
        this.name = name;

    }

    /**
     * Default constructor for creating an empty CoinData instance.
     */
    public CoinData() {}

    /**
     * Constructs a CoinData instance by fetching data based on the provided input.
     * The input can be an id, name, or symbol of the cryptocurrency. For provided name,
     * other parameters are found (like symbol or id).
     *
     * @param input The identifier (id, name, or symbol) of the cryptocurrency.
     */
    public CoinData(String input, Boolean update) {
        new ApiClient().fetchData(COIN_LIST_URL,COIN_LIST_CACHE,update);
        logger.debug("Initializing CoinData for identifier:" + input);

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
     * Provides a string representation of the CoinData instance.
     *
     * @return A string representation of the CoinData instance.
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
     * Retrieves information about a cryptocurrency based on the given input.
     * The method is private and used internally within the class.
     *
     * @param input The identifier (id, name, or symbol) of the cryptocurrency.
     * @return CoinData instance with the fetched data or null if no data found.
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

    /**
     * Gets the unique identifier of the cryptocurrency.
     *
     * @return The unique identifier of the cryptocurrency.
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the symbol of the cryptocurrency.
     *
     * @return The symbol of the cryptocurrency.
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Gets the name of the cryptocurrency.
     *
     * @return The name of the cryptocurrency.
     */
    public String getName() {
        return name;
    }

}

