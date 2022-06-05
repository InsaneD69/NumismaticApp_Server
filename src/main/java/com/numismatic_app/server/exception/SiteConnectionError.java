package com.numismatic_app.server.exception;

public class SiteConnectionError extends Exception{
    public SiteConnectionError(String message) {
        super(message);
    }
}
