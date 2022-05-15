package com.NumismaticApp.Server.NumismaticApp.Controller;


import com.NumismaticApp.Server.NumismaticApp.Entity.CollectionEntity;
import com.NumismaticApp.Server.NumismaticApp.Exception.CollectionNotFoundException;
import com.NumismaticApp.Server.NumismaticApp.Exception.UserAlreadyExistException;
import com.NumismaticApp.Server.NumismaticApp.Service.CollectionService;
import com.NumismaticApp.Server.NumismaticApp.repository.CollectionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/collection")
public class CollectionController {



    @Autowired
    private CollectionService service;

    @PostMapping("/new")
    private ResponseEntity createNewCollection(  @RequestBody CollectionEntity collectionEntity,
                                                 @RequestParam String username){

        try {
            return ResponseEntity.ok(service.createCollection(collectionEntity,username));
        }
        catch(Exception e) {e.printStackTrace();



            return  ResponseEntity.badRequest().body("произошла ошибка");

        }


    }


    @GetMapping("/get")
    private ResponseEntity getCollection(@RequestParam String username,
                                         @RequestParam String collectionname
                                         ){


        try{
            return  ResponseEntity.ok(service.getCollection(username,collectionname));

        }  catch (CollectionNotFoundException e) {
            return  ResponseEntity.badRequest().body(e.getMessage());

        }


    }



}
