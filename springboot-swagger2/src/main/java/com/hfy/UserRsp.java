package com.hfy;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by hfy on 2017/12/4.
 */
@ApiModel("用户信息")
public class UserRsp {

    @ApiModelProperty(value = "姓名", required = true)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
