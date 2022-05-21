package com.NumismaticApp.Server.NumismaticApp.BusinessComponents;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertyConnection {

    Properties property;
    FileInputStream fileInputStream;

    public PropertyConnection(String pathToProperty) throws IOException {


        property = new Properties();
        fileInputStream = new FileInputStream(pathToProperty);
        property.load(fileInputStream);

    }

    public Properties open() throws IOException {

        return property;

    }

    public void close() throws IOException {

        fileInputStream.close();
        fileInputStream=null;

    }


}
