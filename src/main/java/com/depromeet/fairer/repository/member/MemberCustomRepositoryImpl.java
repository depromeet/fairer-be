package com.depromeet.fairer.repository.member;

import com.depromeet.fairer.domain.member.Member;
import com.depromeet.fairer.domain.member.QMember;
import com.depromeet.fairer.domain.team.QTeam;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.depromeet.fairer.domain.assignment.QAssignment.assignment;
import static com.depromeet.fairer.domain.member.QMember.member;
import static com.depromeet.fairer.domain.team.QTeam.team;

@Repository
@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Member> getMemberDtoListByHouseWorkId(Long houseWorkId){
        return jpaQueryFactory
                .selectFrom(member)
                .innerJoin(member.assignments, assignment)
                .where(assignment.houseWork.houseWorkId.eq(houseWorkId))
                .fetch();
    }

    @Override
    public List<Member> getMyTeamMembers(Long memberId) {
        final QMember member1 = QMember.member;
        final QMember member2 = QMember.member;
        final QTeam team = QTeam.team;
        return jpaQueryFactory.select(member1)
                .from(team)
                .innerJoin(member2).on(member2.team.eq(team))
                .where(member2.memberId.eq(memberId))
                .fetch();
    }

}
