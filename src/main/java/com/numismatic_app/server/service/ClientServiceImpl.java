package com.numismatic_app.server.service;

import com.numismatic_app.server.dto.UserDto;
import com.numismatic_app.server.entity.UserEntity;
import com.numismatic_app.server.exception.UserAlreadyExistException;
import com.numismatic_app.server.repository.UserRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Отвечает за обращения к User репозиторию, а следовательно, за обращения к базе данных.
 */
@Service
@Log4j2
public class ClientServiceImpl  {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * @param incomingUser Данные пользователя для регистрации {@link UserDto}
     * @throws UserAlreadyExistException Выбрасывается, если уже есть пользователь с таким никнеймом
     */
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


}
