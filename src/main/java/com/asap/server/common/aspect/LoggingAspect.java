package com.asap.server.common.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Pointcut("execution(* com.asap.server.controller..*(..)) || ( execution(* com.asap.server.common.advice..*(..)) && !execution(* com.asap.server.common.advice.ControllerExceptionAdvice.handleException*(..)))")
    public void controllerInfoLevelExecute() {
    }

    @Pointcut("execution(* com.asap.server.common.advice.ControllerExceptionAdvice.handleException*(..))")
    public void controllerErrorLevelExecute() {
    }

    @Around("com.asap.server.common.aspect.LoggingAspect.controllerInfoLevelExecute()")
    public Object requestInfoLevelLogging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        final ContentCachingRequestWrapper cachingRequest = (ContentCachingRequestWrapper) request;
        long startAt = System.currentTimeMillis();
        Object returnValue = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        long endAt = System.currentTimeMillis();

        log.info("================================================NEW===============================================");
        log.info("====> Request: {} {} ({}ms)\n *Header = {}", request.getMethod(), request.getRequestURL(), endAt - startAt, getHeaders(request));
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            log.info("====> Body: {}", objectMapper.readTree(cachingRequest.getContentAsByteArray()));
        }
        if (returnValue != null) {
            log.info("====> Response: {}", returnValue);
        }
        log.info("================================================END===============================================");
        return returnValue;
    }

    @Around("com.asap.server.common.aspect.LoggingAspect.controllerErrorLevelExecute()")
    public Object requestErrorLevelLogging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        final ContentCachingRequestWrapper cachingRequest = (ContentCachingRequestWrapper) request;
        long startAt = System.currentTimeMillis();
        Object returnValue = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        long endAt = System.currentTimeMillis();

        log.error("====> Request: {} {} ({}ms)\n *Header = {}", request.getMethod(), request.getRequestURL(), endAt - startAt, getHeaders(request));
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            log.error("====> Body: {}", objectMapper.readTree(cachingRequest.getContentAsByteArray()));
        }
        if (returnValue != null) {
            log.error("====> Response: {}", returnValue);
        }
        log.error("================================================END===============================================");
        return returnValue;
    }

    private Map<String, Object> getHeaders(HttpServletRequest request) {
        Map<String, Object> headerMap = new HashMap<>();

        Enumeration<String> headerArray = request.getHeaderNames();
        while (headerArray.hasMoreElements()) {
            String headerName = headerArray.nextElement();
            headerMap.put(headerName, request.getHeader(headerName));
        }
        return headerMap;
    }
}
