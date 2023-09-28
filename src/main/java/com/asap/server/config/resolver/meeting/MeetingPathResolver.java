package com.asap.server.config.resolver.meeting;

import com.asap.server.common.utils.SecureUrlUtil;
import com.asap.server.exception.Error;
import com.asap.server.exception.model.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.PathVariableMethodArgumentResolver;

@Component
@RequiredArgsConstructor
public class MeetingPathResolver extends PathVariableMethodArgumentResolver {
    private final SecureUrlUtil secureUrlUtil;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(MeetingPathVariable.class);
    }

    @Override
    protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
        MeetingPathVariable mpv = parameter.getParameterAnnotation(MeetingPathVariable.class);
        return new NamedValueInfo(mpv.name(), mpv.required(), "");
    }

    @Override
    protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
        final String encodeUrl = (String) super.resolveName(name, parameter, request);
        try {
            return secureUrlUtil.decodeUrl(encodeUrl);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(Error.INVALID_MEETING_URL_EXCEPTION);
        }
    }
}
