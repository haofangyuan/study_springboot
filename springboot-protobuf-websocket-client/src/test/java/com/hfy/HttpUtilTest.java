package com.hfy;

import com.hfy.network.bean.UserProtos;
import com.mycompany.demo.PlayerProtos;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

/**
 * HttpUtil Tester.
 *
 * @author hfy
 * @version 1.0
 * @since 12/05/2017
 */
public class HttpUtilTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: postPb(String url, byte[] bytes, Class<T> clazz)
     */
    @Test
    public void testPostPb() throws Exception {
        String url = "http://127.0.0.1:8888/user";
        UserProtos.User user = UserProtos.User.newBuilder().setName("测试").build();

        UserProtos.User userRsp = HttpUtil.postPb(url, user.toByteArray(), UserProtos.User.class);
        Assert.assertEquals("测试", userRsp.getName());


    }


}
