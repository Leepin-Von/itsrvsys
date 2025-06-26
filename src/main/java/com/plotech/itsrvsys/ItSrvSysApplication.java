package com.plotech.itsrvsys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class}, scanBasePackages = {"org.jeecg.modules.jmreport", "com.plotech.itsrvsys"})
public class ItSrvSysApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItSrvSysApplication.class, args);
    }

}
