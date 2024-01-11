package com.data.api;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

/**
 * The ApiClient class provides methods to fetch data from a given URL.
 * It supports caching of the fetched data and can update the cache if required.
 */
public class  ApiClient {
    private static final Logger logger = LogManager.getLogger(ApiClient.class);
    private static int responseCode;

    /**
     * Fetches data from the specified URL and caches it.
     * If a cache file already exists and updating is not required, it uses data from cache.
     * Otherwise, it fetches new data from the URL and updates the cache.
     *
     * @param link      The URL to fetch data from.
     * @param cache     The file path for storing the cached data.
     * @param update    A boolean flag indicating whether to update the cache or not
     */
    public void fetchData(String link, String cache, Boolean update) {
        File cacheFile = new File(cache);
        try {
            if (cacheFile.exists() && (!update)) {
                logger.info("Cache file exists: " + cache);
            } else {
                logger.info("Connecting to server by request: " + link);

                URL url = new URL(link);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

               responseCode = conn.getResponseCode();

                logger.debug("Response code: " + responseCode);
                if (responseCode == 429) {
                    showAlertRetry(link,cache,update);

                } else if (responseCode == 200) {
                    logger.info("Connection established");

                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    BufferedWriter writer = new BufferedWriter(new FileWriter(cacheFile));
                    writer.write(response.toString());
                    writer.close();


                    logger.info("Data fetched and saved to cache: " + cache);
                } else {
                    logger.error("GET request not worked. Response code: " + responseCode + ". Try again later...");
                }
            }

        } catch (IOException e) {
            logger.error("Error occured during fetching data: ", e);
            System.exit(0);
        }
    }

    /**
     * Shows alert when API connection limit has been exceeded.
     * User has option to either retry fetching data or close application
     *
     * @param link  URL link to fetch data
     * @param cache Cache file path
     * @param update    Flag indicating whether to fetch new data or not
     */
    private void showAlertRetry(String link, String cache, Boolean update) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd");
        alert.setHeaderText("Przekroczono limit połączeń API");
        alert.setContentText("Czy chcesz spróbować ponownie?");

        ButtonType retryButton = new ButtonType("Spróbuj ponownie");
        ButtonType closeButton = new ButtonType("Zamknij aplikację");

        alert.getButtonTypes().setAll(retryButton,closeButton);

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == retryButton) {
            fetchData(link,cache,update);
        } else {
            System.exit(0);
        }
    }
}

