package com.numismatic_app.server.file_worker;

import lombok.extern.log4j.Log4j2;

import java.io.*;
@Log4j2
public class SaverInfo {

    private File file;
    private FileOutputStream fileOutputStream;
    private ObjectOutputStream objectOutputStream;


    public SaverInfo(String filePath) throws IOException {


        file =new File(filePath);
        fileOutputStream=new FileOutputStream(file);
        objectOutputStream = new ObjectOutputStream(fileOutputStream);
        log.info("preparing for preservation to "+file.getPath());


    }

    public  void save(Object object) throws IOException {

            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            log.info("file was saved:"+file.getPath());

    }

    public  void close() throws IOException {
            objectOutputStream.close();
            fileOutputStream.close();
    }






}
