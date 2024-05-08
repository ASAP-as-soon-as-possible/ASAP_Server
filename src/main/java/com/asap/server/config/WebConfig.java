package com.asap.server.config;

import com.asap.server.common.interceptor.DuplicatedInterceptor;
import com.asap.server.config.resolver.meeting.MeetingPathVariableResolver;
import com.asap.server.config.resolver.user.UserIdResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final UserIdResolver userIdResolver;
    private final MeetingPathVariableResolver meetingPathVariableResolver;
    private final DuplicatedInterceptor duplicatedInterceptor;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST")
                .maxAge(3000);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userIdResolver);
        resolvers.add(meetingPathVariableResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry) {
        interceptorRegistry.addInterceptor(duplicatedInterceptor)
                .addPathPatterns("/meeting", "/user/{meetingId}/time", "/user/host/{meetingId}/time");
    }
}
