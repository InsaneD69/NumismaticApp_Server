package com.numismatic_app.server.service;

import com.numismatic_app.server.dto.UserDto;
import com.numismatic_app.server.entity.UserEntity;
import com.numismatic_app.server.exception.UserAlreadyExistException;
import com.numismatic_app.server.exception.UserNotFoundException;
import com.numismatic_app.server.exception.WrongPasswordException;
import com.numismatic_app.server.model.UserModel;
import com.numismatic_app.server.repository.UserRepo;
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

    public void registration(UserDto incomingUser) throws UserAlreadyExistException {

        UserEntity entity = new UserEntity();
        entity.setUsername(incomingUser.getUsername());

        UserEntity user = userRepo.findByUsername(entity.getUsername());

        if (user != null) {

            throw new UserAlreadyExistException("Пользователь с таким именем существует");

        }
        entity.setPassword(passwordEncoder.encode(incomingUser.getPassword()));

        userRepo.save(entity);

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
