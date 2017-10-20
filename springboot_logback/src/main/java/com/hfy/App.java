package com.hfy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

/**
 *
 * Created by cage on 2017/10/20.
 */
@SpringBootApplication
public class App {

    public static void main(String[] args) {
        System.setProperty("log.path", new File("").getAbsolutePath() + "\\log_test.log");
        SpringApplication.run(App.class, args);
    }
}
