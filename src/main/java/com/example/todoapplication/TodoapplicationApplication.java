package com.example.todoapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
@SpringBootApplication
@EntityScan("com.example.todoapplication.model")
public class TodoapplicationApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoapplicationApplication.class, args);
    }

}
