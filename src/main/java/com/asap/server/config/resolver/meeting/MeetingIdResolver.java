package com.asap.server.config.resolver.meeting;

import com.asap.server.common.utils.SecureUrlUtil;
import com.asap.server.exception.Error;
import com.asap.server.exception.model.BadRequestException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

@Component
@RequiredArgsConstructor
public class MeetingIdResolver implements HandlerMethodArgumentResolver {
    private final SecureUrlUtil secureUrlUtil;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(MeetingId.class) && Long.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(@NotNull MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        final HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        final Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        final String meetingId = pathVariables.get("meetingId");
        try {
            return secureUrlUtil.decodeUrl(meetingId);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(Error.INVALID_MEETING_URL_EXCEPTION);
        }
    }
}
