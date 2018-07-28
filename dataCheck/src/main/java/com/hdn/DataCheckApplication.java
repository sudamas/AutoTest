package com.hdn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PreDestroy;

@EnableScheduling
@SpringBootApplication
public class DataCheckApplication {

    private  static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        DataCheckApplication.context = SpringApplication.run(DataCheckApplication.class,args);
    }

    @PreDestroy
    public void close(){
        DataCheckApplication.context.close();
    }

}
