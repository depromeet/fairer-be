package com.depromeet.fairer.repository;

import com.depromeet.fairer.domain.assignment.QAssignment;
import com.depromeet.fairer.domain.housework.QHouseWork;
import com.depromeet.fairer.domain.member.QMember;
import com.depromeet.fairer.dto.member.MemberDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public List<MemberDto> addMemberDtoById(Long houseWorkId){
        List<MemberDto> memberDtos = jpaQueryFactory
                .select(Projections.fields(MemberDto.class,
                        member.memberId,
                        member.memberName,
                        member.profilePath
                ))
                .from(member)
                .leftJoin(member.assignments, assignment)
                .on(member.memberId.eq(assignment.member.memberId))
                .leftJoin(assignment.housework, houseWork)
                .on(houseWork.houseWorkId.eq(assignment.housework.houseWorkId))
                .where(houseWork.houseWorkId.eq(houseWorkId))
                .fetch();
        return memberDtos;
    }

    @Override
    public Long getHouseWorkSuccessCount(Long memberId, LocalDate startDate, LocalDate endDate) {
        QAssignment assignment = QAssignment.assignment;
        QMember member = QMember.member;
        QHouseWork houseWork = QHouseWork.houseWork;

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
}



















