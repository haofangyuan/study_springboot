package com.hfy.twoway.server;

import com.hfy.thrift.Message;
import com.hfy.thrift.MessageService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

/**
 * This class is a stub that the server can use to send messages back
 * to the client.
 *
 * @author Joel Meyer
 */
public class MessageServiceClient implements MessageService.Iface {
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
