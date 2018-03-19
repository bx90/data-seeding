package config;

import exception.ServerException;
import encryption.Encryption;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author bsun
 */
public class Config implements Configurable {
    Map<String, String> map = new HashMap<>();
    File propertyFile;

    public Config (String absolutePath) {
        try {
            propertyFile = new File(absolutePath);
            configure();
        } catch (ServerException e) {
            e.printStackTrace();
        }
    }

    public String get(String property) {
        return map.get(property);
    }

    public Boolean getBoolean(String property) {
        return map.get(property) == null ? null : Boolean.valueOf(map.get(property));
    }

    public Integer getInteger(String property) {
        return map.get(property) == null ? null : Integer.valueOf(map.get(property));
    }

    public String getDecryption(String property) {
        return map.get(property) == null ? null : Encryption.decrypt(Encryption.StringtoByte(map.get(property)));
    }

    @Override
    public void configure() throws ServerException {
        Properties properties = new Properties();
        if( propertyFile != null ) {
            configure(properties, propertyFile);
        }
        // java.util.Map has better interface than java.util.Properties
        map = populateMap(properties);
    }

    private void configure(Properties props, File file) throws ServerException {
        try (InputStream inputStream = new FileInputStream(file)) {
            props.load(inputStream);
        } catch (IOException e) {
            throw (new ServerException("Error reading " + file.getPath(), e));
        }

    }

    private Map<String,String> populateMap(Properties properties) {
        Map<String, String> map = new LinkedHashMap<>(properties.size());
        Enumeration<?> enumeration = properties.propertyNames();

        while (enumeration.hasMoreElements()) {
            String key = (String) enumeration.nextElement();
            String value = properties.getProperty(key);
            map.put(key, value);
        }

        return map;
    }
}
