package com.depromeet.fairer.repository.memberToken;

import com.depromeet.fairer.domain.memberToken.QMemberToken;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Repository
public class MemberTokenCustomRepositoryImpl implements MemberTokenCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public void updateExpirationTimeByMemberId(Long memberId, LocalDateTime expirationTime) {
        jpaQueryFactory.update(QMemberToken.memberToken)
                .where(QMemberToken.memberToken.memberId.eq(memberId))
                .set(QMemberToken.memberToken.tokenExpirationTime, expirationTime)
                .execute();
    }
}
