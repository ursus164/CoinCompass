package com.data.coin;

import com.data.api.ApiClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;
import java.util.Optional;

public class CoinData {
    private static final Logger logger = LogManager.getLogger(CoinData.class);
    private static final String COIN_LIST_CACHE = "src/cache/coin_list_cache.json";
    private static final String COIN_LIST_URL = "https://api.coingecko.com/api/v3/coins/list";
    private String id;
    private String symbol;
    private String name;

    public CoinData(String id, String symbol, String name) {
        this.id = id;
        this.symbol = symbol;
        this.name = name;

    }

    public CoinData() {}

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

    @Override
    public String toString() {
        return "CoinData{" +
                "id='" + id + '\'' +
                ", symbol='" + symbol + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

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

