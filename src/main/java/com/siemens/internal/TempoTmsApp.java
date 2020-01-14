package com.siemens.internal;

import com.atlassian.plugin.spring.AvailableToPlugins;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@AvailableToPlugins
public class TempoTmsApp {

    public static void main(String[] args) {
        SpringApplication.run(TempoTmsApp.class, args);
    }

}
