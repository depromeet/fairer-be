package com.depromeet.fairer.dto.member.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class GoogleUserInfo {
    @JsonProperty("id")
    private String id;

    @JsonProperty("email")
    private String email;

    @JsonProperty("verified_email")
    private boolean verified_email;

    @JsonProperty("name")
    private String name;

    @JsonProperty("given_name")
    private String given_name;

    @JsonProperty("family_name")
    private String family_name;

    @JsonProperty("picture")
    private String picture;

    @JsonProperty("locale")
    private String locale;
}
