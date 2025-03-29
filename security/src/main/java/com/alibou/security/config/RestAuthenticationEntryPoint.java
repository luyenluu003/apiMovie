package com.alibou.security.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Log4j2
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Autowired
    @Qualifier("handlerExceptionResolver") //Đăng ký bean
    private HandlerExceptionResolver resolver;

    @Override
    public void commence(javax.servlet.http.HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, SecurityException {
        log.error("VAO DAY =>> {}", request.getRequestURI());
        log.error("VAO DAY 1 =>> {}", exception.getMessage());
        log.error("VAO DAY 2 =>> {}", request);
        log.error("Request method: {}, Headers: {}", request.getMethod(), request.getHeader("Authorization"));
        resolver.resolveException(request, response, null, exception);
    }
}
