package org.skiftsu.service;

import lombok.extern.apachecommons.CommonsLog;
import org.skiftsu.dto.MessageDto;
import org.skiftsu.dto.UserDto;
import org.skiftsu.repository.AppUserRepository;
import org.skiftsu.entity.AppUserEntity;
import org.skiftsu.service.enums.EUserState;
import org.skiftsu.service.enums.EServiceCommands;
import org.skiftsu.service.interfaces.MainService;
import org.skiftsu.service.interfaces.MessageBrokerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@CommonsLog
public class MainServiceImpl implements MainService {
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    @Lazy
    private MessageBrokerService messageBrokerService;
    @Autowired
    private ChatCommandHandlerServiceImpl chatCommandHandlerService;
    @Override
    public void processMessage(MessageDto message) {
        var user = findOrSaveAppUser(message.getUser());

        var result = chatCommandHandlerService.handleCommand(EServiceCommands.textToEnum(message.getTextMessage()), user, message);
        sendAnswer(message.getChatId(), result);
    }

    private void sendAnswer(Long chatId, String text) {
        var message = new MessageDto(chatId, null);
        message.setTextMessage(text);
        messageBrokerService.sendResponseToController(message);
    }

    private AppUserEntity findOrSaveAppUser(UserDto user) {
        var persistentAppUser = appUserRepository.findByTelegramUserId(user.getUserId());
        if (persistentAppUser.isPresent()) {
            return persistentAppUser.get();
        }

        AppUserEntity transientAppUser = AppUserEntity.builder()
                .telegramUserId(user.getUserId())
                .username(user.getUserUsername())
                .isActive(false)
                .state(EUserState.WAIT_FOR_EMAIL_STATE)
                .build();
        log.info("Postgres| User saved -> Username: " + user.getUserUsername());
        return appUserRepository.save(transientAppUser);
    }
}
