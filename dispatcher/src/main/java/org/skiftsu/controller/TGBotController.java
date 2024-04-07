package org.skiftsu.controller;

import lombok.extern.apachecommons.CommonsLog;
import org.skiftsu.RabbitQueue.ERabbitQueue;
import org.skiftsu.TelegramUtils;
import org.skiftsu.dto.MessageDto;
import org.skiftsu.dto.UserDto;
import org.skiftsu.enums.ETelegramMessageType;
import org.skiftsu.service.MessageBrokerServiceImpl;
import org.skiftsu.service.interfaces.TGFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@CommonsLog
public class TGBotController extends TelegramLongPollingBot {
    @Value("${bot.name}")
    private String botUsername;

    @Value("${bot.token}")
    private String botToken;

    @Autowired
    @Lazy
    private MessageBrokerServiceImpl messageBrokerService;

    @Autowired
    TGFileService tgFileService;

    @Override
    public String getBotUsername() {
       return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update == null) {
            log.error("TG| Received update is null");
            return;
        }

        var message = update.getMessage();
        // Обработка только новых сообщений
        if (message == null) return;

        distributeMessageByType(message);
    }

    public void sendMessageToChat(Long chatId, String text) {
        var message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void distributeMessageByType(Message tgMsg) {
        var messageType = TelegramUtils.getMessageType(tgMsg);
        if (messageType == ETelegramMessageType.Other) {
            sendMessageToChat(tgMsg.getChatId(), "Неподдерживаемый тип сообщения");
            log.info("TG| Unsupported message type");
            return;
        }
        log.info("TG| Message accepted -> Message type: " + messageType.toString() + " -> Message text: " + tgMsg.getText());

        var tgUser = tgMsg.getFrom();
        var user = new UserDto(tgUser.getId(), tgUser.getUserName());
        var message = new MessageDto(tgMsg.getChatId(), user);

        switch (TelegramUtils.getMessageType(tgMsg)){
            case Text -> {
                message.setTextMessage(tgMsg.getText());
                messageBrokerService.sendMessageToRabbit(ERabbitQueue.Text, message);
            }
            case Document -> {
                var docFile = tgFileService.getDocumentFile(tgMsg);
                docFile.setName(tgMsg.getDocument().getFileName());

                message.setFileMessage(docFile);
                messageBrokerService.sendMessageToRabbit(ERabbitQueue.File, message);
            }
            case Photo -> {
                message.setFileMessage(tgFileService.getPhotoFile(tgMsg));
                messageBrokerService.sendMessageToRabbit(ERabbitQueue.File, message);
            }
        }
    }
}
