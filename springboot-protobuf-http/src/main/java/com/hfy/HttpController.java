package com.hfy;

import com.hfy.proto.ImageTest;
import com.hfy.proto.PlayerProtos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@RestController
public class HttpController {

    @Autowired
    private ImageRepository imageRepository;


    @PostMapping("/uploadImg")
    void uploadImg(HttpServletRequest request) {
        try {
            ImageTest.Data data = ImageTest.Data.parseFrom(request.getInputStream());
            imageRepository.uploadImage(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/addPlayer")
    String addPlayer(HttpServletRequest request) {
        try {
            PlayerProtos.Player player = PlayerProtos.Player.parseFrom(request.getInputStream());
            System.out.println(player);
            System.out.println(player.getFullName());
            return player.getFullName();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用httpclient测试，需要设置content-Type
     * httppost.addHeader("Content-Type", "application/x-protobuf;charset=utf-8");
     * @param player
     */
    @PostMapping(value = "/addPlayer1")
//    @PostMapping(value = "/addPlayer1", consumes = {"application/x-protobuf"}, produces = "application/x-protobuf")
    PlayerProtos.Player addPlayer1(@RequestBody PlayerProtos.Player player) {
        System.out.println(player.getFullName());
        return player;
    }
}
