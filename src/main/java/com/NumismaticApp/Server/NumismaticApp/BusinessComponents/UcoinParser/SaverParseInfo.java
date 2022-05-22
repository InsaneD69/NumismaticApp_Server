package com.NumismaticApp.Server.NumismaticApp.BusinessComponents.UcoinParser;

import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Document;

import java.io.*;
@Log4j2
public class SaverParseInfo {

    private File file;
    private FileOutputStream fileOutputStream;
    private ObjectOutputStream objectOutputStream;


    public SaverParseInfo(String filePath)  {

        log.info("start");
        try{
        file =new File( new File("").getAbsolutePath()+filePath);
        fileOutputStream=new FileOutputStream(file);
        objectOutputStream = new ObjectOutputStream(fileOutputStream);
            log.info("stream created");
        }

        catch (IOException e){

            e.printStackTrace();

        }


    }



    public  void save(Object object)  {

        try {

            objectOutputStream.writeObject(String.valueOf(object));
            objectOutputStream.flush();
        }
        catch (IOException e){

            e.printStackTrace();

        }
    }
    public  boolean isEmpty()  {

        if(file.length()!=0){
            log.info("not null");
            return false;
        }
        return true;

    }


    public  void close() throws IOException {

        objectOutputStream.close();
       fileOutputStream.close();

    }






}
