package com.example.socketdemo.web;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.example.socketdemo.domain.Greeting;
import com.example.socketdemo.domain.Hello;

@Controller
public class MessageController {

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(Hello msg) throws InterruptedException {
        Thread.sleep(2000);
        return new Greeting("Hi, " + msg.getName());
    }

}
