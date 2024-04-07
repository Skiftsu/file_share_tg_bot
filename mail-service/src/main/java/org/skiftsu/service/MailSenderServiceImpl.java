package org.skiftsu.service;

import lombok.extern.apachecommons.CommonsLog;
import org.skiftsu.dto.MailDto;
import org.skiftsu.dto.MailParamsModel;
import org.skiftsu.service.interfaces.MailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@CommonsLog
@Service
public class MailSenderServiceImpl implements MailSenderService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String emailFrom;

    @Override
    public void send(MailDto msg) {
        var mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(emailFrom);
        mailMessage.setTo(msg.getReceiverEmail());
        mailMessage.setSubject(msg.getMessageName());
        mailMessage.setText(msg.getText());

        javaMailSender.send(mailMessage);
    }
}
