package com.depromeet.fairer.repository.assignment;

import com.depromeet.fairer.domain.assignment.Assignment;
import com.depromeet.fairer.domain.housework.HouseWork;
import com.depromeet.fairer.domain.member.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.depromeet.fairer.domain.assignment.QAssignment.assignment;
import static com.depromeet.fairer.domain.member.QMember.member;

@Repository
@RequiredArgsConstructor
public class AssignmentRepositoryCustomImpl implements AssignmentRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public void deleteAllByMember(Member member) {
        jpaQueryFactory.delete(assignment)
                .where(assignment.member.eq(member))
                .execute();
    }

    @Override
    public List<HouseWork> findAllHouseWorkByAssignmentIdInAndHasOnlyAssignee(List<Long> assignmentIds) {
        return jpaQueryFactory.select(assignment.houseWork)
                .from(assignment)
                .where(assignment.assignmentId.in(assignmentIds))
                .groupBy(assignment.assignmentId)
                .having(assignment.member.count().eq(1L))
                .fetch();
    }

    @Override
    public List<Assignment> findAllByMember(Member member) {
        return jpaQueryFactory.selectFrom(assignment)
                .where(assignment.member.eq(member))
                .fetch();
    }

}
