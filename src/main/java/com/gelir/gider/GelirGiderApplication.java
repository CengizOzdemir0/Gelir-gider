package com.gelir.gider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GelirGiderApplication {

    public static void main(String[] args) {
        SpringApplication.run(GelirGiderApplication.class, args);
    }
}
