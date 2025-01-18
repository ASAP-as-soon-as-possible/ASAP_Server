package com.asap.server.presentation.common.log;

import jakarta.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    private static final String REQUEST_FORMAT = "URI : %s | Request : %s";
    private static final String RESPONSE_FORMAT = "URI : %s | Response : %s";

    @Pointcut("execution(* com.asap.server.presentation.controller..*(..))")
    public void serviceLoggingExecute() {
    }

    @Pointcut("execution(* com.asap.server.presentation.common.advice.ControllerExceptionAdvice..*(..))")
    public void exceptionLoggingExecute() {
    }

    @Around("com.asap.server.presentation.common.log.LoggingAspect.serviceLoggingExecute()")
    public Object logging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        final String uri = request.getRequestURI();
        final String args = getParameterNameAndArgs(proceedingJoinPoint);

        log.info(String.format(REQUEST_FORMAT, uri, args));
        final Object returnValue = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        log.info(String.format(RESPONSE_FORMAT, uri, returnValue));

        return returnValue;
    }

    @Around("com.asap.server.presentation.common.log.LoggingAspect.exceptionLoggingExecute()")
    public Object errorLogging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        final Object returnValue = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());

        final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        final String uri = request.getRequestURI();
        log.info(String.format(RESPONSE_FORMAT, uri, returnValue));
        return returnValue;
    }

    private String getParameterNameAndArgs(final ProceedingJoinPoint proceedingJoinPoint) {
        final MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        final String[] parameterNames = methodSignature.getParameterNames();
        final Object[] args = proceedingJoinPoint.getArgs();

        if (parameterNames == null || args == null || parameterNames.length != args.length) {
            return "[]";
        }

        return IntStream.range(0, parameterNames.length)
                .mapToObj(i -> parameterNames[i] + " : " + args[i])
                .collect(Collectors.joining(", ", "[", "]"));
    }
}
