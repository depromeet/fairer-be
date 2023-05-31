package com.depromeet.fairer.repository.feedback;

import com.depromeet.fairer.domain.feedback.Feedback;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.depromeet.fairer.domain.feedback.QFeedback.feedback;
import static com.depromeet.fairer.domain.houseworkComplete.QHouseworkComplete.houseworkComplete;
import static com.depromeet.fairer.domain.member.QMember.member;

@Repository
@RequiredArgsConstructor
@Slf4j
public class FeedbackCustomRepositoryImpl implements FeedbackCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Feedback> getFeedback(Long houseworkCompleteId, Long memberId) {

        return jpaQueryFactory.select(feedback)
                .from(feedback)
                .leftJoin(member).on(member.memberId.eq(feedback.member.memberId))
                .leftJoin(houseworkComplete).on(houseworkComplete.houseWorkCompleteId.eq(feedback.houseworkComplete.houseWorkCompleteId))
                .where(feedback.member.memberId.eq(memberId)
                        .and(feedback.houseworkComplete.houseWorkCompleteId.eq(houseworkCompleteId)))
                .fetch();
    }

}











