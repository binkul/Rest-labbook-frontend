package com.lab.labbook.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LogDto {
    private Long id;
    private LocalDateTime date;
    private String level;
    private String log;
    private String comments;
}
