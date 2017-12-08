package com.hfy.client;

import com.hfy.thrift.HelloService;
import org.apache.thrift.TApplicationException;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.async.TAsyncClientManager;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by hfy on 2017/12/7.
 */
public class AppClient1 {

    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 8090;
    private static final int TIMEOUT = 3 * 1000;
    // 阻塞式
    private void startClient() {
        TTransport transport = null;
        try {
            // 阻塞式
            transport = new TSocket(SERVER_IP, SERVER_PORT, TIMEOUT);

            TProtocol protocol = new TBinaryProtocol(transport);
            HelloService.Client client = new HelloService.Client(protocol);
            transport.open();

            try {
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 调用方法开始
            System.out.println(client.helloString("xxx"));
            System.out.println(client.helloBoolean(true));
            // 测试超时，会抛异常：org.apache.thrift.transport.TTransportException: java.net.SocketTimeoutException: Read timed out
//            System.out.println(client.helloInt(12));
            // 测试返回null，会抛异常：org.apache.thrift.TApplicationException: helloNull failed: unknown result
//            client.helloNull();

        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
            if (e instanceof TApplicationException && ((TApplicationException) e).getType() == TApplicationException.MISSING_RESULT) {
                System.out.println("返回null");
            }
        } finally {
            if (null != transport) {
                transport.close();
            }
        }
    }

    // 非阻塞式
    private void startTFramedTransportClient() {
        TTransport transport = null;
        try {
            // 非阻塞式
            transport = new TFramedTransport(new TSocket(SERVER_IP, SERVER_PORT, TIMEOUT));

            TProtocol protocol = new TBinaryProtocol(transport);
            HelloService.Client client = new HelloService.Client(protocol);
            transport.open();

            // 调用方法开始
            System.out.println(client.helloString("xxx"));

        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
            if (e instanceof TApplicationException && ((TApplicationException) e).getType() == TApplicationException.MISSING_RESULT) {
                System.out.println("返回null");
            }
        } finally {
            if (null != transport) {
                transport.close();
            }
        }
    }

    // 异步客户端（Thrift 提供非阻塞的调用方式，可构建异步客户端）
    public void startTFramedTransportAsynClient() {
        TNonblockingTransport transport = null;
        try {
            TAsyncClientManager clientManager = new TAsyncClientManager();
            transport = new TNonblockingSocket(SERVER_IP, SERVER_PORT, TIMEOUT);

            TProtocolFactory tprotocol = new TBinaryProtocol.Factory();
            HelloService.AsyncClient asyncClient = new HelloService.AsyncClient(
                    tprotocol, clientManager, transport);
            System.out.println("Client start .....");

            CountDownLatch latch = new CountDownLatch(1);
            AsynCallback callBack = new AsynCallback(latch);
            System.out.println("call method start ...");
            asyncClient.helloInt(2, callBack);
            System.out.println("call method .... end");
            boolean wait = latch.await(30, TimeUnit.SECONDS);
            System.out.println("latch.await: " + wait);
            System.out.println("result: " + callBack.getResult());
            /*// 或者通过轮询方式获取。
            Object res = callBack.getResult();
            // 等待服务调用后的返回结果
            while (res == null) {
                res = callBack.getResult();
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != transport) {
                transport.close();
            }
        }
        System.out.println("startClient end.");
    }

    public class AsynCallback implements AsyncMethodCallback {
        private CountDownLatch latch;
        private Object response;

		public AsynCallback(CountDownLatch latch) {
            this.latch = latch;
        }

        public Object getResult() {
            return response;
        }

        @Override
        public void onComplete(Object response) {
            System.out.println("onComplete");
            this.response = response;
            latch.countDown();
        }

        @Override
        public void onError(Exception exception) {
            System.out.println("onError :" + exception.getMessage());
            latch.countDown();
        }


    }

    public static void main(String[] args) {
        AppClient1 client = new AppClient1();
        client.startClient();
//        client.startTFramedTransportClient();
//        client.startTFramedTransportAsynClient();
    }
}
