package com.hfy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 基础配置
 * Created by cage on 2017/2/13.
 */
@Component
@ConfigurationProperties
public class BasicProperties {

    @Value("${swagger.show}")       // 是否显示swagger文档
    private boolean swaggerShow;
    @Value("${swagger.patterns}")   // 显示指定的api
    private String[] swaggerPatterns;

    public boolean isSwaggerShow() {
        return swaggerShow;
    }

    public void setSwaggerShow(boolean swaggerShow) {
        this.swaggerShow = swaggerShow;
    }

    public String[] getSwaggerPatterns() {
        return swaggerPatterns;
    }

    public void setSwaggerPatterns(String[] swaggerPatterns) {
        this.swaggerPatterns = swaggerPatterns;
    }
}
