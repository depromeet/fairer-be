package com.depromeet.fairer.global.resolver;

import com.depromeet.fairer.service.member.jwt.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class RequestMemberIdArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        final boolean hasMemberAnnotation = parameter.hasParameterAnnotation(RequestMemberId.class);
        final boolean hasLong = Long.class.isAssignableFrom(parameter.getParameterType());

        return hasMemberAnnotation && hasLong;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        final HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        final String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        return TokenProvider.getMemberId(token);
    }
}
