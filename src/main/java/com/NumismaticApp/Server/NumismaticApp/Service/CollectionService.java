package com.NumismaticApp.Server.NumismaticApp.Service;

import com.NumismaticApp.Server.NumismaticApp.DTO.CollectionDTO;
import com.NumismaticApp.Server.NumismaticApp.DataStorage.CollectionStorage;
import com.NumismaticApp.Server.NumismaticApp.Entity.CollectionEntity;
import com.NumismaticApp.Server.NumismaticApp.Entity.UserEntity;
import com.NumismaticApp.Server.NumismaticApp.Exception.CollectionNotFoundException;
import com.NumismaticApp.Server.NumismaticApp.Model.CollectionModel;
import com.NumismaticApp.Server.NumismaticApp.repository.CollectionRepo;
import com.NumismaticApp.Server.NumismaticApp.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
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

    public CollectionModel getCollection(String username, String collectionName) throws CollectionNotFoundException {

        CollectionEntity collection = userRepo.findByUsername(username).getCollectionByCollectionName(collectionName);

        if(collection==null){
            throw  new CollectionNotFoundException("Коллекции с таким именем не существует");

        }

        //поиск коллекции в хранилище

        //пока затычка
        return CollectionModel.toModel(collection);

    }




}
