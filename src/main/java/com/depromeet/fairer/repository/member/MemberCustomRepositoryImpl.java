package com.depromeet.fairer.repository.member;

import com.depromeet.fairer.dto.member.MemberDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.depromeet.fairer.domain.assignment.QAssignment.assignment;
import static com.depromeet.fairer.domain.member.QMember.member;

@Repository
@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<MemberDto> getMemberDtoListByHouseWorkId(Long houseWorkId){
        return jpaQueryFactory
                .select(Projections.fields(MemberDto.class,
                        member.memberId,
                        member.memberName,
                        member.profilePath
                ))
                .from(member)
                .innerJoin(member.assignments, assignment)
                .where(assignment.houseWork.houseWorkId.eq(houseWorkId))
                .fetch();
    }
}
