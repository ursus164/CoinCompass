package com.data.market;

import java.util.ArrayList;
import java.util.List;

public class MarketDataSeries {
    private List<MarketDataPoint> prices = new ArrayList<>();

    public void addPrice(long timestamp, double price) {
        prices.add(new MarketDataPoint(timestamp,price));
    }

    public List<MarketDataPoint> getPrices() {
        return prices;
    }
}
