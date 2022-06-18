package com.depromeet.fairer.vo.team;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class InviteCodeVo {

    private String inviteCode;
    private LocalDateTime inviteCodeExpirationDateTime;
}
