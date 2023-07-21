package com.ioufev.wsforward.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ClientEndpoint()
@Component
public class WebSocketClient {

    private static final Logger log = LoggerFactory.getLogger(WebSocketClient.class);

    /**
     * 计算结果地址
     */
    @Value("${ws.uri:ws://127.0.0.1:8080/websocket/}")
    private String uri;

    private Map<String,Session> map = new ConcurrentHashMap<>();


    @OnMessage public void onMessage(String message) {
        log.info("Client onMessage: " + message);
    }


    @OnClose
    public void onClose() {
        log.info("onOpen.........onClose");
    }

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "projectId") String projectId) {
        log.info("onOpen.........client");
    }

    public void close(String sessionId) {
        try {
            if(map.containsKey(sessionId)) {
                map.get(sessionId).close();
                map.remove(sessionId);
            }
        } catch (Exception ex) {
            log.error("error" + ex);
        }
    }

    public void sendTextMessage(String sessionId, String message) {
        WebSocketContainer container;
        try {
            URI r = URI.create(uri + sessionId);
            container = ContainerProvider.getWebSocketContainer();
            if(!map.containsKey(sessionId)) {
                map.put(sessionId, container.connectToServer(this, r));
            }
            map.get(sessionId).getBasicRemote().sendText(message);
        } catch (DeploymentException | IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            log.error("error" + ex);
        }
    }
}
