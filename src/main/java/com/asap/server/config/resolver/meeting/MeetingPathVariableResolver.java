package com.asap.server.config.resolver.meeting;

import com.asap.server.common.utils.SecureUrlUtil;
import com.asap.server.exception.model.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

import static com.asap.server.exception.Error.INVALID_MEETING_URL_EXCEPTION;

@Component
@RequiredArgsConstructor
public class MeetingPathVariableResolver implements HandlerMethodArgumentResolver {
    private static final String MEETING_PATH_VARIABLE = "meetingId";
    private final SecureUrlUtil secureUrlUtil;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(MeetingPathVariable.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        final HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        final Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        final String meetingId = pathVariables.get(MEETING_PATH_VARIABLE);
        try {
            return secureUrlUtil.decodeUrl(meetingId);
        } catch (NumberFormatException e) {
            throw new BadRequestException(INVALID_MEETING_URL_EXCEPTION);
        }
    }
}
