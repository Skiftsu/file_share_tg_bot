package org.skiftsu.service.interfaces;

import org.skiftsu.RabbitQueue.ERabbitQueue;
import org.skiftsu.dto.MessageDto;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface MessageBrokerService {
    void acceptMessageFromRabbit(MessageDto message);
    void sendMessageToRabbit(ERabbitQueue rabbitQueue, MessageDto message);
}
