package com.deathcode.sqs.controller;

import lombok.extern.flogger.Flogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/sqs/v1/message")
@Slf4j
public class SQSController {

    @Autowired
    private QueueMessagingTemplate messagingTemplate;
    @Value("${cloud.aws.sqs.endpoint}")
    private String sqsEndpoint;

    @PostMapping(path = "/sendMessage")
    public String sendMessage(@RequestBody String message)
    {
        messagingTemplate.send(sqsEndpoint, MessageBuilder.withPayload(message).build());
        return "Message Sent";
    }
    
    @GetMapping(path = "/health")
    public String healthCheck()
    {
        return "Application is Running Fine";
    }

    @SqsListener(value = "AWS_SQS_Practice")
    public void loadMessageFromQueue(String message)
    {
       log.info("The Message Received : \" {} \"",message);
    }


}
