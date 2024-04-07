package org.skiftsu.service;

import jakarta.transaction.Transactional;
import org.skiftsu.dto.MailDto;
import org.skiftsu.dto.MessageDto;
import org.skiftsu.entity.AppDocumentEntity;
import org.skiftsu.entity.AppUserEntity;
import org.skiftsu.repository.AppDocumentRepository;
import org.skiftsu.repository.AppUserRepository;
import org.skiftsu.service.enums.EServiceCommands;
import org.skiftsu.service.enums.EUserState;
import org.skiftsu.service.interfaces.ChatCommandHandlerService;
import org.skiftsu.service.interfaces.FileService;
import org.skiftsu.service.interfaces.MessageBrokerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.Objects;
import java.util.Random;


@Service
public class ChatCommandHandlerServiceImpl implements ChatCommandHandlerService {
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private MessageBrokerService messageBrokerService;
    @Autowired
    private FileService fileService;
    @Autowired
    private AppDocumentRepository appDocumentRepository;

    @Override
    public String handleCommand(EServiceCommands command, AppUserEntity user, MessageDto message) {
        var userState = user.getState();
        switch (userState) {
            case BASIC_STATE -> {
                return newUserState(command, user, message);
            }
            case WAIT_FOR_EMAIL_STATE -> {
                return waitEmailState(command, user, message);
            }
            case REGISTRAITED -> {
                return registraitedState(command, user, message);
            }
        }

        return "Ошибка 1";
    }

    private String newUserState(EServiceCommands command, AppUserEntity user, MessageDto message) {
        switch (command) {
            case NULL -> {
                return error();
            }
            case HELP -> {
                return helpNewUser();
            }
            case START -> {
                return welcomeNewUser();
            }
            case REGISTRATION -> {
                user.setState(EUserState.WAIT_FOR_EMAIL_STATE);
                appUserRepository.save(user);
                return "Введите почту: ";
            }
        }
        return "Ошибка 1";
    }

    private String waitEmailState(EServiceCommands command, AppUserEntity user, MessageDto message) {
            if(user.getEmail() == null || user.getEmail().isEmpty()){
                try {
                    var email = message.getTextMessage();
                    var internetAddress = new InternetAddress(email);
                    internetAddress.validate();
                    user.setEmail(email);
                    Integer verificationCode = new Random().nextInt(1000);
                    user.setAdditionalInfo(verificationCode.toString());
                    appUserRepository.save(user);

                    return sendVerificationCode(email, verificationCode.toString());
                } catch (AddressException e) {
                    return "Неправильная почта";
                }
            } else {
                if (Objects.equals(message.getTextMessage(), "почта")) {
                    return sendVerificationCode(user.getEmail(), user.getAdditionalInfo());
                }

                if(Integer.parseInt(message.getTextMessage()) == Integer.parseInt(user.getAdditionalInfo())) {
                    user.setState(EUserState.REGISTRAITED);
                    user.setIsActive(true);
                    appUserRepository.save(user);
                    return "Почта подтверждена. Введите /help для что бы узнать новые команды.";
                }
                return "Неправильный код. Для отправки нового письма напишите в чат: почта";
            }
    }

    private String sendVerificationCode(String email, String code) {
        var emailMsg = new MailDto(email, "Подтверждение почты для TG бота", code);
        messageBrokerService.sendMessageToMailService(emailMsg);
        return "На вашу почту отправлен код для подтверждения. Отправьте его в чат.";
    }

    private String registraitedState(EServiceCommands command, AppUserEntity user, MessageDto message) {
        if(message.getMessageType() == MessageDto.EMessageType.File) {
            return processFileMessage(user, message);
        }

        switch (command) {
            case HELP -> {
                return helpRegistratedUser();
            }
            case START -> {
                return welcomeRegistratedUser();
            }
            case REGISTRATION -> {
                user.setState(EUserState.WAIT_FOR_EMAIL_STATE);
                appUserRepository.save(user);
                return "Введите почту: ";
            }
            default -> {
                return error();
            }
        }
    }

    private String processFileMessage(AppUserEntity user, MessageDto message) {

        var fileMsg = message.getFileMessage();
        var filePath = fileService.saveFile(fileMsg, message.getUser().getUserId().toString());

        var appDocument = AppDocumentEntity.builder()
                .tgUrl(fileMsg.getUrl())
                .fsPath(filePath)
                .name(fileMsg.getName())
                .fileSize(fileMsg.getSize())
                .type(fileMsg.getType())
                .fileOwner(user)
                .build();

        appDocumentRepository.save(appDocument);

        user.getUserFiles().add(appDocument);
        appUserRepository.save(user);

        return ("Файл загружен. Имя файла: " + fileMsg.getName() + " Дата загрузки: " + appDocument.getSaveDate());
    }

    private String error() {
        return """
                Такой команды не существует!
                /help - все доступные вам команды
                """;
    }
    private String helpNewUser() {
        return """
                Команды для незарегистрированного пользователя:
                /help - список команд
                /start - начать
                /registration - зарегистрировать пользователя
                """;
    }

    private String welcomeNewUser() {
        return """
                Это бот для хранения и передачи файлов и картинок.
                Список всех команд: /help
                
                Что бы использовать бота нужно зарегистрироваться /registration
                """;
    }

    private String welcomeRegistratedUser() {
        return """
                Это бот для хранения и передачи файлов и картинок.
                Список всех команд: /help
                
                Отправьте в чат документ или фото и сможете им поделиться с другими.
                """;
    }

    private String helpRegistratedUser(){
        return """
                Команды для зарегистрированного пользователя:
                /help - список команд
                /start - начать
                /list - список всех моих файлов
                /get код_документ - получить документ другого пользователя
                """;
    }
}
