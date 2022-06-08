package com.numismatic_app.server.service;

import com.numismatic_app.server.dto.CollectionDTO;
import com.numismatic_app.server.data_storage.CollectionStorage;
import com.numismatic_app.server.entity.CollectionEntity;
import com.numismatic_app.server.entity.UserEntity;
import com.numismatic_app.server.exception.CollectionNameAlreadyIsExist;
import com.numismatic_app.server.exception.CollectionNotFoundException;
import com.numismatic_app.server.exception.DataStorageException;

import com.numismatic_app.server.repository.CollectionRepo;
import com.numismatic_app.server.repository.UserRepo;
import com.numismatic_app.server.security.HashToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;


/**
 * Обрабатывает сохранение и получение коллекций пользователей,
 * осуществляет взаимодействие с бд и хранилищем сервера
 */
@Service
@Log4j2
public class CollectionService {

    @Autowired
    CollectionRepo collectionRepo;
    @Autowired
    UserRepo userRepo;


    /** Сохраняет коллекцию пользователя
     * @param collection Коллекция дял сохраенеия
     * @param user  Владелец коллекии
     * @throws DataStorageException Выбрасывается при возникновении ошибки
     * при сохранении коллекции пользователя в хранилище сервера
     */
    public void saveCollectionToAcc(CollectionDTO collection, UserEntity user, String newOrUpdate) throws DataStorageException, CollectionNameAlreadyIsExist {

        CollectionEntity collectionEntity=null;
        if(newOrUpdate.equals("new")){
              for( CollectionEntity col: user.getCollection()){
                  if(col.getCollectionname().equals(collection.getNameCollection())){

                      throw new CollectionNameAlreadyIsExist("коллекция с таким именем уже существует");
                  }

             }

            collectionEntity = new CollectionEntity();
            collectionEntity.setCollectionname(collection.getNameCollection());
            collectionEntity.setUser(user);
            collectionEntity.setPlacehash(getHashPlace(
                     user.getUsername()
                    ,collection.getNameCollection()
                    )
            );

            collectionRepo.save(collectionEntity);
        }





        try {
            CollectionStorage.saveData(collection,
                    Optional.ofNullable(Optional.ofNullable(collectionEntity).get().getPlacehash())
                            .orElse(getHashPlace(user.getUsername(),collection.getNameCollection())));
        } catch (DataStorageException e) {

            throw new DataStorageException(e.getMessage());
        }




    }

    /**  проверяет, есть ли у пользователя сохраненные коллекции в базе данных и
     * вызывает метод получения всех коллекций
     * @param user Сущность владельца коллекции
     * @return Возвращает список из всех коллекций пользователя {@link CollectionDTO}
     * @throws DataStorageException Выбрасывается при возникновении ошибки
     *      * при сохранении коллекции пользователя в хранилище сервера
     * @throws CollectionNotFoundException Выбрасывается при отсутствии коллекции
     * в хранилище сервера
     */
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

        } catch (DataStorageException  e) {

            throw new DataStorageException(e.getMessage());
        }


    }


    /** Извлекает из хранилища все коллекции пользователя
     * @param collectionEntities
     * @return Возвращает список из всех коллекций пользователя {@link CollectionDTO}
     * @throws DataStorageException Выбрасывается при возникновении ошибки
     *      *      * при сохранении коллекции пользователя в хранилище сервера
     */
    private ArrayList<CollectionDTO>  getAllUserCollectonDTOs(ArrayList<CollectionEntity> collectionEntities) throws DataStorageException {

        ArrayList<CollectionDTO> collectionDTOS = new ArrayList<>();

        try{
            for (CollectionEntity encol : collectionEntities) {

                collectionDTOS.add(CollectionStorage.getUserCollectionFromFile(encol.getPlacehash()));
            }
            log.info("have collections on server data storage: "+collectionDTOS.size());
            return  collectionDTOS;

        } catch (DataStorageException e){

            log.error(e.getMessage());
            throw new DataStorageException(e.getMessage());

        } catch (ClassNotFoundException | IOException e) {
            log.error(e.getMessage());
            throw new DataStorageException("Data storage has been broken");
        }

    }


    private String getHashPlace(String userName, String collectionName){
         return HashToString.convert(
                Base64.getEncoder().encode(
                        (userName+collectionName)
                                .getBytes(StandardCharsets.UTF_8)
                )
        );

    }
}
