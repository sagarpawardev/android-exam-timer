package dev.sagar.examtimer.utils;

import static dev.sagar.examtimer.Constants.PROP_MOCK_SERVICES;

import android.app.Activity;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private Properties properties;
    private static ConfigReader instance = null;

    private ConfigReader(Properties properties) {
        this.properties = properties;
    }

    public static ConfigReader getInstance(Activity activity) {
        if (instance == null) {
            Properties properties = loadProperties(activity);
            instance = new ConfigReader(properties);
        }

        return instance;
    }

    public boolean getBoolean(String key, boolean defaultVal){
        String val = properties.getProperty(key);
        if(StringUtils.isBlank(val)){
            return defaultVal;
        }
        return Boolean.parseBoolean(val);
    }

    public int getInt(String key, int defaultVal){
        String val = properties.getProperty(key);
        if(StringUtils.isBlank(val)){
            return defaultVal;
        }
        return Integer.parseInt(val);
    }

    private static Properties loadProperties(Activity activity) {
        Properties properties = new Properties();
        try {
            InputStream inputStream = activity.getBaseContext().getAssets().open("config.properties");
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

}
