 package com.numismatic_app.server.controller;



import com.numismatic_app.server.dto.UserDto;
import com.numismatic_app.server.entity.UserEntity;
import com.numismatic_app.server.exception.UserAlreadyExistException;
import com.numismatic_app.server.service.ClientServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;


 @RestController
@RequestMapping("/acc")
@Log4j2
public class UserController  {


    @Autowired
    private ClientServiceImpl clientServiceImpl;


    @PostMapping("/new")
    public ResponseEntity<String> registration(@RequestBody UserDto user){



       log.info("Процесс регистрация пользователя  "+user.getUsername());
        try {
            clientServiceImpl.registration(user);
            log.info("Регистрация пользователя  "+user.getUsername()+"успешно завершена");
            return ResponseEntity.ok("пользователь с именем "+user.getUsername()+" успешно загерестрирован");
        }
        catch (UserAlreadyExistException e){

            log.info("пользователь с именем "+user.getUsername()+"уже существует");

            return  ResponseEntity.status(205).contentType(MediaType.APPLICATION_JSON).body(e.getMessage());
        }
        catch(Exception e) {

            e.printStackTrace();

            return  ResponseEntity.badRequest().body("произошла ошибка");
        }

    }

    @GetMapping("/login")
    public ResponseEntity<String> logInAccount(){

        return  ResponseEntity.status(200).body("уcпешная аунтефикация");

    }


}
