package com.hfy;

import com.hfy.network.bean.UserProtos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

/**
 *
 * Created by hfy on 2017/11/17.
 */
@Component
public class PbWebSocketHandler extends BinaryWebSocketHandler {

    @Autowired
    private CloudUtils cloudUtils;


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println(session + " --- afterConnectionEstablished -- ");
        super.afterConnectionEstablished(session);
        CloudUtils.setSession(session);
        // 1.测试客户端超时。服务端处理慢。
//        for (int i = 0; i < 100; i++) {
//            UserProtos.User user = UserProtos.User.newBuilder().setName("测试用户-" + i).build();
//            cloudUtils.sendMessage(user.toByteArray());
//        }

        // 2.只发送一条消息，测试服务端所线程发送
        UserProtos.User user = UserProtos.User.newBuilder().setName("测试用户-" + 0).build();
        cloudUtils.sendMessage(user.toByteArray());

        // 3.以下是测试多线程发送：发送不加锁，报错：BINARY_PARTIAL_WRITING
        /*new Thread(){
            @Override
            public void run() {
                while (true) {
                    UserProtos.User user = UserProtos.User.newBuilder().setName("测试用户-" + 1).build();
                    cloudUtils.sendMessage(user.toByteArray());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        new Thread(){
            @Override
            public void run() {
                while (true) {
                    UserProtos.User user = UserProtos.User.newBuilder().setName("测试用户-" + 2).build();
                    cloudUtils.sendMessage(user.toByteArray());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        new Thread(){
            @Override
            public void run() {
                while (true) {
                    UserProtos.User user = UserProtos.User.newBuilder().setName("测试用户-" + 3).build();
                    cloudUtils.sendMessage(user.toByteArray());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();*/
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {

        UserProtos.User user = UserProtos.User.parseFrom(message.getPayload());
        System.out.println(user.getName());
        super.handleBinaryMessage(session, message);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println(session + " --- afterConnectionClosed -- ");
        super.afterConnectionClosed(session, status);
    }
}
