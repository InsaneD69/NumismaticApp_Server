package com.numismatic_app.server.file_worker;

import java.io.*;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class PropertyConnection {

    Properties property;
    InputStreamReader inputStreamReader;

    public PropertyConnection(String pathToProperty) throws IOException {
        property = new Properties();
        inputStreamReader = new InputStreamReader(new FileInputStream(pathToProperty), StandardCharsets.UTF_8);
        property.load(inputStreamReader);
    }

    public Properties open()  {
        return property;
    }

    public void close() throws IOException {
        inputStreamReader.close();
        inputStreamReader =null;

    }

}
