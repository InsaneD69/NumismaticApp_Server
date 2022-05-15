package com.NumismaticApp.Server.NumismaticApp.repository.Model;

import com.NumismaticApp.Server.NumismaticApp.Entity.UserEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class User {
    private Long id;
    private String username;

    public List<Collection> getCollections() {
        return collections;
    }

    private List<Collection> collections;

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setCollections(List<Collection> collections) {

        this.collections = collections;

    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static User toModel(UserEntity entity){

        User model = new User();
        model.setId(entity.getId());
        model.setUsername(entity.getUsername());
        if(entity.getCollectionEntities()==null){
            model.setCollections(null);
        }else {
            model.setCollections(entity.getCollectionEntities().stream().map(Collection::toModel).collect(Collectors.toList()));
        }

        return  model;

    }


}
