package org.skiftsu.dto;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@ToString
public class MessageDto {
    public enum EMessageType {
        Text,
        File,
        None
    }

    private Long chatId;
    private UserDto user;
    private String textMessage;
    private FileDto fileMessage;

    public MessageDto(Long chatId, UserDto user){
        this.chatId = chatId;
        this.user = user;
    }
    public MessageDto(Long chatId){
        this.chatId = chatId;
    }
    public EMessageType getMessageType() {
        // File Priority
        if(fileMessage != null) return EMessageType.File;
        if(textMessage != null || !textMessage.isEmpty()) return EMessageType.Text;
        return EMessageType.None;
    }
}
