package com.depromeet.fairer.dto.member.oauth;

import com.depromeet.fairer.domain.member.constant.SocialType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
public class OAuthAttributes {

    private Map<String, Object> attributes;
    private String nameAttributesKey;
    private String name;
    private String email;
    private SocialType socialType;
    private String password;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributesKey, String name, String email, SocialType socialType, String password) {
        this.attributes = attributes;
        this.nameAttributesKey = nameAttributesKey;
        this.name = name;
        this.email = email;
        this.socialType = socialType;
        this.password = password;
    }
}
