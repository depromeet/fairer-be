package com.depromeet.fairer.global.config.interceptor;

import com.depromeet.fairer.repository.memberToken.MemberTokenRepository;
import com.depromeet.fairer.service.member.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.transaction.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
public class AuthInterceptor implements HandlerInterceptor {
    private final TokenProvider tokenProvider;
    private final MemberTokenRepository memberTokenRepository;
}
