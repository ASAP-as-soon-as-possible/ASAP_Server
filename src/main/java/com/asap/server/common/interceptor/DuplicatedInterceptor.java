package com.asap.server.common.interceptor;


import com.asap.server.exception.Error;
import com.asap.server.exception.model.InternalErrorException;
import com.asap.server.exception.model.TooManyRequestException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class DuplicatedInterceptor implements HandlerInterceptor {

    private final RedissonClient redissonClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        final ContentCachingRequestWrapper cachingRequest = (ContentCachingRequestWrapper) request;
        final String lockKey = (cachingRequest.getHeader("Host") + objectMapper.readTree(cachingRequest.getContentAsByteArray()));
        RLock lock = redissonClient.getLock(lockKey);
        boolean isLock = false;
        try {
            isLock = lock.tryLock(0, 0, TimeUnit.SECONDS);
            if (!isLock) throw new TooManyRequestException(Error.TOO_MANY_REQUEST_EXCEPTION);
        } catch (InterruptedException e) {
            throw new InternalErrorException(Error.INTERNAL_SERVER_ERROR);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        final ContentCachingRequestWrapper cachingRequest = (ContentCachingRequestWrapper) request;
        final String lockKey = (cachingRequest.getHeader("Host") + objectMapper.readTree(cachingRequest.getContentAsByteArray()));
        RLock lock = redissonClient.getLock(lockKey);
        lock.unlock();
    }
}
