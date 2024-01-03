package com.data.api;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONPropertyName;

/**
 * The ApiClient class is designed to fetch data from an external API.
 * It provides functionality to retrieve data and save it to a cache file, with an option to update it.
 * In case of failure to connect, it retries the connection a specified number of times.
 */
public class ApiClient {
    private static final Logger logger = LogManager.getLogger(ApiClient.class);

    private static final int MAX_ATTEMPTS = 3;


    /**
     * Fetches data from the specified URL and saves it to a cache file.
     * If the cache file already exists, data is fetched only if the update flag is set.
     * In case of failure to fetch data, the method retries up to a maximum of MAX_ATTEMPTS.
     *

     * @param link   The URL from which data is to be fetched.
     * @param cache  The path to the cache file where the data is to be saved.
     * @param update A flag indicating whether to update the data (true) or not (false).
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

                int responseCode = conn.getResponseCode();
                logger.debug("Response code: " + responseCode);

                if (responseCode == 200) {
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
            logger.fatal("Maximum attempts reached. Failed to connect.");
            System.exit(0);
        }
    }
}

