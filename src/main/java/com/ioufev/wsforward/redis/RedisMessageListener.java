package com.ioufev.wsforward.redis;

import com.ioufev.wsforward.consts.RedisConst;
import com.ioufev.wsforward.ws.WebSocketServer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class RedisMessageListener implements MessageListener {

    @Resource
    private WebSocketServer webSocket;

    public RedisMessageListener(WebSocketServer webSocket) {
        this.webSocket = webSocket;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {

        // 获取频道名称
        String channel = new String(message.getChannel());

        // 判断是否为需要转发的频道
        if(channel.equals(RedisConst.PUB_SUB_TOPIC)){

            // 获取频道内容
            byte[] body = message.getBody();
            String contentBase64WithQuotes = new String(body, StandardCharsets.UTF_8); // 带引号的Base64
            String contentBase64 = removeQuotes(contentBase64WithQuotes); // base64
            String content = new String(Base64.getDecoder().decode(contentBase64), StandardCharsets.UTF_8); // 原来的字符串

            String key = content.split("::")[0];
            String wsContent  = content.substring((key + "::").length());
            webSocket.sendOneMessageForRedisMessage(key, wsContent);
        }

    }

    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory factory,
                                                   RedisMessageListener listener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        container.addMessageListener(listener, new ChannelTopic(RedisConst.PUB_SUB_TOPIC));
        return container;
    }

    /**
     * 移除存在Redis中的值开头和结尾的引号
     * @param input 输入
     * @return 输出
     */
    private String removeQuotes(String input) {
        if (input != null && input.length() >= 2 && input.startsWith("\"") && input.endsWith("\"")) {
            return input.substring(1, input.length() - 1);
        }
        return input;
    }

}
