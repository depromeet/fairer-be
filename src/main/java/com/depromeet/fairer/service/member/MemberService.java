package com.depromeet.fairer.service.member;

import com.depromeet.fairer.dto.member.oauth.OAuthAttributes;
import com.depromeet.fairer.dto.member.jwt.TokenDto;
import com.depromeet.fairer.domain.member.Member;
import com.depromeet.fairer.domain.memberToken.MemberToken;
import com.depromeet.fairer.global.util.DateTimeUtils;
import com.depromeet.fairer.repository.MemberRepository;
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

    public Optional<Member> getOptionalMember(String email) {
        return memberRepository.findByEmail(email);
    }

    public Member createMember(OAuthAttributes socialUserInfo) {
        return Member.create(socialUserInfo);
    }

    public Member saveMember(Member member, TokenDto tokenDto) {
        final Member savedMember = memberRepository.save(member);
        saveRefreshToken(savedMember, tokenDto);
        return savedMember;
    }

    /**
     * refresh token 저장
     * @param member
     * @param tokenDto
     */
    public void saveRefreshToken(Member member, TokenDto tokenDto) {
        LocalDateTime tokenExpiredTime = DateTimeUtils.convertToLocalDateTime(tokenDto.getRefreshTokenExpireTime());

        final MemberToken memberToken = MemberToken.create(member, tokenDto.getRefreshToken(), tokenExpiredTime);
        memberRepository.save(member);
    }
}
