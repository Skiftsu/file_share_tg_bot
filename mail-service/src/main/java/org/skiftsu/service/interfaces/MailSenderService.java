package org.skiftsu.service.interfaces;

import org.skiftsu.dto.MailDto;
import org.skiftsu.dto.MailParamsModel;

public interface MailSenderService {
    void send(MailDto msg);
}
