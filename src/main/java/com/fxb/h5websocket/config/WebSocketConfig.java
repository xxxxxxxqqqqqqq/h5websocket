package com.fxb.h5websocket.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.util.Objects;

@Configuration
public class WebSocketConfig extends ServerEndpointConfig.Configurator{

    private static final Logger log = LoggerFactory.getLogger(WebSocketConfig.class);


    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }

    /**
     * token鉴权认证
     * @param originHeaderValue originHeaderValue
     * @return 鉴权结果
     */
    @Override
    public boolean checkOrigin(String originHeaderValue) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String token = request.getParameter("token");
        // 测试token 123
        return Objects.equals(token, "123");
    }
}
