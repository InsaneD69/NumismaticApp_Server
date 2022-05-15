package com.NumismaticApp.Server.NumismaticApp.DTO;

import com.NumismaticApp.Server.NumismaticApp.UcoinParser.Coin;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class CollectionDTO {

    private ArrayList<Coin> collection=new ArrayList<>();

    public String nameCollection;


    public String getNameCollection() {
        return nameCollection;
    }

    public CollectionDTO() {


    }

    public ArrayList<Coin> getCollection() {
        return collection;
    }


}

