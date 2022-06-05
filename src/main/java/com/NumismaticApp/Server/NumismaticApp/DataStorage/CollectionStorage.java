package com.NumismaticApp.Server.NumismaticApp.DataStorage;

import com.NumismaticApp.Server.NumismaticApp.BusinessComponents.PropertyConnection;
import com.NumismaticApp.Server.NumismaticApp.DTO.CollectionDTO;
import com.NumismaticApp.Server.NumismaticApp.Exception.DataStorageException;

import java.io.*;
import java.util.concurrent.Semaphore;

public class CollectionStorage {

    private CollectionStorage(){}

    private static String pathToUserDataProp =  new File("").getAbsolutePath()+"/src/main/resources/userData.properties";

    private static Semaphore semaphore = new Semaphore(1);

    public static void saveData(CollectionDTO collectionDTO, String place){
        try {
            semaphore.acquire();

            // сохраняем данные DTO в файл
            saveUserCollectionInFile(collectionDTO, place);

        } catch (InterruptedException e){
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            semaphore.release();
        }
    }

    private static void saveUserCollectionInFile(CollectionDTO collectionDTO, String place) throws IOException {

        PropertyConnection prop=null;
        FileOutputStream fileOutputStream=null;
        ObjectOutputStream objectOutputStream=null;

        try {
             prop = new PropertyConnection(pathToUserDataProp);

             File file = new File(new File("").getAbsolutePath()+prop.open().getProperty("collectionPath")+place+".pdto");

             fileOutputStream = new FileOutputStream(file);
             objectOutputStream = new ObjectOutputStream(fileOutputStream);
             objectOutputStream.writeObject(collectionDTO);


        } catch(IOException e ){
            e.printStackTrace();
        }
        finally {
            prop.close();
            objectOutputStream.close();
            fileOutputStream.close();
        }

    }
    public static CollectionDTO getUserCollectionFromFile(String place) throws IOException, DataStorageException {

        System.out.println(place);
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
            System.out.println("ok");

            CollectionDTO collectionDTO =    (CollectionDTO)  objectInputStream.readObject();
            System.out.println(collectionDTO.getNameCollection());
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

