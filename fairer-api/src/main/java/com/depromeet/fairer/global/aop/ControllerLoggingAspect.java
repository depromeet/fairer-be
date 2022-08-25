package com.depromeet.fairer.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Aspect
@Component
public class ControllerLoggingAspect {
    @Pointcut("execution(* com.depromeet.fairer.api.*.*(..))")
    private void controllerPointCut(){}

    @Around(value = "controllerPointCut()")
    public Object aroundControllerPointCut(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Object[] args = joinPoint.getArgs();
        log.info("[{}|{}] request : {}", request.getRequestURI(), request.getMethod(), args);
        Object response = joinPoint.proceed();
        ResponseEntity<?> responseEntity = (ResponseEntity<?>)response;
        log.info("[{}|{}] response : {}", request.getRequestURI(), responseEntity.getStatusCode(), responseEntity.getBody());
        return response;
    }
}
