package org.marketpulse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DataProcessorApp {

    public static void main(String[] args) {
        SpringApplication.run(DataProcessorApp.class, args);
        System.out.println("data-processor-service started");
    }
}