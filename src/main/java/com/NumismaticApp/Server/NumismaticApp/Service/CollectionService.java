package com.NumismaticApp.Server.NumismaticApp.Service;

import com.NumismaticApp.Server.NumismaticApp.DTO.CollectionDTO;
import com.NumismaticApp.Server.NumismaticApp.DataStorage.CollectionStorage;
import com.NumismaticApp.Server.NumismaticApp.Entity.CollectionEntity;
import com.NumismaticApp.Server.NumismaticApp.Entity.UserEntity;
import com.NumismaticApp.Server.NumismaticApp.Exception.CollectionNotFoundException;
import com.NumismaticApp.Server.NumismaticApp.Exception.DataStorageException;
import com.NumismaticApp.Server.NumismaticApp.Model.CollectionModel;
import com.NumismaticApp.Server.NumismaticApp.repository.CollectionRepo;
import com.NumismaticApp.Server.NumismaticApp.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;

@Service
public class CollectionService {

    @Autowired
    CollectionRepo collectionRepo;
    @Autowired
    UserRepo userRepo;




    public CollectionModel createCollection(CollectionDTO collection,UserEntity user){




        CollectionEntity collectionEntity = new CollectionEntity();

        collectionEntity.setCollectionname(collection.getNameCollection());

        String hashPlace=String.valueOf(
                Base64.getEncoder()
                        .encode(
                                (user.getUsername()+collection.getNameCollection())
                                        .getBytes(StandardCharsets.UTF_8)
                        )
        );
        collectionEntity.setUser(user);
        collectionEntity.setPlacehash(hashPlace);

        CollectionStorage.saveData(collection,hashPlace);

        return  CollectionModel.toModel(collectionRepo.save(collectionEntity));


    }

    public ArrayList<CollectionDTO> getCollections(UserEntity user) throws CollectionNotFoundException, DataStorageException {

         ArrayList<CollectionEntity> collectionEntities= user.getCollection();

        if(collectionEntities.isEmpty()){

            throw  new CollectionNotFoundException("У пользователя отсутсвуют коллекции");

        }




        try {

            return getAllUserCollectonDTOs(collectionEntities);

        } catch (DataStorageException e) {

            throw new DataStorageException(e.getMessage());
        }


    }


    private ArrayList<CollectionDTO>  getAllUserCollectonDTOs(ArrayList<CollectionEntity> collectionEntities) throws DataStorageException{

        ArrayList<CollectionDTO> collectionDTOS = new ArrayList<>();


        try{
            for (CollectionEntity encol : collectionEntities) {

                collectionDTOS.add(CollectionStorage.getUserCollectionFromFile(encol.getPlacehash()));
            }

        } catch (DataStorageException e){

            throw new DataStorageException(e.getMessage());


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return  collectionDTOS;
    }


}
