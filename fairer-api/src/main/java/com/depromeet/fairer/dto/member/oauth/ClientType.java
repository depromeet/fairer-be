package com.depromeet.fairer.dto.member.oauth;

public enum ClientType {
    ANDROID("ANDROID"),
    IOS("IOS");

    private String value;

    ClientType(String value) {
        this.value = value;
    }
}
