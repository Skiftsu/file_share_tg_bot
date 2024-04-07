package org.skiftsu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Getter
@Setter
public class UserDto {
    private Long userId;
    private String userUsername;
}
