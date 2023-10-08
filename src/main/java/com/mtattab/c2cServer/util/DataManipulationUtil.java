package com.mtattab.c2cServer.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@UtilityClass
@Slf4j
public class DataManipulationUtil {

    public static String getWordByIndex(String input, int index) {
        // Define a regex pattern to match the first word
        List<String> listOfWords= stringToList(input, " ");
        try {
            return listOfWords.get(index);

        }catch (Exception e){
            log.warn("[-] There is no word at index '{}' in input '{}'", index,input);
            return input;
        }
    }

    public static List<String> stringToList(String inputString, String delimiter) {
        try {
            // Split the input string by space and convert it to a list
            return Arrays.asList(inputString.split(delimiter));

        }catch (Exception e){
            log.warn("[-] Input '{}' does not contain the delimiter '{}' returning input as a list",inputString,delimiter);
            return List.of(inputString);
        }
    }

    public static <T> String convertObjectToJson(T obj){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
