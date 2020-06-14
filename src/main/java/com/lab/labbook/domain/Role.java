package com.lab.labbook.domain;

import com.lab.labbook.exception.RoleNotFoundException;

import java.util.stream.Stream;

public enum Role {
    DEFAULT,
    USER,
    MODERATOR,
    ADMIN;

    public static Role findRole(String role) {
        return Stream.of(Role.values())
                .filter(i -> i.name().equals(role))
                .findFirst()
                .orElseThrow(() -> new RoleNotFoundException("Role '" + role + "' was not found"));
    }
}
