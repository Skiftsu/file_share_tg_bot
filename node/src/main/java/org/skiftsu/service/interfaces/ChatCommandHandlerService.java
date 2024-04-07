package org.skiftsu.service.interfaces;

import org.skiftsu.dto.MessageDto;
import org.skiftsu.entity.AppUserEntity;
import org.skiftsu.service.enums.EServiceCommands;

public interface ChatCommandHandlerService {
    String handleCommand(EServiceCommands command, AppUserEntity user, MessageDto message);
}
