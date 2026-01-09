package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

@org.springframework.boot.autoconfigure.SpringBootApplication
@ComponentScan({"org.example"})
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        //http://localhost:8080/swagger-ui/swagger-ui/index.html





    }
}