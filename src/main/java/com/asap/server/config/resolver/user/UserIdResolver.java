package com.asap.server.config.resolver.user;

import com.asap.server.config.jwt.JwtService;
import com.asap.server.exception.Error;
import com.asap.server.exception.model.NotFoundException;
import com.asap.server.exception.model.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

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
        if (token == null || token.isBlank()) {
            throw new UnauthorizedException(Error.TOKEN_NOT_CONTAINED_EXCEPTION);
        }
        if (!jwtService.verifyToken(token)) {
            throw new NotFoundException(Error.USER_NOT_FOUND_EXCEPTION);
        }

        final String tokenContents = jwtService.getJwtContents(token.split(" ")[1]);
        try {
            return Long.parseLong(tokenContents);
        } catch (NumberFormatException e) {
            throw new NotFoundException(Error.USER_NOT_FOUND_EXCEPTION);
        }
    }

}