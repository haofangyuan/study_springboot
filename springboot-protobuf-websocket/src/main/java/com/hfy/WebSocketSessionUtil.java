package com.hfy;

import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * Created by hfy on 2017/11/20.
 */
public class WebSocketSessionUtil {
    public static void sendMessage(WebSocketSession webSocketSession, BinaryMessage message) throws IOException {
        synchronized (webSocketSession) {
            if (webSocketSession.isOpen()) {
                webSocketSession.sendMessage(message);
            }
        }
    }
}
