package com.NumismaticApp.Server.NumismaticApp.DTO;

import java.io.Serializable;
import java.util.ArrayList;

public class CollectionDTO implements Serializable {

    private ArrayList<CoinDto> collection=new ArrayList<>();
    public String nameCollection;



    public CollectionDTO() {}

    public ArrayList<CoinDto> getCollection() {return collection;}
    public String getNameCollection() {
        return nameCollection;
    }

}

