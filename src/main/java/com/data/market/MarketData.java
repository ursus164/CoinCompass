package com.data.market;

import com.data.api.ApiClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;
import java.util.Optional;

/**
 * The MarketData class represents detailed information about a specific market.
 * It includes various attributes like price, volume, market cap and others.
 */
public class MarketData {
    private static final String MARKET_LIST_CACHE = "src/cache/market_list_cache.json";
    private static final Logger logger = LogManager.getLogger(MarketData.class);
    private String id, symbol, name, IconUrl, currency;
    private double current_price, price_change_24h, price_change_percentage_24h,
            low_24h, high_24h, ath, atl, market_cap;
    private int market_cap_rank;

    /**
     * Constructs a new MarketData object with specific market details.
     *
     * @param id                            The market identifier.
     * @param symbol                        The market symbol.
     * @param name                          The market name.
     * @param current_price                 The current price of the market.
     * @param price_change_24h              The price change in the last 24 hours.
     * @param price_change_percentage_24h   The price change percentage in the last 24 hours.
     * @param low_24h                       The lowest price in the last 24 hours.
     * @param high_24h                      The highest price in the last 24 hours.
     * @param iconUrl                       The URL of the market icon.
     * @param ath                           All-time high price.
     * @param atl                           All-time low price.
     * @param market_cap                    The market capitalization
     * @param market_cap_rank               The market capitalization rank.
     */
    public MarketData(String id, String symbol, String name, double current_price,
                      double price_change_24h, double price_change_percentage_24h,
                      double low_24h, double high_24h, String iconUrl, double ath,
                      double atl, double market_cap, int market_cap_rank) {
        this.id = id;
        this.symbol = symbol;
        this.name = name;
        this.current_price = current_price;
        this.price_change_24h = price_change_24h;
        this.price_change_percentage_24h = price_change_percentage_24h;
        this.low_24h = low_24h;
        this.high_24h = high_24h;
        this.IconUrl = iconUrl;
        this.ath = ath;
        this.atl = atl;
        this.market_cap = market_cap;
        this.market_cap_rank = market_cap_rank;
    }

    /**
     * Constructs a new MarketData object by fetching data from an API for a given market.
     * If the data for the specified market is found, it initializes the MarketData object with this data.
     *
     * @param market        The identifier of the market. For e.g. 'bitcoin'
     * @param vs_currency   The currency to compare against.
     * @param update        Boolean flag indicating whether to update the cache when fetching data.
     */
    public MarketData(String market,String vs_currency,Boolean update) {

        if(vs_currency == null) {
            vs_currency = "usd";        //set default
        }
        vs_currency = vs_currency.toLowerCase();
        String MARKET_LIST_URL = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=" + vs_currency + "&order=market_cap_desc&per_page=100&page=1&sparkline=false&locale=pl&precision=6";

        new ApiClient().fetchData(MARKET_LIST_URL,MARKET_LIST_CACHE,update);

        logger.debug("Initializing MarketData for identifier: " + market);

        MarketData data = getInfo(market);    // helper object to initialize data

        if(data != null) {
            this.id = data.getId();
            this.name = data.getName();
            this.symbol = data.getSymbol();
            this.current_price = data.getCurrent_price();
            this.price_change_24h = data.getPrice_change_24h();
            this.price_change_percentage_24h = data.getPrice_change_percentage_24h();
            this.low_24h = data.getLow_24h();
            this.high_24h = data.getHigh_24h();
            this.IconUrl = data.getIconUrl();
            this.ath = data.getAth();
            this.atl = data.getAtl();
            this.market_cap_rank = data.getMarket_cap_rank();
            this.market_cap = data.getMarket_cap();
        } else {
            logger.warn("No data found for identifier: " + market);
        }
    }

    /**
     * Returns a string representation of the MarketData object.
     *
     * @return  A string representation of the MarketData object.
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
                ", IconUrl='" + IconUrl + '\'' +
                ", ath=" + ath +
                ", atl=" + atl +
                ", market_cap=" + market_cap +
                ", market_cap_rank=" + market_cap_rank +
                '}';
    }

    /**
     * Retrieves market information based on the provided input (id, name or symbol).
     * Searches in a list of MarketData and returns the matching MarketData object if found.
     *
     * @param input The identifier (id, name or symbol) to search for.
     * @return      The MarketData object if found, null otherwise.
     */
    private MarketData getInfo(String input) {

        List<MarketData> jsonList = MarketDataList.convert();
        MarketDataList list = new MarketDataList(jsonList);
        Optional<MarketData> data;

        if((data = list.getById(input)).isPresent() || (data = list.getByName(input)).isPresent()
                || (data = list.getBySymbol(input)).isPresent()) {

            logger.info("Market for identifier:" + input + " found");
            return data.get();

        } else {
            logger.error("Market for identifier:" + input + " not found");
            return null;

        }
    }

    // Getters and setters for all attributes.
    public String getCurrency() {
        return currency;
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
    public String getIconUrl() {
        return IconUrl;
    }
    public double getAth() {
        return ath;
    }
    public double getAtl() {
        return atl;
    }
    public double getMarket_cap() {
        return market_cap;
    }
    public int getMarket_cap_rank() {
        return market_cap_rank;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
