package com.numismatic_app.server.controller;


import com.numismatic_app.server.dto.CollectionDTO;
import com.numismatic_app.server.dto.TestDto;
import com.numismatic_app.server.exception.CollectionNameAlreadyIsExist;
import com.numismatic_app.server.exception.CollectionNotFoundException;
import com.numismatic_app.server.exception.DataStorageException;
import com.numismatic_app.server.security.AuthProviderImpl;
import com.numismatic_app.server.security.JWTAuthentication;
import com.numismatic_app.server.service.CollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

/**
 * Обслуживает запросы, связанные с коллекциями пользователей
 */
@RestController
@RequestMapping("/collection")
@RequiredArgsConstructor
public class CollectionController {


    private final CollectionService collectionService;

    private final AuthProviderImpl authProvider;



    /** Обрабатывает запросы клиентов по пути /collection/postcollection
     * , сохраняет коллекцию пользователя в его аккаунт
     * @param collectionDTO Колекция, которую нужно сохранить

     * @return Положительный или отрицательный ответ
     */
    @PostMapping("/postcollection")
   public ResponseEntity<String> saveUserCollection(@RequestBody CollectionDTO collectionDTO){

        try {
            collectionService.postCollectionToAcc(collectionDTO
                    , SecurityContextHolder
                            .getContext()
                            .getAuthentication()
                            .getPrincipal().toString());
           return ResponseEntity.ok().body("ok");
        } catch (DataStorageException e) {
            return  ResponseEntity.status(500).build();
        } catch (CollectionNameAlreadyIsExist e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PutMapping("/updatecollection")
    public ResponseEntity<String> updateUserCollection(@RequestParam String  nameCollection, @RequestBody CollectionDTO collectionDTO, @RequestParam(required = false) Optional<String>  newNameCollection){


        System.out.println("Имя изменяемой коллекции "+nameCollection);
        System.out.println("Новое имя: "+newNameCollection.orElse("/не_предоставлено."));
        if(collectionDTO.getCollection()!=null){
            System.out.println("Содержимое коллекции обновляется " );


        } else   System.out.println("Содержимое коллекции не обновляется " );


        String updatingToNameCollection=newNameCollection.orElse(null);


        if((collectionDTO.getCollection()==null)&&(updatingToNameCollection==null)){

            return  ResponseEntity.status(204).body("Отсутствуют данные для обновления");
        }

        try {
            collectionService.updateCollectionToAcc(nameCollection,collectionDTO
                    , SecurityContextHolder.getContext().getAuthentication().getName(),updatingToNameCollection);
            return ResponseEntity.ok().body("ok");
        } catch (DataStorageException e) {
            return  ResponseEntity.status(500).body("произошла ошибка");
        } catch (CollectionNameAlreadyIsExist e) {
            return ResponseEntity.status(405).body(e.getMessage());
        } catch (CollectionNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (IOException e) {
            return  ResponseEntity.status(500).body("произошла ошибка");
        } catch (ClassNotFoundException e) {
            return  ResponseEntity.status(500).body("произошла ошибка");
        }


    }

    /** ИСПОЛЬЗУЕТСЯ ТОЛЬКО ДЛЯ ПРИЛОЖЕНИЯ
     * Обрабатывает запросы клиентов по пути /collection/get на получение его коллеций на аккаунте
     * @return Возвращает список из коллекций {@link CollectionDTO}
     */
  /* @GetMapping("/get")
   public ResponseEntity<Object> getCollection() {

        try {
            return ResponseEntity.ok(
                    collectionService
                            .getCollections(
                                    ((UserEntity) SecurityContextHolder
                                            .getContext()
                                            .getAuthentication()
                                            .getPrincipal()
                                    )

                            )
            );

        } catch (CollectionNotFoundException e) {

            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (DataStorageException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).build();

        }

   }*/

    @GetMapping("/getAll")
    public ResponseEntity<Object> getAllCollection() {
        System.out.println("Try to get collection:");
        System.out.println(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal().toString());


        try {
            return ResponseEntity.ok(
                    collectionService
                            .getCollections(
                                    SecurityContextHolder
                                            .getContext()
                                            .getAuthentication()
                                            .getPrincipal().toString()
                                    )

                            );

        } catch (CollectionNotFoundException e) {

            return ResponseEntity.status(200).body("none");

        } catch (DataStorageException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).build();

        }

    }

}
