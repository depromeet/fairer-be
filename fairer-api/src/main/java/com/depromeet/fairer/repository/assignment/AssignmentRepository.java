package com.depromeet.fairer.repository.assignment;

import com.depromeet.fairer.domain.assignment.Assignment;
import com.depromeet.fairer.domain.housework.HouseWork;
import com.depromeet.fairer.domain.member.Member;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long>, AssignmentRepositoryCustom {
    Optional<Assignment> findByHouseWorkAndMember(HouseWork houseWork, Member member);
    List<Assignment> findAllByHouseWorkAndMemberNotIn(HouseWork houseWork, List<Member> members);

    List<Assignment> findAllByMember(Member member);

    List<HouseWork> findAllHouseWorkByAssignmentIdInAndHasOnlyAssignee(List<Long> assignmentIds);

    @Modifying(clearAutomatically = true)
    @Query("delete from Assignment a where a.houseWork.houseWorkId = :houseWorkId")
    void deleteAllByHouseworkId(@Param("houseWorkId") Long houseWorkId);

    void deleteAllByMember(Member member);
}
