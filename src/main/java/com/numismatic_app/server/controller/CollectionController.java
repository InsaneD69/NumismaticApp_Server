package com.numismatic_app.server.controller;


import com.numismatic_app.server.dto.CollectionDTO;
import com.numismatic_app.server.entity.UserEntity;
import com.numismatic_app.server.exception.CollectionNotFoundException;
import com.numismatic_app.server.exception.DataStorageException;
import com.numismatic_app.server.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/collection")
public class CollectionController {



    @Autowired
    private CollectionService service;




    @PostMapping("/postcollection")
   public ResponseEntity<String> createNewCollection(@RequestBody CollectionDTO collectionDTO){

        try {
            service.createCollection(collectionDTO
                    ,(UserEntity)SecurityContextHolder.getContext().getAuthentication().getPrincipal());
           return ResponseEntity.ok().body("ok");
        } catch (DataStorageException e) {
            return  ResponseEntity.badRequest().body("произошла ошибка");
        }

    }




    @GetMapping("/get")
    public ResponseEntity<Object> getCollection()

        {

        try{
            return  ResponseEntity.ok(
                    service
                            .getCollections(
                                    ((UserEntity)SecurityContextHolder.getContext().getAuthentication().getPrincipal()
                                    )

                            )
            );

        }  catch (CollectionNotFoundException e) {

            return  ResponseEntity.badRequest().body(e.getMessage());

        } catch (DataStorageException e) {
            return  ResponseEntity.status(500).body(e.getMessage());
        }
        catch (Exception e){
            return  ResponseEntity.status(500).build();

        }


        }



}