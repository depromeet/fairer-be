package com.depromeet.fairer.repository.houseworkcomplete;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static com.depromeet.fairer.domain.member.QMember.member;
import static com.depromeet.fairer.domain.housework.QHouseWork.houseWork;
import static com.depromeet.fairer.domain.houseworkComplete.QHouseworkComplete.houseworkComplete;
import static com.depromeet.fairer.domain.assignment.QAssignment.assignment;

import static com.depromeet.fairer.domain.team.QTeam.team;

@Repository
@RequiredArgsConstructor
public class HouseWorkCompleteCustomRepositoryImpl implements HouseWorkCompleteCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    public List<Tuple> getTeamHouseWorkStatisticPerMonthByTeamIdAndHouseWorkName(Long teamId, int year, int month, String houseWorkName) {
        int firstDateOfMonth = 1;
        int startHour = 0;
        int startMinute = 0;
        int startSecond = 0;
        int nextMonth = (month + 1) % 12;

        if (nextMonth == 0) {
            nextMonth = 12;
        }

        LocalDateTime startDateTimeOfMonth = LocalDateTime.of(
                year,
                Month.of(month),
                firstDateOfMonth,
                startHour,
                startMinute,
                startSecond
        );
        LocalDateTime endDateTimeOfMonth = startDateTimeOfMonth.withMonth(nextMonth).minusNanos(1);

        return jpaQueryFactory.select(member, member.count())
                .from(houseworkComplete)
                .leftJoin(houseWork).on(houseWork.houseWorkId.eq(houseworkComplete.houseWork.houseWorkId))
                .leftJoin(assignment).on(houseWork.houseWorkId.eq(assignment.houseWork.houseWorkId))
                .leftJoin(member).on(assignment.member.memberId.eq(member.memberId))
                .leftJoin(team).on(member.team.teamId.eq(team.teamId))
                .where(team.teamId.eq(teamId),
                        houseworkComplete.successDateTime.between(startDateTimeOfMonth, endDateTimeOfMonth),
                        houseworkNameEq(houseWorkName))
                .groupBy(member)
                .orderBy(member.count().desc())
                .fetch();
    }

    private BooleanExpression houseworkNameEq(String houseworkName) {
        return houseworkName != null? houseWork.houseWorkName.eq(houseworkName) : null;
    }
}
