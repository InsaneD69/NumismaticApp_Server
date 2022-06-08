package com.numismatic_app.server.validator;

import com.numismatic_app.server.exception.CollectionStatusException;

public class IncomingStatusToCollection {

    private IncomingStatusToCollection() {
        throw new IllegalStateException("Utility class");
    }

    public static void checkStatus(String status) throws CollectionStatusException {

        if(!(status.equals("new")||status.equals("update"))){

            throw new CollectionStatusException("exteds new or update status");
        }
    }
}
