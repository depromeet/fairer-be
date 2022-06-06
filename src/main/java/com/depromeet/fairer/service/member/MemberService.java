package com.depromeet.fairer.service.member;

import com.depromeet.fairer.domain.housework.HouseWork;
import com.depromeet.fairer.domain.team.Team;
import com.depromeet.fairer.dto.member.oauth.OAuthAttributes;
import com.depromeet.fairer.dto.member.jwt.TokenDto;
import com.depromeet.fairer.domain.member.Member;
import com.depromeet.fairer.domain.memberToken.MemberToken;
import com.depromeet.fairer.global.util.DateTimeUtils;
import com.depromeet.fairer.repository.assignment.AssignmentRepository;
import com.depromeet.fairer.repository.housework.HouseWorkRepository;
import com.depromeet.fairer.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
//    private final HouseWorkRepository houseWorkRepository;
//    private final AssignmentRepository assignmentRepository;

    public Team getTeam(Long memberId) {
        return memberRepository.findById(memberId).get().getTeam();
    }

//    public List<HouseWork> getHouseWorks(Member member){
//        return assignmentRepository.findAllByMember(member)
//                .get().f;
//    }

}
