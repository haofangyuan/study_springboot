package com.hfy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

@Component
public class CloudUtils {

    private static final Logger logger = LoggerFactory.getLogger(CloudUtils.class);

    private String WS_URI = "ws://127.0.0.1:8082/springboot-protobuf-websocket";
    private static final String ORIGIN = "www.hfy.com";
    private static WebSocketSession session;

    @Autowired
    private PbWebSocketHandler pbWebSocketHandler;

    public static synchronized void setSession(WebSocketSession session) {
        CloudUtils.session = session;
    }

    public void createConn() {
        createConn(session, pbWebSocketHandler);
    }

    private void createConn(WebSocketSession session, BinaryWebSocketHandler handler) {
        if (session != null && session.isOpen()) {
            return;
        }
        StandardWebSocketClient client = new StandardWebSocketClient();
        WebSocketConnectionManager manager = new WebSocketConnectionManager(
                client, handler, WS_URI);
        manager.setOrigin(ORIGIN);
        manager.start();
        logger.info("connect to the cloud ... ");
    }

    public void sendMessage(byte[] bytes) {
        if (connectCloud()) {
            try {
                BinaryMessage message = new BinaryMessage(bytes);
                synchronized (session) {    // 此处需要加锁，否则多线程调用报错：java.lang.IllegalStateException: The remote endpoint was in state [BINARY_PARTIAL_WRITING] which is an invalid state for called method
                    session.sendMessage(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            logger.info("not connected to the cloud !!! ");
        }
    }


    /**
     * 车场是否连接上了云端(下行通道)
     *
     * @return boolean
     */
    public static boolean connectCloud() {
        return session != null && session.isOpen();
    }

}
