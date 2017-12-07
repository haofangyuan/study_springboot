package com.hfy;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.*;
import java.lang.reflect.Method;

/**
 *
 * Created by hfy on 2017/12/5.
 */
public class HttpUtil {

    private static final int TIME_OUT = 5 * 1000;

    public static <T> T postPb(String url, byte[] bytes, Class<T> clazz) {
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            config(httpPost);

            httpPost.addHeader("Content-Type", "application/x-protobuf");
            httpPost.addHeader("Accept", "application/x-protobuf");
            httpPost.setEntity(new ByteArrayEntity(bytes));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            if (response != null) {
                int res = response.getStatusLine().getStatusCode();
                if (res == 200) {
                    InputStream inputStream = response.getEntity().getContent();
                    Method method = clazz.getMethod("parseFrom", InputStream.class);
                    Object object = method.invoke(null, inputStream);
                    if (clazz.isInstance(object)) {
                        return (T) object;
                    }
                }
            }
        } catch (IOException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        } finally {
            try {
                //关闭流并释放资源
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static void config(HttpPost httpPost) {
        // 配置请求的超时设置
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(TIME_OUT)
                .setConnectTimeout(TIME_OUT).setSocketTimeout(TIME_OUT).build();
        httpPost.setConfig(requestConfig);

        httpPost.addHeader("Connection", "close");
    }
}
