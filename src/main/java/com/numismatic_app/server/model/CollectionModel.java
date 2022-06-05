package com.numismatic_app.server.model;

import com.numismatic_app.server.entity.CollectionEntity;

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
