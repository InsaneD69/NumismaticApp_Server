package com.NumismaticApp.Server.NumismaticApp.Service;

import com.NumismaticApp.Server.NumismaticApp.Entity.CollectionEntity;
import com.NumismaticApp.Server.NumismaticApp.Entity.UserEntity;
import com.NumismaticApp.Server.NumismaticApp.Exception.CollectionNotFoundException;
import com.NumismaticApp.Server.NumismaticApp.Model.CollectionModel;
import com.NumismaticApp.Server.NumismaticApp.repository.CollectionRepo;
import com.NumismaticApp.Server.NumismaticApp.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CollectionService {

    @Autowired
    CollectionRepo collectionRepo;
    @Autowired
    UserRepo userRepo;

    public CollectionModel createCollection(CollectionEntity collection, String username ){

        UserEntity user = userRepo.findByUsername(username);

        collection.setUser(user);

        return  CollectionModel.toModel(collectionRepo.save(collection));


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
