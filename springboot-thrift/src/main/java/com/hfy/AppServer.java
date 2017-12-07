package com.hfy;

import com.hfy.thrift.HelloService;
import com.hfy.thrift.impl.HelloServiceImpl;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by hfy on 2017/12/7.
 */
@SpringBootApplication
public class AppServer {

    private static final int SERVER_PORT = 8090;

    public static void main(String[] args) {
        SpringApplication.run(AppServer.class, args);

        startServer();
//        startTFramedTransportServer();
    }

    // TSocket —— 使用阻塞式 I/O 进行传输，是最常见的模式
    private static void startServer() {

        try {
            TServerSocket serverTransport = new TServerSocket(SERVER_PORT);
            HelloService.Processor processor = new HelloService.Processor(new HelloServiceImpl());
            TBinaryProtocol.Factory factory = new TBinaryProtocol.Factory();

            // 单线程服务器端，一般用于测试
//            TServer.Args tArgs = new TServer.Args(serverTransport);
//            tArgs.processor(processor);
//            tArgs.protocolFactory(factory);
//            TServer server = new TSimpleServer(tArgs);

            // 多线程服务器端，预先创建一组线程处理请求。
            TThreadPoolServer.Args args = new TThreadPoolServer.Args(
                    serverTransport);
            args.processor(processor);
            args.protocolFactory(factory);
            TServer server = new TThreadPoolServer(args);

            System.out.println("Running server...");
            server.serve();
        } catch (TTransportException e) {
            e.printStackTrace();
        }

    }

    // TFramedTransport —— 使用非阻塞方式，按块的大小进行传输，类似于 Java 中的 NIO
    private static void startTFramedTransportServer() {

        try {
            // 多线程服务器端使用非阻塞式 I/O。
            TNonblockingServerSocket serverTransport = new TNonblockingServerSocket(SERVER_PORT);
            HelloService.Processor processor = new HelloService.Processor(new HelloServiceImpl());
            TBinaryProtocol.Factory protocolFactory = new TBinaryProtocol.Factory();
            TFramedTransport.Factory transportFactory = new TFramedTransport.Factory();

//            TNonblockingServer.Args tArgs = new TNonblockingServer.Args(serverTransport);
//            tArgs.processor(processor);
//            tArgs.protocolFactory(protocolFactory);
//            tArgs.transportFactory(transportFactory);
//            TNonblockingServer server = new TNonblockingServer(tArgs);

            //半同步半异步的服务模型
            THsHaServer.Args thhsArgs = new THsHaServer.Args(serverTransport);
            thhsArgs.processor(processor);
            thhsArgs.protocolFactory(protocolFactory);
            thhsArgs.transportFactory(transportFactory);
            TServer server = new THsHaServer(thhsArgs);

            System.out.println("Running server...");
            server.serve();
        } catch (TTransportException e) {
            e.printStackTrace();
        }

    }
}
