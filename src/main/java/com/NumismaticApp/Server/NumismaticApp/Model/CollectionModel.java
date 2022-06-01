package com.NumismaticApp.Server.NumismaticApp.Model;

import com.NumismaticApp.Server.NumismaticApp.Entity.CollectionEntity;

public class CollectionModel {

    private String collectionname;

    public String getCollectionname() {
        return collectionname;
    }

    public void setCollectionname(String collectionname) {
        this.collectionname = collectionname;
    }

    public static CollectionModel toModel(CollectionEntity entity){

        CollectionModel model = new CollectionModel();
        model.setCollectionname(entity.getCollectionname());
        return model;
    }

    public CollectionModel(){}

}
