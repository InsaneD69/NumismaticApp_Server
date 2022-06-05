 package com.numismatic_app.server.controller;



import com.numismatic_app.server.entity.UserEntity;
import com.numismatic_app.server.exception.UserAlreadyExistException;
import com.numismatic_app.server.service.ClientServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


 @RestController
@RequestMapping("/acc")
@Log4j2
public class UserController  {


    @Autowired
    private ClientServiceImpl clientServiceImpl;


    @PostMapping("/new")
    public ResponseEntity registration(@RequestBody UserEntity user){


       log.info("Процесс регистрация пользователя  "+user.getUsername());
        try {
            clientServiceImpl.registration(user);
            log.info("Регистрация пользователя  "+user.getUsername()+"успешно завершена");
            return ResponseEntity.ok("пользователь с именем "+user.getUsername()+" успешно загерестрирован");
        }
        catch (UserAlreadyExistException e){

            return  ResponseEntity.badRequest().body(e.getMessage());
        }
        catch(Exception e) {

            e.printStackTrace();

            return  ResponseEntity.badRequest().body("произошла ошибка");
        }

    }


    @PutMapping("/login")
    public ResponseEntity logInAccount(){

        return  ResponseEntity.status(200).body("successful login");

    }



    @GetMapping("/get")
    public ResponseEntity getUser(@RequestParam String username){

        System.out.println(username);
        try {


            return ResponseEntity.ok(clientServiceImpl.getOne(username));
        }
        catch(Exception e) {

            return  ResponseEntity.badRequest().body("произошла ошибка");

         }

    }
}
