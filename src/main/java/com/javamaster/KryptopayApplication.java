package com.javamaster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KryptopayApplication {

    public static void main(String[] args) {
        SpringApplication.run(KryptopayApplication.class, args);
    }


}
