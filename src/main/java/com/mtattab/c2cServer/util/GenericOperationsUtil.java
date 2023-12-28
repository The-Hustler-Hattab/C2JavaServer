package com.mtattab.c2cServer.util;

import io.github.pixee.security.BoundedLineReader;
import jakarta.persistence.EntityManager;
import lombok.Cleanup;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
@Slf4j
public class GenericOperationsUtil {

    public static String getDateTime() {
        // Get the current date and time
        LocalDateTime now = LocalDateTime.now();

        // Define a custom date and time format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm");

        // Format the current date and time using the defined format
        return now.format(formatter);
    }


    public void closeEntityManager(EntityManager entityManager){
        if (entityManager.isOpen()){
            entityManager.close();
        }
    }

    public static String sendGetRequest(String url) {
        HttpURLConnection connection = null;
        StringBuilder response = new StringBuilder();

        try {
            // Create a URL object
            URL obj = new URL(url);

            // Open a connection to the URL
            connection = (HttpURLConnection) obj.openConnection();
            // Set the request method to GET
            connection.setRequestMethod("GET");

            // Get the response code
            int responseCode = connection.getResponseCode();
            log.info("Response Code: " + responseCode);

            // Read the response from the input stream
            @Cleanup
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String inputLine;

            while ((inputLine = BoundedLineReader.readLine(in, 5_000_000)) != null) {
                response.append(inputLine);
            }

            // Return the response
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            if (connection!=null){
                connection.disconnect();
            }

        }
        return response.toString();

    }

}
