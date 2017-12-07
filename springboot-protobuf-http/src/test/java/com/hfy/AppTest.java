package com.hfy;

import com.hfy.proto.PlayerProtos;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testAddPlayer() throws IOException, InterruptedException {
        PlayerProtos.Player player = PlayerProtos.Player.newBuilder().setFullName("测试").build();
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity("/addPlayer", player, String.class);
        Assert.assertEquals("测试", stringResponseEntity.getBody());
    }

    @Test
    public void testAddPlayer1() throws IOException, InterruptedException {
        PlayerProtos.Player player = PlayerProtos.Player.newBuilder().setFullName("测试").build();
        ResponseEntity<PlayerProtos.Player> responseEntity = restTemplate.postForEntity("/addPlayer1", player, PlayerProtos.Player.class);
        Assert.assertEquals("测试", responseEntity.getBody().getFullName());
    }
}
