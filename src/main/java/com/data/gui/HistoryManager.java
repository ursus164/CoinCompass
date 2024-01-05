package com.data.gui;

import com.data.market.MarketData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.List;

/**
 * The HistoryManager class follows the Singleton design pattern to manage the history of MarketData searches.
 * It keeps track of the most recent searches up to a specified limit, ensuring access to recently viewed data.
 */
public class HistoryManager {
    //Singleton instance
    private static HistoryManager instance;
    private LinkedList<MarketData> history;
    private static final Logger logger = LogManager.getLogger(HistoryManager.class);

    /**
     * Private constructor to prevent instantiation from outside the class.
     * Initializes the history LinkedList
     */
    private HistoryManager() {
        history = new LinkedList<>();
    }

    /**
     * Provides global access to the single instance of HistoryManager.
     * If the instance does not exist, it creates a new one; otherwise, it returns the existing instance.
     *
     * @return The single instance of HistoryManager.
     */
    public static HistoryManager getInstance() {
        if (instance == null) {
            instance = new HistoryManager();
        }
        return instance;
    }

    /**
     * Adds a MarketData search to the history.
     * If the search already exists in the history, it is moved to the front of the list.
     * The history is maintained up to a maximum of 4 items, removing the oldest if exceeded.
     *
     * @param data The MarketData object to be added to the history.
     */
    public void addSearch(MarketData data) {
        history.remove(data);                   // preventing duplicates
        history.addFirst(data);

        logger.info("History added for market: " + data.getName());

        while (history.size() > 4) {
            history.removeLast();
        }
    }

    public List<MarketData> getHistory() {
        return new LinkedList<>(history);
    }
}

