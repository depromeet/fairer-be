package com.depromeet.fairer.repository.housework;

import com.depromeet.fairer.domain.assignment.QAssignment;
import com.depromeet.fairer.domain.housework.HouseWork;
import com.depromeet.fairer.domain.housework.QHouseWork;
import com.depromeet.fairer.domain.member.QMember;
import com.depromeet.fairer.domain.team.QTeam;
import com.depromeet.fairer.vo.houseWork.HouseWorkDetailVo;
import com.querydsl.core.types.Projections;
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

    @Override
    public List<HouseWork> getHouseWorkAndAssignee(Long memberId, LocalDate localDate) {
        return jpaQueryFactory.selectFrom(houseWork)
                .innerJoin(assignment.member, member)
                .innerJoin(houseWork.assignments, assignment)
                .where(member.memberId.eq(memberId)
                        .and(houseWork.scheduledDate.eq(localDate)))
                .fetch();

    }

    @Override
    public List<HouseWorkDetailVo> getHouseWorkAndAssignees(Long memberId, LocalDate localDate) {
        QAssignment assignment1 = QAssignment.assignment;
        QMember member = QMember.member;
        QAssignment assignment2 = QAssignment.assignment;
        QHouseWork houseWork = QHouseWork.houseWork;

        return jpaQueryFactory.select(Projections.bean(HouseWorkDetailVo.class,
                        houseWork.houseWorkId,
                        houseWork.space,
                        houseWork.houseWorkName,
                        houseWork.scheduledTime,
                        houseWork.successDateTime,
                        houseWork.success,
                        member.memberId,
                        member.memberName,
                        member.profilePath))
                .from(assignment1)
                .innerJoin(houseWork).on(assignment1.houseWork.houseWorkId.eq(houseWork.houseWorkId))
                .innerJoin(assignment2).on(assignment2.houseWork.houseWorkId.eq(houseWork.houseWorkId))
                .innerJoin(member).on(assignment2.member.memberId.eq(member.memberId))
                .where(houseWork.scheduledDate.eq(localDate)
                        .and(assignment1.member.memberId.eq(memberId)))
                .fetch();
    }

    @Override
    public List<HouseWorkDetailVo> getHouseWorkAndAssigneesByDate(Long reqMemberId, LocalDate localDate) {
        QMember reqMember = QMember.member;
        QHouseWork houseWork = QHouseWork.houseWork;
        QTeam team = QTeam.team;

        return jpaQueryFactory.select(Projections.bean(HouseWorkDetailVo.class,
                houseWork.houseWorkId,
                houseWork.space,
                houseWork.houseWorkName,
                houseWork.scheduledTime,
                houseWork.successDateTime,
                houseWork.success,
                member.memberId,
                member.memberName,
                member.profilePath))
                .from(team)
                .innerJoin(reqMember).on(reqMember.team.teamId.eq(team.teamId))
                .innerJoin(member).on(member.team.teamId.eq(team.teamId))
                .innerJoin(assignment).on(assignment.member.memberId.eq(member.memberId))
                .innerJoin(houseWork).on(assignment.houseWork.houseWorkId.eq(houseWork.houseWorkId))
                .where(reqMember.memberId.eq(reqMemberId)
                        .and(houseWork.scheduledDate.eq(localDate)))
                .fetch();
    }
}