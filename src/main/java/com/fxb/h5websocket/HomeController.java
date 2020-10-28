package com.fxb.h5websocket;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HomeController {

    /**
     * 推送消息到前端
     *
     * @param msg 文本消息内容
     */
    @GetMapping("/broadcast")
    public void broadcast(String msg){
        MyWebSocket.broadcast(msg);
    }
}
