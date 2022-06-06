package com.numismatic_app.server.service;

import com.numismatic_app.server.dto.CollectionDTO;
import com.numismatic_app.server.data_storage.CollectionStorage;
import com.numismatic_app.server.entity.CollectionEntity;
import com.numismatic_app.server.entity.UserEntity;
import com.numismatic_app.server.exception.CollectionNotFoundException;
import com.numismatic_app.server.exception.DataStorageException;

import com.numismatic_app.server.repository.CollectionRepo;
import com.numismatic_app.server.repository.UserRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


@Service
@Log4j2
public class CollectionService {

    @Autowired
    CollectionRepo collectionRepo;
    @Autowired
    UserRepo userRepo;




    public void createCollection(CollectionDTO collection, UserEntity user) throws DataStorageException {




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

        try {
            CollectionStorage.saveData(collection,hashPlace);
        } catch (DataStorageException e) {

            throw new DataStorageException(e.getMessage());
        }

        boolean have = false;

      for( CollectionEntity col: user.getCollection()){
          if(col.getCollectionname().equals(collection.getNameCollection())){

              have=true;
          }

      }


        if(!have) {collectionRepo.save(collectionEntity);}


    }

    public List<CollectionDTO> getCollections(UserEntity user) throws DataStorageException, CollectionNotFoundException {

         List<CollectionEntity> collectionEntities= user.getCollection();

        if(collectionEntities.isEmpty()){

            throw  new CollectionNotFoundException("У пользователя отсутсвуют коллекции");

        }

        log.info("User "+user.getUsername()+" have collections:");
        collectionEntities.forEach(col->

            log.info(col.getCollectionname())

        );


        try {

            return getAllUserCollectonDTOs(new ArrayList<>(collectionEntities));

        } catch (DataStorageException | IOException e) {

            throw new DataStorageException(e.getMessage());
        }


    }


    private ArrayList<CollectionDTO>  getAllUserCollectonDTOs(ArrayList<CollectionEntity> collectionEntities) throws DataStorageException, IOException {

        ArrayList<CollectionDTO> collectionDTOS = new ArrayList<>();


        try{
            for (CollectionEntity encol : collectionEntities) {

                collectionDTOS.add(CollectionStorage.getUserCollectionFromFile(encol.getPlacehash()));
            }

        } catch (DataStorageException e){

            log.error(e.getMessage());

            throw new DataStorageException(e.getMessage());


        } catch (ClassNotFoundException e) {
            log.error(e.getMessage());
            throw new DataStorageException("Data storage has been broken");
        }
        log.info("have collections on server data storage: "+collectionDTOS.size());
        return  collectionDTOS;
    }


}
