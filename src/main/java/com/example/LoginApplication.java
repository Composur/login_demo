package com.example;

import com.example.service.DatabaseConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class LoginApplication {

    @Autowired
    private DatabaseConnectionService databaseConnectionService;

    public static void main(String[] args) {
        SpringApplication.run(LoginApplication.class, args);
        System.out.println("----------------------");
        System.out.println("|---start success----|");
        System.out.println("----------------------");
    }

    @PostConstruct
    public void init() {
        boolean isConnected = databaseConnectionService.testDatabaseConnection();
        if (isConnected) {
            System.out.println("Database connection successful.");
        } else {
            System.out.println("Failed to connect to the database.");
        }
    }
}
