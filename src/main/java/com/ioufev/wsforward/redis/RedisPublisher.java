package com.ioufev.wsforward.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RedisPublisher {

    @Resource
    private RedisTemplate<String, byte[]> redisTemplate;

    public void publishMessage(String channel, byte[] message) {
        redisTemplate.convertAndSend(channel, message);
    }

}
