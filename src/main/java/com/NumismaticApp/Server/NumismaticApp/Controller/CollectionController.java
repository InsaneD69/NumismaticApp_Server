package com.NumismaticApp.Server.NumismaticApp.Controller;


import com.NumismaticApp.Server.NumismaticApp.DTO.CollectionDTO;
import com.NumismaticApp.Server.NumismaticApp.Entity.CollectionEntity;
import com.NumismaticApp.Server.NumismaticApp.Entity.UserEntity;
import com.NumismaticApp.Server.NumismaticApp.Exception.CollectionNotFoundException;
import com.NumismaticApp.Server.NumismaticApp.Exception.UserAlreadyExistException;
import com.NumismaticApp.Server.NumismaticApp.Service.CollectionService;
import com.NumismaticApp.Server.NumismaticApp.repository.CollectionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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


    @PutMapping("/get")
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
