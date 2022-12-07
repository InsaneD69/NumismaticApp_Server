 package com.numismatic_app.server.controller;



import com.numismatic_app.server.dto.JWTResponse;
import com.numismatic_app.server.dto.RefreshJWTRequest;
import com.numismatic_app.server.dto.UserDto;
import com.numismatic_app.server.entity.UserEntity;
import com.numismatic_app.server.exception.UserAlreadyExistException;
import com.numismatic_app.server.exception.UserNotFoundException;
import com.numismatic_app.server.exception.WrongPasswordException;
import com.numismatic_app.server.security.AuthProviderImpl;
import com.numismatic_app.server.service.ClientServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.message.AuthException;
import java.io.BufferedReader;


 /**
  * Обрабатывает запросы, связанные с аккаунтами пользователей
  */

@RestController
@RequestMapping("/acc")
@RequiredArgsConstructor

@Log4j2
public class UserController {


     private final ClientServiceImpl clientServiceImpl;

     private final AuthProviderImpl authProvider;

     /**
      * Метод обрабатывает запросы пользователей по пути /acc/new на регистрацию нового аккаунта
      *
      * @param user Содержит в себе данные пользователя, необходимые для регистрации
      *             {@link UserDto}
      * @return Возвращает ответ: успешно ли прошла регистрация
      */
     @PostMapping("/new")
     public ResponseEntity<String> registration(@RequestBody UserDto user) {

         log.info("Процесс регистрация пользователя  " + user.getUsername());
         try {
             clientServiceImpl.registration(user);
             log.info("Регистрация пользователя  " + user.getUsername() + " успешно завершена");
             return ResponseEntity.ok("пользователь с именем " + user.getUsername() + " успешно загерестрирован");
         } catch (UserAlreadyExistException e) {

             log.info("пользователь с именем " + user.getUsername() + " уже существует");

             return ResponseEntity.status(205).contentType(MediaType.APPLICATION_JSON).body(e.getMessage());
         } catch (Exception e) {

             e.printStackTrace();

             return ResponseEntity.badRequest().body("произошла ошибка");
         }

     }

     /**
      * Обрататывает запросs пользователей по пути /acc/login на аутентификацию
      *
      * @return Возвращает ответ об успешности аутентификации
      */
     @PostMapping("/login")
     public ResponseEntity<JWTResponse> logInAccount(@RequestBody UserDto authReq) throws  AuthException {

             final JWTResponse token = authProvider.login(authReq);
             return ResponseEntity.ok(token);


     }

     @PostMapping("/token")
     public ResponseEntity<JWTResponse> getNewAccessToken(@RequestBody RefreshJWTRequest request) {
         final JWTResponse token = authProvider.getAccessToken(request.getRefreshToken());
         return ResponseEntity.ok(token);
     }

     @PostMapping("/refresh")
     public ResponseEntity<Object> getNewRefreshToken(@RequestBody RefreshJWTRequest request)  {
         try{
         final JWTResponse token = authProvider.refresh(request.getRefreshToken());
         return ResponseEntity.ok(token);
         }
         catch (AuthException e){
             return  ResponseEntity.status(403).body(e.getMessage());
         }
     }
 }