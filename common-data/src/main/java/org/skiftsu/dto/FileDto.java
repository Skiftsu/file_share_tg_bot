package org.skiftsu.dto;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@ToString
public class FileDto {
    private String url;
    private String name;
    private Long size;
    private String type;
}
