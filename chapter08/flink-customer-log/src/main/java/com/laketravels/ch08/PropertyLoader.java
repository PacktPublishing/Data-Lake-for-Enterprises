package com.laketravels.ch08;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyLoader {

    public static Properties loadProperty(String propertyFileName) throws IOException {
        String basePath = System.getProperty("configPath");
        if ((basePath == null) || (basePath.trim().length()==0)) {
            basePath = System.getProperty("user.dir") + File.separator  + "config";
        }
        Properties props = new Properties();
        props.load(new FileInputStream(basePath +
                        File.separator +
                        propertyFileName));
        return props;
    }
}
