package com.hfy;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

/**
 *
 * Created by hfy on 2017/11/20.
 */
@SpringBootApplication
@Component
public class App implements InitializingBean {

    @Autowired
    private CloudUtils cloudUtils;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        cloudUtils.createConn();
    }
}
