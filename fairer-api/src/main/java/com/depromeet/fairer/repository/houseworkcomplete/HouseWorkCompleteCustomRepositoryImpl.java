package com.depromeet.fairer.repository.houseworkcomplete;

import com.depromeet.fairer.vo.houseWorkComplete.HouseWorkCompleteStatisticsVo;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.*;
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

    public List<HouseWorkCompleteStatisticsVo> findMonthlyHouseWorkStatisticByTeamIdAndHouseWorkName(Long teamId, YearMonth month, String houseWorkName) {
        LocalDateTime startTimeOfMonth = month.atDay(1) .atStartOfDay();
        LocalDateTime endTimeOfMonth = month.atEndOfMonth().atTime(LocalTime.MAX);

        return jpaQueryFactory.select(
                    Projections.fields(
                            HouseWorkCompleteStatisticsVo.class,
                            member.as("member"),
                            member.count().as("completeCount")
                    )
                )
                .from(houseworkComplete)
                .leftJoin(houseWork).on(houseWork.houseWorkId.eq(houseworkComplete.houseWork.houseWorkId))
                .leftJoin(assignment).on(houseWork.houseWorkId.eq(assignment.houseWork.houseWorkId))
                .leftJoin(member).on(assignment.member.memberId.eq(member.memberId))
                .leftJoin(team).on(member.team.teamId.eq(team.teamId))
                .where(team.teamId.eq(teamId),
                        houseworkComplete.successDateTime.between(startTimeOfMonth, endTimeOfMonth),
                        houseworkNameEq(houseWorkName))
                .groupBy(member)
                .orderBy(member.count().desc())
                .fetch();
    }

    @Override
    public List<HouseWorkCompleteStatisticsVo> findMonthlyHouseWorkRanking(Long teamId, YearMonth month) {

        LocalDateTime startTimeOfMonth = month.atDay(1) .atStartOfDay();
        LocalDateTime endTimeOfMonth = month.atEndOfMonth().atTime(LocalTime.MAX);

        return jpaQueryFactory.select(
                        Projections.fields(
                                HouseWorkCompleteStatisticsVo.class,
                                member.as("member"),
                                member.count().as("completeCount")
                        )
                )
                .from(houseworkComplete)
                .leftJoin(houseWork).on(houseWork.houseWorkId.eq(houseworkComplete.houseWork.houseWorkId))
                .leftJoin(assignment).on(houseWork.houseWorkId.eq(assignment.houseWork.houseWorkId))
                .leftJoin(member).on(assignment.member.memberId.eq(member.memberId))
                .leftJoin(team).on(member.team.teamId.eq(team.teamId))
                .where(team.teamId.eq(teamId),
                        houseworkComplete.successDateTime.between(startTimeOfMonth, endTimeOfMonth))
                .groupBy(member)
                .orderBy(member.count().desc())
                .fetch();
    }

    private BooleanExpression houseworkNameEq(String houseworkName) {
        return houseworkName != null? houseWork.houseWorkName.eq(houseworkName) : null;
    }
}
