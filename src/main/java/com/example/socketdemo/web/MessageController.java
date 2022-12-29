package com.example.socketdemo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;

import com.example.socketdemo.domain.Greeting;
import com.example.socketdemo.domain.Hello;

@Controller
public class MessageController {

    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/hello")
    public void greeting(Hello msg) throws InterruptedException {
        Thread.sleep(2000);
        Message<Greeting> response = MessageBuilder.withPayload(new Greeting("Hi, " + msg.getName())).build();
        template.convertAndSend("/topic/greetings", response);
        Thread.sleep(2000);
        Message<Greeting> response2 = MessageBuilder.withPayload(new Greeting("Hi again, " + msg.getName())).build();
        template.convertAndSend("/topic/greetings", response2);
    }

}
