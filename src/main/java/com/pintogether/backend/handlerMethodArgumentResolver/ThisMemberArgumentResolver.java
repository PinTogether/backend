package com.pintogether.backend.handlerMethodArgumentResolver;

import com.pintogether.backend.customAnnotations.ThisMember;
import com.pintogether.backend.exception.CustomException;
import com.pintogether.backend.model.CustomStatusMessage;
import com.pintogether.backend.model.StatusCode;
import com.pintogether.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class ThisMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberRepository memberRepository;

//    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return true;
//        return parameter.getParameterAnnotation(ThisMember.class) != null && parameter.getParameterType().equals(String.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal().toString().equals("anonymousUser")) {
            return null;
        }
        return memberRepository.findById(Long.parseLong(authentication.getPrincipal().toString())).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND, CustomStatusMessage.MEMBER_NOT_FOUND.getMessage())
        );
    }

}
