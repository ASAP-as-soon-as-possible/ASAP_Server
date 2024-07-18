package com.asap.server.presentation.config.resolver.user;

import com.asap.server.common.jwt.JwtService;
import com.asap.server.common.exception.Error;
import com.asap.server.common.exception.model.BadRequestException;
import com.asap.server.common.exception.model.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;

@RequiredArgsConstructor
@Component
public class UserIdResolver implements HandlerMethodArgumentResolver {
    private final JwtService jwtService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserId.class) && Long.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(@NotNull MethodParameter parameter, ModelAndViewContainer modelAndViewContainer, @NotNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        final HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        final String token = request.getHeader("Authorization");
        if (token == null || token.isBlank() || !token.startsWith("Bearer ")) {
            throw new UnauthorizedException(Error.TOKEN_NOT_CONTAINED_EXCEPTION);
        }
        final String encodedUserId = token.substring("Bearer ".length());
        if (!jwtService.verifyToken(encodedUserId)) {
            throw new UnauthorizedException(Error.EXPIRE_TOKEN_EXCEPTION);
        }
        final String decodedUserId = jwtService.getJwtContents(encodedUserId);
        try {
            return Long.parseLong(decodedUserId);
        } catch (NumberFormatException e) {
            throw new BadRequestException(Error.INVALID_TOKEN_EXCEPTION);
        }
    }

}