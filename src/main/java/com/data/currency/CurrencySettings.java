package com.data.currency;

public class CurrencySettings {
    private static CurrencySettings instance;
    private String selectedCurrency = "usd"; // domyślna wartość

    private CurrencySettings() {}

    public static CurrencySettings getInstance() {
        if (instance == null) {
            instance = new CurrencySettings();
        }
        return instance;
    }

    public String getSelectedCurrency() {
        return selectedCurrency;
    }

    public void setSelectedCurrency(String selectedCurrency) {
        this.selectedCurrency = selectedCurrency;
    }
}
