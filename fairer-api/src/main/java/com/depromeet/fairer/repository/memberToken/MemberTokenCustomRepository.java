package com.depromeet.fairer.repository.memberToken;

import java.time.LocalDateTime;

public interface MemberTokenCustomRepository {

    void updateExpirationTimeByMemberId(Long memberId, LocalDateTime expirationTime);
}
