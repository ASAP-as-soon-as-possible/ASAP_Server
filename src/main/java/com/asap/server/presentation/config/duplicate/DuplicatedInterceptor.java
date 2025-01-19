package com.asap.server.presentation.config.duplicate;


import com.asap.server.common.exception.Error;
import com.asap.server.common.exception.model.TooManyRequestException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
@RequiredArgsConstructor
public class DuplicatedInterceptor implements HandlerInterceptor {
    private static final String REDIS_KEY = "ASAP_REDIS";
    private static final String RMAP_VALUE = "ASAP";
    private static final String RMAP_KEY_FORMAT = "LOCK [ ip : %s , body : %s ]";
    private static final String USER_IP_HEADER = "x-real-ip";
    private final RedissonClient redissonClient;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (lock(request)) return true;
        throw new TooManyRequestException(Error.TOO_MANY_REQUEST_EXCEPTION);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        unLock(request);
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        unLock(request);
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    private String getRmapKey(HttpServletRequest request) {
        final String body = ((CustomHttpServletRequestWrapper) request).getBody();
        final String userIp = request.getHeader(USER_IP_HEADER);
        return String.format(RMAP_KEY_FORMAT, userIp, body);
    }

    private boolean lock(HttpServletRequest request) {
        final String rmapKey = getRmapKey(request);
        RMap<String, String> redissonClientMap = redissonClient.getMap(REDIS_KEY);
        return redissonClientMap.putIfAbsent(rmapKey, RMAP_VALUE) == null;
    }

    private void unLock(HttpServletRequest request) {
        final String rmapKey = getRmapKey(request);
        RMap<String, String> redissonClientMap = redissonClient.getMap(REDIS_KEY);
        redissonClientMap.remove(rmapKey);
    }
}
