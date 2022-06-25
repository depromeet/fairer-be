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
        final QMember teamMember = new QMember("teamMember");
        final QTeam aTeam = new QTeam("ATeam");
//        return jpaQueryFactory.select(teamMember)
//                .leftJoin(teamMember.team, ateam)
//                .where(JPAExpressions
//                                .selectFrom(team)
//                                .leftJoin(member.team, team)
//                                .where(member.memberId.eq(memberId))
//                ), aTeam).fetch();
        return null;
    }

}
