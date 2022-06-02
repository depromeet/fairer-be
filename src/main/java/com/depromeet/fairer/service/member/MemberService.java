package com.depromeet.fairer.service.member;

import com.depromeet.fairer.domain.team.Team;
import com.depromeet.fairer.dto.member.oauth.OAuthAttributes;
import com.depromeet.fairer.dto.member.jwt.TokenDto;
import com.depromeet.fairer.domain.member.Member;
import com.depromeet.fairer.domain.memberToken.MemberToken;
import com.depromeet.fairer.global.util.DateTimeUtils;
import com.depromeet.fairer.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Team getTeam(Long memberId) {
        return memberRepository.findById(memberId).get().getTeam();
    }

}
