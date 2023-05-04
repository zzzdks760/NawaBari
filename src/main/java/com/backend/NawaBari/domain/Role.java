package com.backend.NawaBari.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    GUEST("ROLE_GUEST"), MEMBER("ROLE_MEMBER");

    private final String key;
}