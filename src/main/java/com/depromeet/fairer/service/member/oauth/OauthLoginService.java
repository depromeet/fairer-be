package com.depromeet.fairer.service.member.oauth;

import com.depromeet.fairer.dto.member.oauth.OAuthAttributes;
import com.depromeet.fairer.dto.member.oauth.OauthLoginDto;
import com.depromeet.fairer.dto.member.jwt.ResponseJwtTokenDto;
import com.depromeet.fairer.dto.member.jwt.TokenDto;
import com.depromeet.fairer.service.member.jwt.TokenProvider;
import com.depromeet.fairer.service.member.oauth.google.GoogleFeignService;
import com.depromeet.fairer.domain.member.Member;
import com.depromeet.fairer.domain.member.constant.SocialType;
import com.depromeet.fairer.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OauthLoginService {

    private final GoogleFeignService googleFeignService;
    private final MemberService memberService;
    private final TokenProvider tokenProvider;
    private final ModelMapper modelMapper;


    public ResponseJwtTokenDto createMemberAndJwt(OauthLoginDto oauthLoginDto) {
        // 소셜 회원 정보 조회
        final OAuthAttributes socialUserInfo = getSocialUserInfo(oauthLoginDto);
        log.info("oauthAttributes: {}", socialUserInfo.toString());

        // JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.createTokenDto(socialUserInfo.getEmail());
        log.info("tokenDto: {}", tokenDto);

        // 회원 가입 or 로그인
        Boolean isNewMember = false;
        final Optional<Member> optionalMember = memberService.getOptionalMember(socialUserInfo.getEmail());
        if (optionalMember.isEmpty()) { // 기존 회원 아닐 때
            Member member = memberService.createMember(socialUserInfo);
            memberService.saveMember(member, tokenDto);
            isNewMember = true;
        } else memberService.saveRefreshToken(optionalMember.get(), tokenDto); // 기존 회원일

        ResponseJwtTokenDto responseJwtTokenDto = modelMapper.map(tokenDto, ResponseJwtTokenDto.class);
        responseJwtTokenDto.setIsNewMember(isNewMember);

        return responseJwtTokenDto;
    }

    private OAuthAttributes getSocialUserInfo(OauthLoginDto oauthLoginDto) {
        final String accessToken = oauthLoginDto.getAccessToken();
        final SocialType socialType = oauthLoginDto.getSocialType();
        return googleFeignService.getUserInfo(accessToken);
    }

    public String getAccessToken(String authorizationCode) {
        return googleFeignService.getAccess(authorizationCode);
    }
}
