package org.skiftsu;

import org.skiftsu.enums.ETelegramMessageType;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public class TelegramUtils {
    public static ETelegramMessageType getMessageType(Message message) {
        if (message.hasText()) {
            return ETelegramMessageType.Text;
        } else if (message.hasDocument()) {
            return ETelegramMessageType.Document;
        } else if (message.hasPhoto()) {
            return ETelegramMessageType.Photo;
        } else {
            return ETelegramMessageType.Other;
        }
    }
}
