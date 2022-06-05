package com.numismatic_app.server.data_storage;

import com.numismatic_app.server.file_worker.PropertyConnection;
import com.numismatic_app.server.dto.CollectionDTO;
import com.numismatic_app.server.exception.DataStorageException;
import com.numismatic_app.server.file_worker.SaverInfo;

import java.io.*;
import java.util.concurrent.Semaphore;

public class CollectionStorage {

    private CollectionStorage(){}

    private static String pathToUserDataProp =  new File("").getAbsolutePath()+"/src/main/resources/userData.properties";

    private static Semaphore semaphore = new Semaphore(1);

    public static void saveData(CollectionDTO collectionDTO, String place) throws DataStorageException {
        try {
            semaphore.acquire();

            // сохраняем данные DTO в файл
            saveUserCollectionInFile(collectionDTO, place);

        }  catch (IOException e) {

           Thread.currentThread().interrupt();
           throw new DataStorageException("error");

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            semaphore.release();
        }
    }

    private static void saveUserCollectionInFile(CollectionDTO collectionDTO, String place) throws IOException {

        PropertyConnection prop= new PropertyConnection(pathToUserDataProp);
        File file = new File(new File("").getAbsolutePath()+prop.open().getProperty("collectionPath")+place+".pdto");
        SaverInfo saverInfo =new SaverInfo(file.getAbsolutePath());
        saverInfo.save(collectionDTO);
        saverInfo.close();





    }
    public static CollectionDTO getUserCollectionFromFile(String place) throws IOException, DataStorageException {


        PropertyConnection prop=new PropertyConnection(pathToUserDataProp);
        String s =new File("").getAbsolutePath()+prop.open().getProperty("collectionPath")+place+".pdto";
        System.out.println(s);
        File file = new File(s);
        System.out.println(file.isFile());
        prop.close();


        try( FileInputStream fileInputStream= new FileInputStream(file);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
             )
        {
            CollectionDTO collectionDTO =    (CollectionDTO)  objectInputStream.readObject();

            return collectionDTO;

        } catch (FileNotFoundException e){

            throw new DataStorageException("Collection not found in server data storage");
        }
        catch(IOException e ){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


        return null;


    }


}

