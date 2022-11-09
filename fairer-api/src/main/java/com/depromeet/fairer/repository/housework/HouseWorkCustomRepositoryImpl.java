package com.depromeet.fairer.repository.housework;

import com.depromeet.fairer.domain.housework.QHouseWork;
import com.depromeet.fairer.domain.housework.constant.RepeatCycle;
import com.depromeet.fairer.domain.team.Team;
import com.depromeet.fairer.dto.housework.response.HouseWorkQueryResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanOperation;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.depromeet.fairer.domain.assignment.QAssignment.assignment;
import static com.depromeet.fairer.domain.housework.QHouseWork.houseWork;
import static com.depromeet.fairer.domain.houseworkComplete.QHouseworkComplete.houseworkComplete;
import static com.depromeet.fairer.domain.member.QMember.member;
import static com.depromeet.fairer.domain.repeatexception.QRepeatException.repeatException;

@Repository
@RequiredArgsConstructor
@Slf4j
public class HouseWorkCustomRepositoryImpl implements HouseWorkCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Long getHouseWorkSuccessCount(Long memberId, LocalDate startDate, LocalDate endDate) {
        return jpaQueryFactory.select(houseWork)
                .from(houseWork, houseworkComplete)
                .innerJoin(houseWork.assignments, assignment)
                .innerJoin(assignment.member, member)
                .where(houseworkComplete.successDateTime.goe(startDate.atStartOfDay())
                        .and(houseworkComplete.successDateTime.lt(endDate.plusDays(1).atStartOfDay()))
                        .and(member.memberId.eq(memberId))
                        .and(houseworkComplete.houseWork.houseWorkId.eq(houseWork.houseWorkId)))
                .stream()
                .count();
    }

    @Override
    public List<HouseWorkQueryResponseDto> getCycleHouseWorkQuery(LocalDate date, Long memberId) {

        return jpaQueryFactory
                .selectDistinct(Projections.fields(HouseWorkQueryResponseDto.class,
                        houseWork,
                        houseworkComplete.houseWorkCompleteId))
                .from(houseWork)
                .innerJoin(houseWork.assignments, assignment)
                .innerJoin(assignment.member, member)
                .leftJoin(houseworkComplete).on(houseworkComplete.houseWork.eq(houseWork)
                        .and(houseworkComplete.scheduledDate.eq(date)))
                .leftJoin(repeatException).on(repeatException.houseWork.eq(houseWork))
                .where(member.memberId.eq(memberId).and(houseWork.scheduledDate.loe(date).and(houseWork.repeatEndDate.isNull().or(houseWork.repeatEndDate.goe(date))))
                        .and((houseWork.repeatCycle.eq(RepeatCycle.ONCE).and(houseWork.repeatPattern.eq(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))))
                            .or(houseWork.repeatCycle.eq(RepeatCycle.DAILY).and(getException(houseWork, date)))
                            .or(houseWork.repeatCycle.eq(RepeatCycle.WEEKLY).and(houseWork.repeatPattern.contains(date.getDayOfWeek().toString())).and(getException(houseWork, date)))
                            .or(houseWork.repeatCycle.eq(RepeatCycle.MONTHLY).and(houseWork.repeatPattern.castToNum(Integer.class).eq(date.getDayOfMonth())).and(getException(houseWork, date))))
                ).fetch();


    }

    @Override
    public List<HouseWorkQueryResponseDto> getCycleHouseWorkByTeamQuery(LocalDate date, Team team) {
        return jpaQueryFactory
                .selectDistinct(Projections.fields(HouseWorkQueryResponseDto.class,
                        houseWork,
                        houseworkComplete.houseWorkCompleteId))
                .from(houseWork)
                .innerJoin(houseWork.assignments, assignment)
                .innerJoin(assignment.member, member)
                .leftJoin(houseworkComplete).on(houseworkComplete.houseWork.eq(houseWork)
                        .and(houseworkComplete.scheduledDate.eq(date)))
                .where(houseWork.team.eq(team).and(houseWork.scheduledDate.loe(date).and(houseWork.repeatEndDate.isNull().or(houseWork.repeatEndDate.goe(date))))
                        .and((houseWork.repeatCycle.eq(RepeatCycle.ONCE).and(houseWork.repeatPattern.eq(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))))
                                .or(houseWork.repeatCycle.eq(RepeatCycle.DAILY).and(getException(houseWork, date)))
                                .or(houseWork.repeatCycle.eq(RepeatCycle.WEEKLY).and(houseWork.repeatPattern.contains(date.getDayOfWeek().toString())).and(getException(houseWork, date)))
                                .or(houseWork.repeatCycle.eq(RepeatCycle.MONTHLY).and(houseWork.repeatPattern.castToNum(Integer.class).eq(date.getDayOfMonth())).and(getException(houseWork, date))))
                ).fetch();
    }

    public BooleanOperation getException(QHouseWork houseWork, LocalDate date){
        return jpaQueryFactory.selectFrom(repeatException)
                .where(repeatException.houseWork.houseWorkId.eq(houseWork.houseWorkId)
                        .and(repeatException.exceptionDate.eq(date))).isNull();
    }
}