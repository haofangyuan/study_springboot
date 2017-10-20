package com.hfy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by cage on 2017/10/20.
 */
@RestController
public class IndexController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/")
    public void printLog() {

        try {
            for (int i =0; i < 1000; i++) {
                logger.debug("debug");
            }
            logger.info("info");
            int j = 1/0;
        } catch (Exception e) {
            logger.error("error: {}", e);
        }


    }
}
