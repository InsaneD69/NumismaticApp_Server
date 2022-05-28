 package com.NumismaticApp.Server.NumismaticApp.Controller;



import com.NumismaticApp.Server.NumismaticApp.Entity.UserEntity;
import com.NumismaticApp.Server.NumismaticApp.Exception.UserAlreadyExistException;
import com.NumismaticApp.Server.NumismaticApp.Exception.UserNotFoundException;
import com.NumismaticApp.Server.NumismaticApp.Exception.WrongPasswordException;
import com.NumismaticApp.Server.NumismaticApp.Service.ClientServiceImpl;
import com.NumismaticApp.Server.NumismaticApp.repository.Model.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;


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
            return ResponseEntity.ok("пользователь с именем "+user.getUsername()+" успешно сохранен");
        }
        catch (UserAlreadyExistException e){

            return  ResponseEntity.badRequest().body(e.getMessage());
        }
        catch(Exception e) {

            e.printStackTrace();

            return  ResponseEntity.badRequest().body("произошла ошибка");
        }

    }


    @GetMapping("/login")
    public ResponseEntity logInAccount(){

        /*User user;

        try {

           user= clientServiceImpl.logInUser(incomingUser);
        }
        catch (UserNotFoundException e){

            return  ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (WrongPasswordException e){

            return  ResponseEntity.badRequest().body(e.getMessage());

        }

        return  ResponseEntity.ok().body(user);*/
        return ResponseEntity.ok().body("Successful login");

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
