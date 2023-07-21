package com.ioufev.wsforward.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ThymeleafController {
    @GetMapping("/ws")
    public String ws(){
        return "ws-demo";
    }
}
