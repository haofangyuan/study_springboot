package com.hfy;

import com.hfy.network.bean.UserProtos;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * Created by hfy on 2017/11/20.
 */
public class PbWebSocketHandler extends BinaryWebSocketHandler {

    /**
     * 安全的
     */
    private ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<String, WebSocketSession>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println(session + " --- afterConnectionEstablished -- ");
        sessions.put(session.getId(), session);
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        // 注意：客户端发送数据快，服务端处理慢，即使session关闭，此处还是会接收到未处理的消息。
        // 1.测试客户端超时。服务端处理慢。
//        Thread.sleep(2 * 1000);
        UserProtos.User user = UserProtos.User.parseFrom(message.getPayload());
        System.out.println(user.getName());
        WebSocketSession webSocketSession = sessions.get(session.getId());
        try {
            // 2.发送前，请先判断是否session已经打开。否则会报错：java.io.IOException: java.io.IOException: 远程主机强迫关闭了一个现有的连接
            if (webSocketSession != null && webSocketSession.isOpen()) {
                WebSocketSessionUtil.sendMessage(webSocketSession, message);

                // 3.以下是测试多线程发送：发送不加锁，报错：BINARY_PARTIAL_WRITING
                /*new Thread(){
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                UserProtos.User user1 = UserProtos.User.newBuilder().setName("线程1").build();
                                BinaryMessage binaryMessage = new BinaryMessage(user1.toByteArray());
                                WebSocketSessionUtil.sendMessage(webSocketSession, binaryMessage);
                                Thread.sleep(1000);
                            } catch (IOException | InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }.start();
                new Thread(){
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                UserProtos.User user1 = UserProtos.User.newBuilder().setName("线程2").build();
                                BinaryMessage binaryMessage = new BinaryMessage(user1.toByteArray());
                                WebSocketSessionUtil.sendMessage(webSocketSession, binaryMessage);
                                Thread.sleep(1000);
                            } catch (IOException | InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }.start();
                new Thread(){
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                UserProtos.User user1 = UserProtos.User.newBuilder().setName("线程3").build();
                                BinaryMessage binaryMessage = new BinaryMessage(user1.toByteArray());
                                WebSocketSessionUtil.sendMessage(webSocketSession, binaryMessage);
                                Thread.sleep(1000);
                            } catch (IOException | InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }.start();*/

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println(session + " --- afterConnectionClosed -- " + status.getReason());
        sessions.remove(session.getId());
    }
}
