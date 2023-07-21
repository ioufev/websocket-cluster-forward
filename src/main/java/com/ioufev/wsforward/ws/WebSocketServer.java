package com.ioufev.wsforward.ws;

import com.ioufev.wsforward.consts.RedisConst;
import com.ioufev.wsforward.redis.RedisPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;


import org.springframework.stereotype.Component;

import javax.websocket.server.ServerEndpoint;

@Component
@ServerEndpoint("/websocket/{key}")
public class WebSocketServer {

    private static final Logger log = LoggerFactory.getLogger(WebSocketServer.class);


    private String sessionId;
    private Session session;

    private static RedisPublisher redisPublisher;

    @Autowired
    public void setApplicationContext(RedisPublisher redisPublisher) {
        WebSocketServer.redisPublisher= redisPublisher;
    }


    private static CopyOnWriteArraySet<WebSocketServer> webSockets = new CopyOnWriteArraySet<>();

    private static Map<String, Session> sessionPool = new ConcurrentHashMap<>();


    @OnOpen
    public void onOpen(Session session, @PathParam(value = "key") String key) {

        this.sessionId = key;
        this.session = session;
        webSockets.add(this);
        sessionPool.put(key, session);
        log.info(key + "【websocket消息】有新的连接，总数为:" + webSockets.size() + ", session count is :" + sessionPool.size());
        for(WebSocketServer webSocket : webSockets) {
            log.info("【webSocket】key is :" + webSocket.sessionId);
        }

    }

    /**
     *
     */
    @OnClose
    public void onClose() {
        sessionPool.remove(this.sessionId);
        webSockets.remove(this);
        log.info("【websocket消息】连接断开，总数为:" + webSockets.size());
    }

    @OnMessage
    public void onMessage(@PathParam(value = "key") String pkey, String message) {
        if(pkey.contains("_")){
            log.info("【websocket消息】收到算法端服务消息message:" + message);
//			String key = pkey.split("_")[1];
            String key = pkey.substring(pkey.indexOf("_") + 1);
            sendOneMessage(key, message);
        } else {
            log.info("【websocket消息】收到客户端服务消息:" + message);
        }
    }

    /**
     * 广播消息
     *
     * @param message
     */
    public void sendAllMessage(String message) {
        for (WebSocketServer webSocket : webSockets) {
            log.info("【websocket消息】广播消息:" + message);
            try {
                webSocket.session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 此为单点消息
     *
     * @param key
     * @param message
     */
    public void sendOneMessage(String key, String message) {

//		Session session = sessionPool.get(pkey);
        Session session = getSession(key);
        if (session != null) {
            try {
//				session.getAsyncRemote().sendText(message);
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            redisPublisher.publishMessage(RedisConst.PUB_SUB_TOPIC, (key + "::" + message).getBytes(StandardCharsets.UTF_8));
        }
    }

    /**
     * 用来Redis订阅后使用
     */
    public void sendOneMessageForRedisMessage(String key, String message) {
        Session session = getSession(key);
        if (session != null) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static Session getSession(String key){
        for (WebSocketServer webSocket : webSockets) {
            if(webSocket.sessionId.equals(key)){
                return webSocket.session;
            }
        }
        return null;
    }

}
