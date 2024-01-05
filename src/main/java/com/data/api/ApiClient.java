package com.data.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * The ApiClient class provides methods to fetch data from a given URL.
 * It supports caching of the fetched data and can update the cache if required.
 */
public class ApiClient {
    private static final Logger logger = LogManager.getLogger(ApiClient.class);

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

                int responseCode = conn.getResponseCode();

                logger.debug("Response code: " + responseCode);

                if (responseCode == 200) {
                    logger.info("Connection established");

                    BufferedWriter writer = getWriter(conn, cacheFile);
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
     * Creates a BufferedWriter to write response from the HttpURLConnection to a cache file
     *
     * @param conn          The HttpURLConnection object.
     * @param cacheFile     The file to which the response is to be written.
     * @return              A BufferedWriter to write the response.
     * @throws IOException  If an I/O error occurs.
     */
    private static BufferedWriter getWriter(HttpURLConnection conn, File cacheFile) throws IOException {
        // read
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        // write
        BufferedWriter writer = new BufferedWriter(new FileWriter(cacheFile));
        writer.write(response.toString());
        return writer;
    }
}

