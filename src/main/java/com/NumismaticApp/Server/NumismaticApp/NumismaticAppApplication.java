package com.NumismaticApp.Server.NumismaticApp;

import com.NumismaticApp.Server.NumismaticApp.BusinessComponents.CountryListComponent;
import com.NumismaticApp.Server.NumismaticApp.Security.SecurityInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NumismaticAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(NumismaticAppApplication.class, args);
	}

}
