package com.hfy.twoway.other.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.hfy.thrift.Message;
import com.hfy.thrift.MessageService;
import org.apache.log4j.Logger;

import org.apache.thrift.TException;
import org.apache.thrift.TProcessor;
import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

/**
 * A simple class that uses a blocking queue to accept and publish
 * messages. This class should be run in its own thread to ensure
 * that message sending doesn't hijack the message receiving thread.
 * 一个简单的类，使用阻塞队列接受和发布消息.此类应在其自己的线程中运行, 以确保发送的消息不会劫持消息接收线程。
 *
 * @author Joel Meyer
 */
public class MessageDistributor implements MessageService.Iface, Runnable {
    private static final Logger LOGGER = Logger.getLogger(MessageDistributor.class);

    private final BlockingQueue<Message> messageQueue;
    private final List<MessageServiceClient> clients;

    public MessageDistributor() {
        this.messageQueue = new LinkedBlockingQueue<Message>();
        this.clients = new ArrayList<MessageServiceClient>();
    }

    public void addClient(MessageServiceClient client) {
        // There should be some synchronization around this list
        clients.add(client);
        LOGGER.info(String.format("Added client at %s", client.getAddy()));
    }

    @Override
    public void run() {
        while (true) {
            try {
                Message msg = messageQueue.take();

                Iterator<MessageServiceClient> clientItr = clients.iterator();
                while (clientItr.hasNext()) {
                    MessageServiceClient client = clientItr.next();
                    try {
                        client.sendMessage(msg);
                    } catch (TException te) {
                        // Most likely client disconnected, should remove it from the list
                        clientItr.remove();
                        LOGGER.info(String.format("Removing %s from client list.", client.getAddy()));
                        LOGGER.debug(te);
                    }
                }
            } catch (InterruptedException ie) {
                LOGGER.debug(ie);
            }
        }
    }

    @Override
    public void sendMessage(Message msg) throws TException {
        System.out.println("server get msg : " + msg.getMessage());
        messageQueue.add(msg);
        LOGGER.info(String.format("Adding message to queue:\n%s", msg));
    }

    /**
     * This class is a stub that the server can use to send messages back
     * to the client.
     *
     * @author Joel Meyer
     */
    public static class MessageServiceClient implements MessageService.Iface {
        protected final TTransport transport;
        protected final String addy;
        protected final int port;
        protected final MessageService.Client client;

        public MessageServiceClient(TTransport transport) {
            TSocket tsocket = (TSocket) transport;
            this.transport = transport;

            this.client = new MessageService.Client(new TBinaryProtocol(transport));
            this.addy = tsocket.getSocket().getInetAddress().getHostAddress();
            this.port = tsocket.getSocket().getPort();

        }

        public String getAddy() {
            return addy;
        }

        public void sendMessage(Message msg) throws TException {
            System.out.println("server send msg : " + msg.getMessage());
            this.client.sendMessage(msg);
        }
    }

    /**
     * A simple server that accepts messages from clients and broadcasts
     * them out to all connected clients.
     *
     * @author Joel Meyer
     */
    public static class Server {
        private static final Logger LOGGER = Logger.getLogger(Server.class);

        private static final int port = 8890;
        public static void main(String[] args) throws Exception {


            final MessageDistributor messageDistributor = new MessageDistributor();

            new Thread(messageDistributor).start();

            // Using our own TProcessorFactory gives us an opportunity to get
            // access to the transport right after the client connection is
            // accepted.
            TProcessorFactory processorFactory = new TProcessorFactory(null) {
                @Override
                public TProcessor getProcessor(TTransport trans) {
                    messageDistributor.addClient(new MessageServiceClient(trans));
                    return new MessageService.Processor(messageDistributor);
                }
            };


            TServerTransport serverTransport = new TServerSocket(port);
    //        TServer server = new TThreadPoolServer(processorFactory, serverTransport);

            // 多线程服务器端，预先创建一组线程处理请求。
            TThreadPoolServer.Args serverArgs = new TThreadPoolServer.Args(serverTransport);
    //        args.processor(processor);
            serverArgs.processorFactory(processorFactory);
            TServer server = new TThreadPoolServer(serverArgs);

            LOGGER.info("Server started");
            server.serve();
        }
    }
}
