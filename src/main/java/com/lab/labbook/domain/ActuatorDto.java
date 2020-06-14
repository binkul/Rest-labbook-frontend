package com.lab.labbook.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ActuatorDto {
    private String name;
    private String author;
    private String version;
    private String description;
    private String extension;
    private String path;
}
