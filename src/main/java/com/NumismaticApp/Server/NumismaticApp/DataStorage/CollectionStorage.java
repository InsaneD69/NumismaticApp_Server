package com.NumismaticApp.Server.NumismaticApp.DataStorage;

import com.NumismaticApp.Server.NumismaticApp.DTO.CollectionDTO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class CollectionStorage {

    private CollectionStorage(){}


    private static Semaphore semaphore = new Semaphore(1);

    public static void saveData(CollectionDTO collectionDTODTO){
        try {
            semaphore.acquire();

            // сохраняем данные DTO в файл
            savePlayerDTOInFile(collectionDTODTO);

        } catch (InterruptedException e){
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }

    private static void savePlayerDTOInFile(CollectionDTO collectionDTO){

/*
        byte[] hashName = HashGen.getHash(playerDTO.getPlayerName());

        try {

             File file = new File("C:\\Users\\dimas\\Downloads\\LitteraWorlds-dev (1)\\LitteraWorlds-dev\\DTO_files\\"+HashToString.convert(hashName)+ ".pdto");
             FileOutputStream fileOutputStream = new FileOutputStream(file);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
             objectOutputStream.writeObject(playerDTO);

            objectOutputStream.close();
            fileOutputStream.close();

        } catch(IOException e ){
            e.printStackTrace();
        }*/
    }

}

