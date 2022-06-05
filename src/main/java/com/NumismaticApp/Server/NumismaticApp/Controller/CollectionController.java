package com.NumismaticApp.Server.NumismaticApp.Controller;


import com.NumismaticApp.Server.NumismaticApp.DTO.CollectionDTO;
import com.NumismaticApp.Server.NumismaticApp.Entity.UserEntity;
import com.NumismaticApp.Server.NumismaticApp.Exception.CollectionNotFoundException;
import com.NumismaticApp.Server.NumismaticApp.Exception.DataStorageException;
import com.NumismaticApp.Server.NumismaticApp.Service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/collection")
public class CollectionController {



    @Autowired
    private CollectionService service;




    @PostMapping("/new")
    private ResponseEntity createNewCollection(@RequestBody CollectionDTO collectionDTO){

        try {
           return ResponseEntity.ok().body(service.createCollection(collectionDTO
                   ,(UserEntity)SecurityContextHolder.getContext().getAuthentication().getPrincipal())
           );
        }
        catch(Exception e) {e.printStackTrace();



            return  ResponseEntity.badRequest().body("произошла ошибка");

        }



    }


    @GetMapping("/get")
    private ResponseEntity getCollection()

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


        }



}
