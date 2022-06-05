package com.numismatic_app.server.file_worker;

import lombok.extern.log4j.Log4j2;

import java.io.*;
@Log4j2
public class SaverInfo {

    private File file;
    private FileOutputStream fileOutputStream;
    private ObjectOutputStream objectOutputStream;


    public SaverInfo(String filePath)  {

        try{
        file =new File(filePath);
        fileOutputStream=new FileOutputStream(file);
        objectOutputStream = new ObjectOutputStream(fileOutputStream);
        log.info("preparing for preservation to "+file.getPath());

        }

        catch (IOException e){

            e.printStackTrace();

        }


    }

    public  void save(Object object)  {

        try {

            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            log.info("file was saved:"+file.getPath());
        }
        catch (IOException e){

            e.printStackTrace();

        }
    }
    public  boolean isEmpty()  {

        if(file.length()!=0){
            return false;
        }
        return true;

    }


    public  void close()  {
        try {
            objectOutputStream.close();
            fileOutputStream.close();
        }
        catch (IOException e){e.printStackTrace();}
    }






}
