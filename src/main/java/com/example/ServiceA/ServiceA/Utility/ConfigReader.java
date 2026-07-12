package com.example.ServiceA.ServiceA.Utility;


import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;


public final class ConfigReader {

    private static final String PROPERTIES_FOLDER = "OAuth2_properties/";
    private static final Map<String, Properties> CACHE = new ConcurrentHashMap<>();

    private ConfigReader() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String getConfigValue(String fileName, String key) {

        Properties properties = CACHE.computeIfAbsent(
                fileName,
                ConfigReader::loadProperties
        );

        String value = properties.getProperty(key);

        if (value == null) {
            throw new RuntimeException(
                    "Key '" + key + "' not found in file '" + fileName + "'"
            );
        }

        return value;
    }

    private static Properties loadProperties(String fileName) {

        Properties properties = new Properties();

        String path = PROPERTIES_FOLDER + fileName;

        try (InputStream input = ConfigReader.class
                .getClassLoader()
                .getResourceAsStream(path)) {

            if (input == null) {
                throw new RuntimeException(
                        "Properties file not found: " + path
                );
            }

            properties.load(input);

            return properties;

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to load properties file: " + path,
                    e
            );
        }
    }
}