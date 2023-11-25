package com.mtattab.c2cServer.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Cleanup;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;


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

    public static String joinListWithDelimiter(List<String> list, String delimiter) {
        return String.join(delimiter, list);
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

    public static <T> T jsonToObject(String json, Class<T> valueType) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, valueType);
        } catch (JsonProcessingException e) {
//            e.printStackTrace(); // Handle the exception appropriately
            log.error("json {} was not converted to {}",json,valueType);
            return null;
        }
    }

    public File convertMultiPartToFile(MultipartFile file) throws IOException {
        String tempDir = System.getProperty("java.io.tmpdir");
//        System.out.println(tempDir);
        File convFile = new File(tempDir ,Objects.requireNonNull(file.getOriginalFilename()));

        @Cleanup
        FileOutputStream fos = new FileOutputStream(convFile);

        fos.write(file.getBytes());
        return convFile;
    }


    public static <T> void removeByValue(HashMap<T, T> hashMap, T value ){
//        the below will remove the item from the list based on its value
        hashMap.remove(value);
        ArrayList<T> keysToRemove = new ArrayList<>();

        // First, identify keys to remove
        hashMap.keySet().parallelStream().forEach(key -> {
            if (hashMap.get(key) != null && hashMap.get(key).equals(value)) {
                keysToRemove.add(key);
                log.info("found an active connection with socket {}",hashMap.get(key));
            }
        });

        // Then, remove the identified keys
        keysToRemove.forEach(hashMap::remove);

    }
    public static <K, V> void removeByValue(Map<K, V> map, V valueToRemove) {
        Iterator<Map.Entry<K, V>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<K, V> entry = iterator.next();
            if (valueToRemove == null ? entry.getValue() == null : valueToRemove.equals(entry.getValue())) {
                iterator.remove();
            }
        }
    }

    public static String getFileNameFromPath(String fullPath) {
        String[] parts = fullPath.split("/");
        if (parts.length > 0) {
            return parts[parts.length - 1];
        }
        return fullPath; // Return the original string if no '/' is found
    }
    public static boolean endsWithSlash(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        return input.endsWith("/");
    }

}
