package com.pintogether.backend.aspect;

import com.pintogether.backend.customAnnotations.CurrentMember;
import com.pintogether.backend.customAnnotations.ThisMember;
import com.pintogether.backend.entity.Member;
import com.pintogether.backend.handlerMethodArgumentResolver.MemberArgumentResolver;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
public class LogginAspect {

    @Around("execution(* com.pintogether.backend.service.*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed(); // 메서드 실행
        long executionTime = System.currentTimeMillis() - start;
        System.out.println("========================================= EXECUTION TIME ===============================================================");
        System.out.println("ID [ " + authentication.getPrincipal() +
                " ] completed request to SERVICE METHOD \"" +
                joinPoint.getSignature().getName() + "\"");
        System.out.println(joinPoint.getSignature());
        System.out.println(joinPoint.getSignature().getName() + " executed in << " + executionTime + "ms >>");
        System.out.println("========================================================================================================================");
        return proceed;

    }

//    @Before("execution(* com.pintogether.backend.controller.*.*(..))")
//    public void logBefore(JoinPoint joinPoint) {
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        System.out.println("ID [ " + authentication.getPrincipal() +
//                " ] made request to CONTROLLER METHOD \"" +
//                joinPoint.getSignature().getName() + "\"");
//
//    }

//    @AfterReturning(pointcut = "execution(* com.pintogether.backend.controller.*.*(..))", returning = "result")
//    public void logAfter(JoinPoint joinPoint, Object result) {
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        System.out.println("ID [ " + authentication.getPrincipal() +
//                " ] completed request to CONTROLLER METHOD \"" +
//                joinPoint.getSignature().getName() + "\"");
//
//    }

}
