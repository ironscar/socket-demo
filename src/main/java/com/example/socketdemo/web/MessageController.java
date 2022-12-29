package com.example.socketdemo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;

import com.example.socketdemo.domain.Greeting;
import com.example.socketdemo.domain.Hello;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MessageController {

    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/hello")
    public void greeting(@Header("simpSessionId") String sessionId, Hello msg) throws InterruptedException {
        log.info("session id = " + sessionId);
        Thread.sleep(2000);
        Message<Greeting> response = MessageBuilder.withPayload(new Greeting("Hi, " + msg.getName())).build();
        template.convertAndSend("/topic/greetings", response);
        Thread.sleep(2000);
        Message<Greeting> response2 = MessageBuilder.withPayload(new Greeting("Hi again, " + msg.getName())).build();
        template.convertAndSend("/topic/greetings", response2);
    }

}

/**
 * If there are multiple instances of this app
 * And if a socket is established
 * And I start an async long-running job that keeps sending message to client
 * And then client sends a STOP message to the same socket
 * Does it mean that the client will definitely hit the same instance and so I can manage it in app-memory
 * Or do I need to assume that the job may be running in a different instance
 */
