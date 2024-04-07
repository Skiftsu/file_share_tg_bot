package org.skiftsu.service;

import lombok.extern.apachecommons.CommonsLog;
import org.skiftsu.dto.MailDto;
import org.skiftsu.dto.MessageDto;
import org.skiftsu.service.interfaces.MessageBrokerService;
import org.skiftsu.service.interfaces.MainService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import static org.skiftsu.RabbitQueue.*;


@CommonsLog
@Service
public class MessageBrokerServiceImpl implements MessageBrokerService {
    @Autowired
    @Lazy
    private MainService mainService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    @RabbitListener(queues = {TEXT_MESSAGE_QUEUE, FILE_MESSAGE_QUEUE})
    public void consumeMessageUpdates(MessageDto message) {
        log.info("RabbitMQ| Message accepted -> Content: " + message);
        try {
            mainService.processMessage(message);
        }
        catch (Exception e) {
            log.error("Message processing error: " + e);
            var errorMessage = new MessageDto(message.getChatId());
            errorMessage.setTextMessage("Ошибка обработки сообщения! Номер ошибки: 2");
            sendResponseToController(errorMessage);
        }
    }

    @Override
    public void sendResponseToController(MessageDto message) {
        log.info("RabbitMQ| Answer sent -> Message: " + message);
        var queue = ERabbitQueue.Answer.getQueue();
        rabbitTemplate.convertAndSend(queue, message);
    }

    @Override
    public void sendMessageToMailService(MailDto mail) {
        log.info("RabbitMQ| Mail sent -> Message: " + mail);
        var queue = ERabbitQueue.Email.getQueue();
        rabbitTemplate.convertAndSend(queue, mail);
    }
}
