package com.hfy;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@Api("test1的Api")
@RestController
@RequestMapping("test1")
public class HttpController {

    @ApiOperation(value = "获取用户信息", notes = "获取用户信息")
    @GetMapping("getUsers")
    public UserRsp test1() {
        UserRsp rsp = new UserRsp();
        rsp.setName("本人无名");
        return rsp;
    }
}
