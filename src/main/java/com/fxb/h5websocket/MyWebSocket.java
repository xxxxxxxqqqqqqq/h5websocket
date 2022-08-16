package com.fxb.h5websocket;

import com.fxb.h5websocket.config.WebSocketConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * WebSocket实现
 */
@ServerEndpoint(value = "/websocket", configurator = WebSocketConfig.class) //接受websocket请求路径, 鉴权配置
@Component
public class MyWebSocket {


    //保存所有在线socket连接
    private static Map<String,MyWebSocket> webSocketMap = new ConcurrentHashMap<>();

    //记录当前在线数目
    private static AtomicInteger count = new AtomicInteger();

    //当前连接（每个websocket连入都会创建一个MyWebSocket实例
    private Session session;

    private static final Logger log = LoggerFactory.getLogger(MyWebSocket.class);
    //处理连接建立
    @OnOpen
    public void onOpen(Session session){
        this.session=session;
        webSocketMap.put(session.getId(),this);
        addCount();
        log.info("新的连接加入：{}",session.getId());
    }

    //接受消息
    @OnMessage
    public void onMessage(String message,Session session){
        log.info("收到客户端{}消息：{}",session.getId(),message);
        try{
            this.sendMessage("收到消息："+message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //处理错误
    @OnError
    public void onError(Throwable error,Session session){
        log.info("发生错误{},{}",session.getId(),error.getMessage());
    }

    //处理连接关闭
    @OnClose
    public void onClose(){
        webSocketMap.remove(this.session.getId());
        reduceCount();
        log.info("连接关闭:{}",this.session.getId());
    }

    //发送消息
    private void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    //遍历所有链接广播消息
    public static void broadcast(String msg){
        MyWebSocket.webSocketMap.forEach((k,v)->{
            try{
                v.sendMessage(msg);
            }catch (Exception ignored){
            }
        });
    }

    //获取在线连接数目
    public static int getCount(){
        return count.get();
    }

    //操作count，使用synchronized确保线程安全
    private static synchronized void addCount(){
        count.incrementAndGet();
    }

    private static synchronized void reduceCount(){
        count.decrementAndGet();
    }
}

