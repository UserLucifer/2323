package com.compute.rental;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@MapperScan("com.compute.rental.modules")
@ConfigurationPropertiesScan
@SpringBootApplication
public class ComputeRentalApplication {

    public static void main(String[] args) {
        SpringApplication.run(ComputeRentalApplication.class, args);
    }
}
