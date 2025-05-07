package com.one.bootkafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BootKafkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootKafkaApplication.class, args);
    }

}
