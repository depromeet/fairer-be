package com.depromeet.fairer.repository.housework;

import com.depromeet.fairer.domain.assignment.Assignment;
import com.depromeet.fairer.domain.housework.HouseWork;
import com.depromeet.fairer.domain.housework.constant.RepeatCycle;
import com.depromeet.fairer.domain.member.Member;
import com.depromeet.fairer.domain.team.Team;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.depromeet.fairer.domain.assignment.QAssignment.assignment;
import static com.depromeet.fairer.domain.housework.QHouseWork.houseWork;
import static com.depromeet.fairer.domain.houseworkComplete.QHouseworkComplete.houseworkComplete;
import static com.depromeet.fairer.domain.member.QMember.member;

@Repository
@RequiredArgsConstructor
@Slf4j
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

    @Override
    public List<HouseWork> getCycleHouseWork(LocalDate fromDate, LocalDate toDate, Long memberId) {

        return new ArrayList<>(jpaQueryFactory.selectFrom(houseWork)
                .innerJoin(houseWork.assignments, assignment)
                .innerJoin(assignment.member, member)
                .where(((houseWork.repeatCycle.eq(RepeatCycle.ONCE)
                        .and(houseWork.scheduledDate.between(fromDate, toDate)))
                        .and(member.memberId.eq(memberId)))

                        .or((houseWork.repeatCycle.eq(RepeatCycle.EVERY)
                                .and(houseWork.scheduledDate.loe(toDate))
                                .and(houseWork.repeatEndDate.goe(fromDate)))
                                .and(member.memberId.eq(memberId)))

                        .or((houseWork.repeatCycle.eq(RepeatCycle.WEEKLY)
                                .and(houseWork.scheduledDate.loe(toDate))
                                .and(houseWork.repeatEndDate.goe(fromDate)))
                                .and(member.memberId.eq(memberId)))

                        .or((houseWork.repeatCycle.eq(RepeatCycle.MONTHLY)
                                .and(houseWork.scheduledDate.loe(toDate))
                                .and(houseWork.repeatEndDate.goe(fromDate)))
                                .and(member.memberId.eq(memberId)))
                )
                .fetch());
    }

    @Override
    public List<HouseWork> getCycleHouseWorkByTeam(LocalDate fromDate, LocalDate toDate, Team team) {

        return new ArrayList<>(jpaQueryFactory.selectFrom(houseWork)
                .innerJoin(houseWork.assignments, assignment)
                .innerJoin(assignment.member, member)
                .where(((houseWork.repeatCycle.eq(RepeatCycle.ONCE)
                        .and(houseWork.scheduledDate.between(fromDate, toDate)))
                        .and(houseWork.team.eq(team)))

                        .or((houseWork.repeatCycle.eq(RepeatCycle.EVERY)
                                .and(houseWork.scheduledDate.loe(toDate))
                                .and(houseWork.repeatEndDate.goe(fromDate)))
                                .and(houseWork.team.eq(team)))

                        .or((houseWork.repeatCycle.eq(RepeatCycle.WEEKLY)
                                .and(houseWork.scheduledDate.loe(toDate))
                                .and(houseWork.repeatEndDate.goe(fromDate)))
                                .and(houseWork.team.eq(team)))

                        .or((houseWork.repeatCycle.eq(RepeatCycle.MONTHLY)
                                .and(houseWork.scheduledDate.loe(toDate))
                                .and(houseWork.repeatEndDate.goe(fromDate)))
                                .and(houseWork.team.eq(team)))
                )
                .fetch());
    }

}