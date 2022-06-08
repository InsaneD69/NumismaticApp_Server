package com.numismatic_app.server.controller;


import com.numismatic_app.server.dto.CollectionDTO;
import com.numismatic_app.server.entity.UserEntity;
import com.numismatic_app.server.exception.CollectionNameAlreadyIsExist;
import com.numismatic_app.server.exception.CollectionNotFoundException;
import com.numismatic_app.server.exception.CollectionStatusException;
import com.numismatic_app.server.exception.DataStorageException;
import com.numismatic_app.server.service.CollectionService;
import com.numismatic_app.server.validator.IncomingStatusToCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Обслуживает запросы, связанные с коллекциями пользователей
 */
@RestController
@RequestMapping("/collection")
public class CollectionController {

    @Autowired
    private CollectionService collectionService;


    /** Обрабатывает запросы клиентов по пути /collection/postcollection
     * , сохраняет коллекцию пользователя в его аккаунт
     * @param collectionDTO Колекция, которую нужно сохранить
     * @return Положительный или отрицательный ответ
     */
    @PostMapping("/postcollection")
   public ResponseEntity<String> saveUserCollection(@RequestBody CollectionDTO collectionDTO, @RequestParam String status){

        try {
            IncomingStatusToCollection.checkStatus(status);
            collectionService.saveCollectionToAcc(collectionDTO
                    ,(UserEntity)SecurityContextHolder.getContext().getAuthentication().getPrincipal(),status);
           return ResponseEntity.ok().body("ok");
        } catch (DataStorageException e) {
            return  ResponseEntity.badRequest().body("произошла ошибка");
        } catch (CollectionNameAlreadyIsExist | CollectionStatusException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }


    /** Обрабатывает запросы клиентов по пути /collection/get на получение его коллеций на аккаунте
     * @return Возвращает список из коллекций {@link CollectionDTO}
     */
   @GetMapping("/get")
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

   }

}
