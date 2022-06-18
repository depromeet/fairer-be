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

import com.depromeet.fairer.domain.member.Member;
import com.depromeet.fairer.global.exception.NoSuchMemberException;

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

    public Member find(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new NoSuchMemberException("해당하는 멤버를 찾을 수 없습니다."));
    }

    public Member findWithTeam(Long memberId) {
        return memberRepository.findWithTeamByMemberId(memberId).orElseThrow(()->new NoSuchMemberException("해당하는 멤버를 찾을 수 없습니다."));
    }
}