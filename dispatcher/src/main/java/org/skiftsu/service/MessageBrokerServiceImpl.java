package org.skiftsu.service;

import lombok.extern.apachecommons.CommonsLog;
import org.skiftsu.RabbitQueue.ERabbitQueue;
import org.skiftsu.controller.TGBotController;
import org.skiftsu.dto.MessageDto;
import org.skiftsu.service.interfaces.MessageBrokerService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.skiftsu.RabbitQueue.ANSWER_MESSAGE_QUEUE;

@Service
@DependsOn("TGBotController")
@CommonsLog
public class MessageBrokerServiceImpl implements MessageBrokerService {
    @Autowired
    @Lazy
    private TGBotController tgBotController;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    @RabbitListener(queues = ANSWER_MESSAGE_QUEUE)
    public void acceptMessageFromRabbit(MessageDto message) {
        log.info("RabbitMQ| Message accepted -> Message: " + message);
        if(message.getMessageType() == MessageDto.EMessageType.Text) {
            tgBotController.sendMessageToChat(message.getChatId(), message.getTextMessage());
        }
    }

    @Override
    public void sendMessageToRabbit(ERabbitQueue rabbitQueue, MessageDto message) {
        log.info("RabbitMQ| Message sent -> Content: " + message);
        var queue = rabbitQueue.getQueue();
        rabbitTemplate.convertAndSend(queue, message);
    }
}
