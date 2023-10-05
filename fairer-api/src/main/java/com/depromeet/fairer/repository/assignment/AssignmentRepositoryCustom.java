package com.depromeet.fairer.repository.assignment;

import com.depromeet.fairer.domain.assignment.Assignment;
import com.depromeet.fairer.domain.housework.HouseWork;
import com.depromeet.fairer.domain.member.Member;

import java.util.List;

public interface AssignmentRepositoryCustom {

    void deleteAllByMember(Member member);

    List<HouseWork> findAllHouseWorkByAssignmentIdInAndHasOnlyAssignee(List<Long> assignmentIds);

    List<Assignment> findAllByMember(Member member);
}
