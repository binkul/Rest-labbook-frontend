package com.lab.labbook.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
    private Long id;
    private String name;
    private String lastName;
    private String login;
    private String email;
    private String password;
    private boolean blocked;
    private boolean observer;
    private String role;
    private Role roleType;
    private LocalDateTime date;
}
