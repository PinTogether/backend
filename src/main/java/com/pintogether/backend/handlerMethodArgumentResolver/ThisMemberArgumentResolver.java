package com.pintogether.backend.handlerMethodArgumentResolver;

import com.pintogether.backend.customAnnotations.ThisMember;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class ThisMemberArgumentResolver implements HandlerMethodArgumentResolver {

//    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return true;
//        return parameter.getParameterAnnotation(ThisMember.class) != null && parameter.getParameterType().equals(String.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal().toString().equals("anonymousUser")) {
            return -1L;
        }
        return Long.parseLong(authentication.getPrincipal().toString());
    }

}
