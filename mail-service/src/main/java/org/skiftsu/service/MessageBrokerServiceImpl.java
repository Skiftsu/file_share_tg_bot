package org.skiftsu.service;

import lombok.extern.apachecommons.CommonsLog;
import org.skiftsu.dto.MailDto;
import org.skiftsu.service.interfaces.MailSenderService;
import org.skiftsu.service.interfaces.MessageBrokerService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.skiftsu.RabbitQueue.EMAIL_MESSAGE_QUEUE;

@Service
@CommonsLog
public class MessageBrokerServiceImpl implements MessageBrokerService {
    @Autowired
    MailSenderService mailSenderService;

    @Override
    @RabbitListener(queues = EMAIL_MESSAGE_QUEUE)
    public void consumeMessage(MailDto msg) {
        log.info("RabbitMQ| Message accept. Content: " + msg);
        try {
            mailSenderService.send(msg);
        } catch (Exception e) {
            log.error("Error: " + e);
        }

    }
}
