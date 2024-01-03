package com.data.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

/**
 * Represents market data for a cryptocurrency. This class includes information
 * such as the cryptocurrency's current price, price change, and high and low values over a 24-hour period.
 * It provides functionality to fetch and initialize market data from an external API.
 */
public class MarketData {
    private static final String MARKET_LIST_CACHE = "src/cache/market_list_cache.json";
    private static final Logger logger = LogManager.getLogger(MarketData.class);
    private String id;
    private String symbol;
    private String name;
    private double current_price;
    private double price_change_24h;
    private double price_change_percentage_24h;
    private double low_24h;
    private double high_24h;
    /**
     * Constructs a MarketData object with specified market details.
     *
     * @param id                           The unique identifier of the cryptocurrency.
     * @param symbol                       The symbol of the cryptocurrency.
     * @param name                         The name of the cryptocurrency.
     * @param current_price                The current price of the cryptocurrency.
     * @param price_change_24h             The price change of the cryptocurrency in the last 24 hours.
     * @param price_change_percentage_24h  The percentage price change of the cryptocurrency in the last 24 hours.
     * @param low_24h                      The lowest price of the cryptocurrency in the last 24 hours.
     * @param high_24h                     The highest price of the cryptocurrency in the last 24 hours.
     */
    public MarketData(String id, String symbol, String name, double current_price,
                      double price_change_24h, double price_change_percentage_24h,
                      double low_24h, double high_24h) {
        this.id = id;
        this.symbol = symbol;
        this.name = name;
        this.current_price = current_price;
        this.price_change_24h = price_change_24h;
        this.price_change_percentage_24h = price_change_percentage_24h;
        this.low_24h = low_24h;
        this.high_24h = high_24h;
    }

    /**
     * Constructs a MarketData object by fetching market data based on the provided market identifier
     * and the vs_currency. If vs_currency is null, it defaults to "usd".
     *
     * @param market      The market identifier for which to fetch data.
     * @param vs_currency The currency against which the market data is to be fetched.
     * @param update      A flag indicating whether to update the data (true) or use existing cache data (false).
     */
    public MarketData(String market,String vs_currency,Boolean update) {
        // Currency z listy currencyList -> obsługiwanych przez giełdę!!!
        if(vs_currency == null) {
            vs_currency = "usd";        //default
        }
        vs_currency = vs_currency.toLowerCase();
        String MARKET_LIST_URL = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=" + vs_currency + "&order=market_cap_desc&per_page=100&page=1&sparkline=false&locale=pl";

        new ApiClient().fetchData(MARKET_LIST_URL,MARKET_LIST_CACHE,update);

        logger.debug("Initializing MarketData for identifier: " + market);

        MarketData data = getInfo(market);
        if(data != null) {
            this.id = data.getId();
            this.name = data.getName();
            this.symbol = data.getSymbol();
            this.current_price = data.getCurrent_price();
            this.price_change_24h = data.getPrice_change_24h();
            this.price_change_percentage_24h = data.getPrice_change_percentage_24h();
            this.low_24h = data.getLow_24h();
            this.high_24h = data.getHigh_24h();
        } else {
            logger.warn("No data found for identifier: " + market);
        }
    }

    /**
     * Provides a string representation of the MarketData object.
     *
     * @return A string containing detailed market information.
     */
    @Override
    public String toString() {
        return "MarketData{" +
                "id='" + id + '\'' +
                ", symbol='" + symbol + '\'' +
                ", name='" + name + '\'' +
                ", current_price=" + current_price +
                ", price_change_24h=" + price_change_24h +
                ", price_change_percentage_24h=" + price_change_percentage_24h +
                ", low_24h=" + low_24h +
                ", high_24h=" + high_24h +
                '}';
    }

    /**
     * Retrieves market data based on the given input.
     *
     * @param input The identifier of the market data to be retrieved.
     * @return A MarketData object containing the fetched data, or null if no data is found.
     */
    private MarketData getInfo(String input) {
        List<MarketData> jsonList = MarketDataList.convert();
        MarketDataList list = new MarketDataList(jsonList);
        Optional<MarketData> data;

        if((data = list.getById(input)).isPresent() || (data = list.getByName(input)).isPresent() || (data = list.getBySymbol(input)).isPresent()) {
            logger.info("Market for identifier:" + input + " found");
            return data.get();
        } else {
            logger.error("Market for identifier:" + input + " not found");
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

    public double getCurrent_price() {
        return current_price;
    }

    public double getPrice_change_24h() {
        return price_change_24h;
    }

    public double getPrice_change_percentage_24h() {
        return price_change_percentage_24h;
    }

    public double getLow_24h() {
        return low_24h;
    }

    public double getHigh_24h() {
        return high_24h;
    }

}
