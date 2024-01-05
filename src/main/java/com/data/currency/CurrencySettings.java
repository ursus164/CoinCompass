package com.data.currency;

/**
 * The CurrencySettings class implements the Singleton design pattern to manage user's currency choice.
 * It allows setting and getting selected currency with a default value of "usd" and
 * storing previously chosen currencies.
 */
public class CurrencySettings {
    // Singleton instance
    private static CurrencySettings instance;
    private String selectedCurrency = "usd"; // default

    /**
     * Private constructor to prevent instantiation from outside the class
     */
    private CurrencySettings() {}

    /**
     * Provides global access to the single instance of CurrencySettings.
     * If the instance does not exist, it creates a new one. Otherwise, it returns the existing instance.
     *
     * @return The single instance of CurrencySettings class.
     */
    public static CurrencySettings getInstance() {
        if (instance == null) {
            instance = new CurrencySettings();
        }
        return instance;
    }

    /**
     * Retrieves the currently selected currency.
     *
     * @return  The currently selected currency string.
     */
    public String getSelectedCurrency() {
        return selectedCurrency;
    }

    /**
     * Sets the currently selected currency to a new value.
     *
     * @param selectedCurrency The new currency string to be set as selected.
     */
    public void setSelectedCurrency(String selectedCurrency) {
        this.selectedCurrency = selectedCurrency;
    }
}
