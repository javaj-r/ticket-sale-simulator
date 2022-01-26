package com.javid.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author javid
 * Created on 1/2/2022
 */
public class Property {

    private static final String DEFAULT_PROPERTIES = "application.properties";

    private Property() {
        throw new IllegalStateException("Utility class");
    }

    public static Properties getProperties() {
        return getProperties(DEFAULT_PROPERTIES);
    }

    public static Properties getProperties(String filename) {
        try (InputStream input = Property.class.getClassLoader().getResourceAsStream(filename)) {

            Properties properties = new Properties();

            if (input == null) {
                return null;
            }

            properties.load(input);
            return properties;

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
