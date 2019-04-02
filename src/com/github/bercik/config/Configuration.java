package com.github.bercik.config;

import java.text.SimpleDateFormat;

public class Configuration {
    private static SimpleDateFormat dateFormatter;

    public static SimpleDateFormat getDateFormatter() {
        return dateFormatter;
    }

    public static void setDateFormatter(SimpleDateFormat dateFormatter) {
        Configuration.dateFormatter = dateFormatter;
    }
}
