package top.kkuily.xingbackend.web.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.kkuily.xingbackend.model.po.Message;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 * @author 小K
 * @description Websocket服务端处理
 */
@Slf4j
@Component
@ServerEndpoint(value = "/chat/{username}")
public class ChatEndpoint {
    @OnOpen
    public void onOpen(@PathParam("username") String username) {
        log.info("连接的人是： {}", username);
    }

    @OnMessage
    public void onMessage(Session session, Message message) {
        log.info("收到消息：{}", message);
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("close.");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
    }

}