package com.depromeet.fairer.repository;


import com.depromeet.fairer.dto.member.MemberDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.depromeet.fairer.domain.assignment.QAssignment.assignment;
import static com.depromeet.fairer.domain.housework.QHouseWork.houseWork;
import static com.depromeet.fairer.domain.member.QMember.member;

@Repository
@RequiredArgsConstructor
@Slf4j
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
}





















