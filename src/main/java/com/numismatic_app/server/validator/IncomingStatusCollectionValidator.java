package com.numismatic_app.server.validator;

import com.numismatic_app.server.exception.CollectionStatusException;

/**
 * Проверяет на корректность статус публикации коллекции пользователя
 */
public class IncomingStatusCollectionValidator {

    private IncomingStatusCollectionValidator() {
        throw new IllegalStateException("Utility class");
    }

    public static void checkStatus(String status) throws CollectionStatusException {

        if(!(status.equals("new")||status.equals("update"))){

            throw new CollectionStatusException("exteds new or update status");
        }
    }
}
