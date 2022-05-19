package com.NumismaticApp.Server.NumismaticApp.BusinessComponents;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Log4j2
public class ConnectionToUcoin implements Runnable {

    @Autowired
    CountryListComponent countryListComponent;


    @Override
    public void run() {
        log.info("ConnectionToUcoin start");
        
    }
}
