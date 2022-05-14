package com.depromeet.fairer.repository.housework;

import com.depromeet.fairer.domain.housework.HouseWork;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.depromeet.fairer.domain.assignment.QAssignment.assignment;
import static com.depromeet.fairer.domain.housework.QHouseWork.houseWork;
import static com.depromeet.fairer.domain.member.QMember.member;

@Repository
@RequiredArgsConstructor
public class HouseWorkCustomRepositoryImpl implements HouseWorkCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Long getHouseWorkSuccessCount(Long memberId, LocalDate startDate, LocalDate endDate) {
        return jpaQueryFactory.selectFrom(houseWork)
                .innerJoin(houseWork.assignments, assignment)
                .innerJoin(assignment.member, member)
                .where(houseWork.scheduledDate.goe(startDate)
                        .and(houseWork.scheduledDate.loe(endDate))
                        .and(member.memberId.eq(memberId))
                        .and(houseWork.success.eq(true)))
                .stream()
                .count();
    }

//    @Override
//    public List<HouseWork> getHouseWorkList(LocalDate scheduledDate, Long memberId) {
//        return jpaQueryFactory.selectFrom(houseWork)
//                .innerJoin(houseWork.assignments, assignment)
//                .innerJoin(assignment.member, member)
//                .where(houseWork.scheduledDate.eq(scheduledDate)
//                        .and(assignment.member.memberId.eq(memberId)))
//
//    }
}