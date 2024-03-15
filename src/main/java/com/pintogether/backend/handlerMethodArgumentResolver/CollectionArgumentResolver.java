package com.pintogether.backend.handlerMethodArgumentResolver;

import com.pintogether.backend.customAnnotations.CurrentCollection;
import com.pintogether.backend.entity.Collection;
import com.pintogether.backend.exception.CustomException;
import com.pintogether.backend.model.CustomStatusMessage;
import com.pintogether.backend.model.StatusCode;
import com.pintogether.backend.repository.CollectionRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

@Component
public class CollectionArgumentResolver implements HandlerMethodArgumentResolver {

    private final CollectionRepository collectionRepository;

    public CollectionArgumentResolver(CollectionRepository collectionRepository) {
        this.collectionRepository = collectionRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(CurrentCollection.class) != null &&
                Collection.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest httpRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        if (httpRequest != null) {
            Map<String, String> pathVariables = (Map<String, String>) httpRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            String collectionIdStr = pathVariables.get("collectionId");
            if (collectionIdStr != null) {
                Long collectionId = Long.valueOf(collectionIdStr);
                return collectionRepository.findById(collectionId)
                        .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, CustomStatusMessage.COLLECTION_NOT_FOUND.getMessage()));
            }
        }
        throw new CustomException(StatusCode.BAD_REQUEST, "Invalid or missing collectionId path variable");
    }
}
