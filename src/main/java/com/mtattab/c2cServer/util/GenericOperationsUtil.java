package com.mtattab.c2cServer.util;

import jakarta.persistence.EntityManager;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
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
}
