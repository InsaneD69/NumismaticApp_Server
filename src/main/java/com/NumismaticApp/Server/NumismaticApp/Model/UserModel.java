package com.NumismaticApp.Server.NumismaticApp.Model;

import com.NumismaticApp.Server.NumismaticApp.Entity.UserEntity;

import java.util.List;
import java.util.stream.Collectors;

public class UserModel {
    private Long id;
    private String username;

    public List<CollectionModel> getCollections() {
        return collections;
    }

    private List<CollectionModel> collections;

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setCollections(List<CollectionModel> collections) {

        this.collections = collections;

    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static UserModel toModel(UserEntity entity){

        UserModel model = new UserModel();
        model.setId(entity.getId());
        model.setUsername(entity.getUsername());
        if(entity.getCollectionEntities()==null){
            model.setCollections(null);
        }else {
            model.setCollections(entity.getCollectionEntities().stream().map(CollectionModel::toModel).collect(Collectors.toList()));
        }

        return  model;

    }


}
