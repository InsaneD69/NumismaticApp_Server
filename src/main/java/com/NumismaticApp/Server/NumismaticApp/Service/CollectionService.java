package com.NumismaticApp.Server.NumismaticApp.Service;

import com.NumismaticApp.Server.NumismaticApp.Entity.CollectionEntity;
import com.NumismaticApp.Server.NumismaticApp.Entity.UserEntity;
import com.NumismaticApp.Server.NumismaticApp.Exception.CollectionNotFoundException;
import com.NumismaticApp.Server.NumismaticApp.repository.Model.Collection;
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

    public Collection createCollection(CollectionEntity collection, String username ){

        UserEntity user = userRepo.findByUsername(username);

        collection.setUser(user);

        return  Collection.toModel(collectionRepo.save(collection));


    }

    public  Collection  getCollection(String username, String collectionName) throws CollectionNotFoundException {

        CollectionEntity collection = userRepo.findByUsername(username).getCollectionByCollectionName(collectionName);

        if(collection==null){
            throw  new CollectionNotFoundException("Коллекции с таким именем не существует");

        }

        //поиск коллекции в хранилище

        //пока затычка
        return Collection.toModel(collection);

    }




}
