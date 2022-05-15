package com.NumismaticApp.Server.NumismaticApp.repository.Model;

import com.NumismaticApp.Server.NumismaticApp.Entity.CollectionEntity;

public class Collection {

    private String collectionname;

    public String getCollectionname() {
        return collectionname;
    }

    public void setCollectionname(String collectionname) {
        this.collectionname = collectionname;
    }

    public static Collection toModel(CollectionEntity entity){

        Collection model = new Collection();
        model.setCollectionname(entity.getCollectionname());
        return model;
    }

    public  Collection(){}

}
