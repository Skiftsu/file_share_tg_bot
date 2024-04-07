package org.skiftsu.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class MailParamsModel {
    private String id;
    private String emailTo;
}
