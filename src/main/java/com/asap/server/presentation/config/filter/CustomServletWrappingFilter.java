package com.asap.server.presentation.config.filter;

import com.asap.server.presentation.common.wrapper.CustomHttpServletRequestWrapper;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public class CustomServletWrappingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = new CustomHttpServletRequestWrapper((HttpServletRequest) servletRequest);
        filterChain.doFilter(request, servletResponse);
    }
}
