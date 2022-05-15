package com.NumismaticApp.Server.NumismaticApp.repository;


import com.NumismaticApp.Server.NumismaticApp.Entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends CrudRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);

}
