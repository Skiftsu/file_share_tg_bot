package org.skiftsu.service.interfaces;

import org.skiftsu.dto.MailDto;
import org.skiftsu.dto.MessageDto;

public interface MessageBrokerService {
    void consumeMessageUpdates(MessageDto message);
    void sendResponseToController(MessageDto message);
    void sendMessageToMailService(MailDto mail);
}
