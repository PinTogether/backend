package com.pintogether.backend.config;


import com.pintogether.backend.handlerMethodArgumentResolver.CollectionArgumentResolver;
import com.pintogether.backend.handlerMethodArgumentResolver.CollectionCommentArgumentResolver;
import com.pintogether.backend.handlerMethodArgumentResolver.MemberArgumentResolver;
import com.pintogether.backend.handlerMethodArgumentResolver.ThisMemberArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final CollectionArgumentResolver collectionArgumentResolver;
    private final CollectionCommentArgumentResolver collectionCommentArgumentResolver;
    private final MemberArgumentResolver memberArgumentResolver;
    private final ThisMemberArgumentResolver thisMemberArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(collectionArgumentResolver);
        resolvers.add(collectionCommentArgumentResolver);
        resolvers.add(memberArgumentResolver);
        resolvers.add(thisMemberArgumentResolver);
    }
}