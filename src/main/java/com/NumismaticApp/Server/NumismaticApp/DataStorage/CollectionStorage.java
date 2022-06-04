package com.NumismaticApp.Server.NumismaticApp.DataStorage;

import com.NumismaticApp.Server.NumismaticApp.BusinessComponents.PropertyConnection;
import com.NumismaticApp.Server.NumismaticApp.DTO.CollectionDTO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class CollectionStorage {

    private CollectionStorage(){}

    private static String pathToUserDataProp =  new File("").getAbsolutePath()+"/src/main/resources/userData.properties";

    private static Semaphore semaphore = new Semaphore(1);

    public static void saveData(CollectionDTO collectionDTO, String place){
        try {
            semaphore.acquire();

            // сохраняем данные DTO в файл
            savePlayerDTOInFile(collectionDTO, place);

        } catch (InterruptedException e){
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }

    private static void savePlayerDTOInFile(CollectionDTO collectionDTO, String place){


        try {
            PropertyConnection prop = new PropertyConnection(pathToUserDataProp);

             File file = new File(new File("").getAbsolutePath()+prop.open().getProperty("collectionPath")+place+".pdto");
             FileOutputStream fileOutputStream = new FileOutputStream(file);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
             objectOutputStream.writeObject(collectionDTO);

            objectOutputStream.close();
            fileOutputStream.close();

        } catch(IOException e ){
            e.printStackTrace();
        }
    }

}

