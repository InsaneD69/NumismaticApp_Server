package com.NumismaticApp.Server.NumismaticApp.Service;

import com.NumismaticApp.Server.NumismaticApp.Entity.UserEntity;
import com.NumismaticApp.Server.NumismaticApp.Exception.UserAlreadyExistException;
import com.NumismaticApp.Server.NumismaticApp.Exception.UserNotFoundException;
import com.NumismaticApp.Server.NumismaticApp.Exception.WrongPasswordException;
import com.NumismaticApp.Server.NumismaticApp.Model.UserModel;
import com.NumismaticApp.Server.NumismaticApp.repository.UserRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ClientServiceImpl  {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserModel registration(UserEntity incomingUser) throws UserAlreadyExistException {

        UserEntity user = userRepo.findByUsername(incomingUser.getUsername());

        if (user != null) {

            throw new UserAlreadyExistException("Пользователь с таким именем существует");

        }
        incomingUser.setPassword(passwordEncoder.encode(incomingUser.getPassword()));

        return UserModel.toModel(userRepo.save(incomingUser));

    }

    public UserEntity getOne(String name) throws UserNotFoundException {
        UserEntity user = userRepo.findByUsername(name);
        if (user == null) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        return user;
    }

    public UserModel logInUser(UserEntity incomingUser) throws UserNotFoundException, WrongPasswordException {

        UserEntity user = userRepo.findByUsername(incomingUser.getUsername());

        if (user == null) {
            throw new UserNotFoundException("Пользователь с таким логином не существует");
        }

        if (!passwordEncoder.encode(incomingUser.getPassword()).equals(user.getPassword())){

            throw new WrongPasswordException("неверный пароль");

        }

        return UserModel.toModel(user);





    }





    public Long delete(Long id) {
        userRepo.deleteById(id);
        return id;
    }



}
