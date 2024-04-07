package org.skiftsu.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class MailDto {
    private String receiverEmail;
    private String messageName;
    private String text;
}
