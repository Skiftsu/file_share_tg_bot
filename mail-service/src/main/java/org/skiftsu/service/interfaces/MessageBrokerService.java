package org.skiftsu.service.interfaces;

import org.skiftsu.dto.MailDto;

public interface MessageBrokerService {
    void consumeMessage(MailDto msg);
}
