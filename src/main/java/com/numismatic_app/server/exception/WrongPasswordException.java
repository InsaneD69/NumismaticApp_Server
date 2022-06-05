package com.numismatic_app.server.exception;

public class WrongPasswordException extends Exception {
    public WrongPasswordException(String message) {
            super(message);
        }
}

