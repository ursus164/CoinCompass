package com.data.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


public class ApiClient {
    private static final Logger logger = LogManager.getLogger(ApiClient.class);
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

    private static BufferedWriter getWriter(HttpURLConnection conn, File cacheFile) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        BufferedWriter writer = new BufferedWriter(new FileWriter(cacheFile));
        writer.write(response.toString());
        return writer;
    }
}

