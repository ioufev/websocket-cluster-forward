package com.ioufev.wsforward.controller;

import com.ioufev.wsforward.config.WebSocketClient;
import com.ioufev.wsforward.model.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/compute")
public class ComputeController {

     @Resource
     WebSocketClient webSocketClient;

     @PostMapping("/square")
     public Result<Integer> compute(@RequestBody Map<String, Object> requestBody) {
          Integer param = (Integer) requestBody.get("param");
          String session = (String) requestBody.get("session");

          int result = param * param;
          webSocketClient.sendTextMessage(session, "计算结果：" + result);
          return Result.ofSuccess(result);
          // return Result.ofFail(50001,"服务端发生了错误");
     }
}
