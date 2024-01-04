package com.data.gui;

import com.data.market.MarketData;
import java.util.LinkedList;
import java.util.List;


public class HistoryManager {
    private static HistoryManager instance;
    private LinkedList<MarketData> history;

    private HistoryManager() {
        history = new LinkedList<>();
    }

    public static HistoryManager getInstance() {
        if (instance == null) {
            instance = new HistoryManager();
        }
        return instance;
    }

    public void addSearch(MarketData data) {
        history.remove(data);
        history.addFirst(data);
        while (history.size() > 4) {
            history.removeLast();
        }
    }
    public List<MarketData> getHistory() {
        return new LinkedList<>(history);
    }
}

