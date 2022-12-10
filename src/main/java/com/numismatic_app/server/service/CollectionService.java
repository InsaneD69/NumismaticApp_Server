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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


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
     * @param collection Коллекция дял сохраенеи
     * @throws DataStorageException Выбрасывается при возникновении ошибки
     * при сохранении коллекции пользователя в хранилище сервера
     */
    public void postCollectionToAcc(CollectionDTO collection, String  username) throws DataStorageException, CollectionNameAlreadyIsExist {

        String hashPlace="";

        UserEntity user = userRepo.findByUsername(username);

              for( CollectionEntity col: user.getCollectionEntities()){

                  if(col.getCollectionname().equals(collection.getNameCollection())){

                      throw new CollectionNameAlreadyIsExist("коллекция с таким именем уже существует");
                  }

             }


            CollectionEntity collectionEntity = new CollectionEntity();
            collectionEntity.setCollectionname(collection.getNameCollection());
            collectionEntity.setUser(user);
            collectionEntity.setPlacehash(getHashPlace(
                     user.getUsername()
                    ,collection.getNameCollection()
                    ));
            hashPlace =collectionEntity.getPlacehash();

            collectionRepo.save(collectionEntity);


        CollectionStorage.saveData(collection,hashPlace);


    };

    public void updateCollectionToAcc(CollectionDTO collection, String  username) throws DataStorageException, CollectionNameAlreadyIsExist {

        String hashPlace="";

        UserEntity user = userRepo.findByUsername(username);

        CollectionEntity collectionEntity = collectionRepo.findByCollectionnameAndUser_id(collection.getNameCollection(),user.getId());

        collectionEntity.setCollectionname(collection.getNameCollection());
        hashPlace =getHashPlace(user.getUsername(),collection.getNameCollection());

        collectionEntity.setPlacehash(hashPlace);

        CollectionStorage.saveData(collection,hashPlace);

        collectionRepo.save(collectionEntity);
    }

    /**  проверяет, есть ли у пользователя сохраненные коллекции в базе данных и
     * вызывает метод получения всех коллекций
     * @return Возвращает список из всех коллекций пользователя {@link CollectionDTO}
     * @throws DataStorageException Выбрасывается при возникновении ошибки
     *      * при сохранении коллекции пользователя в хранилище сервера
     * @throws CollectionNotFoundException Выбрасывается при отсутствии коллекции
     * в хранилище сервера
     */
    public List<CollectionDTO> getCollections(String username) throws DataStorageException, CollectionNotFoundException {

        UserEntity user = userRepo.findByUsername(username);
        if (user==null) {
            throw new UsernameNotFoundException("Пользователь не найден");
        }

         List<CollectionEntity> collectionEntities= user.getCollectionEntities();

        if(collectionEntities.isEmpty()){

            throw  new CollectionNotFoundException("У пользователя отсутсвуют коллекции");

        }

        log.info("User "+user.getUsername()+" have collections:");
        collectionEntities.forEach(col->

            log.info("    "+col.getCollectionname())

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


    /** Формирует строку-адрес из имени пользователя и имени коллекции
     * @param userName
     * @param collectionName
     * @return строку-адрес
     */
    private String getHashPlace(String userName, String collectionName){
         return HashToString.convert(
                Base64.getEncoder().encode(
                        (userName+collectionName)
                                .getBytes(StandardCharsets.UTF_8)
                )
        );

    }
}
