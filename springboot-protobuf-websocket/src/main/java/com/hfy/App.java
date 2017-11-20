package com.hfy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * 启动类
 * Created by hfy on 2017/11/20.
 */
@SpringBootApplication
@EnableWebSocket
public class App implements WebSocketConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(echoWebSocketHandler(), "/springboot-protobuf-websocket").setAllowedOrigins("www.hfy.com");;
    }

    @Bean
    public WebSocketHandler echoWebSocketHandler() {
        return new PbWebSocketHandler();
    }
}
