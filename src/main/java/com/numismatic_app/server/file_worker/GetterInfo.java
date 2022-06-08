package com.numismatic_app.server.file_worker;

import lombok.extern.log4j.Log4j2;

import java.io.*;
@Log4j2
public class GetterInfo {
    private File file;
    private FileInputStream fileInputStream;
    private ObjectInputStream objectInputStream;


    public GetterInfo(String filePath) throws IOException {

        file =new File( filePath);
        fileInputStream =new FileInputStream(file);
        objectInputStream = new ObjectInputStream(fileInputStream);

        log.info("open file: "+filePath);

    }



    public  Object get() throws IOException, ClassNotFoundException {

       return objectInputStream.readObject();


    }

    public  void close() throws IOException {

        objectInputStream.close();
        fileInputStream.close();

    }

}
