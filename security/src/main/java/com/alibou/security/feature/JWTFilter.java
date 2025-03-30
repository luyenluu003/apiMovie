package com.alibou.security.feature;

import com.alibou.security.exception.NotAuthorizedException;
import com.alibou.security.utils.SecurityUtil;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@Component
@Log4j2
public class JWTFilter extends OncePerRequestFilter {
    @Value("${superapp.jwt.secret.key}")
    String jwtKey;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    HandlerExceptionResolver resolver;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        String token = null;
        if(authorization != null && authorization.startsWith("Bearer ")) {
            try{
                token = authorization.split(" ")[1].trim();

            }catch (Exception e){
                showHeader(httpServletRequest);
                resolver.resolveException(httpServletRequest,response,null,new NotAuthorizedException( "Bearer is invalid"));
                return;
            }
            UserDetails userDetails = null;
            try{
                userDetails = SecurityUtil.validateJWT(token, jwtKey);
            } catch (JWTVerificationException jwt){
                resolver.resolveException(httpServletRequest,response,null,new NotAuthorizedException(  jwt.getMessage()));
            }

            log.info("TOKEN ===> "+token);

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                    = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            filterChain.doFilter(httpServletRequest, response);
        }else{
            showHeader(httpServletRequest);
            resolver.resolveException(httpServletRequest,response,null,new NotAuthorizedException("Bearer header not found "));
        }
    }

    private void showHeader(HttpServletRequest httpServletRequest) {
        Enumeration<String> headers = httpServletRequest.getHeaderNames();
        StringBuilder builder = new StringBuilder();
        while (headers.hasMoreElements()) {
            String headerName = headers.nextElement();
            builder.append(headerName).append(": ").append(httpServletRequest.getHeader(headerName)).append(", ");
        }
        log.info("HEADERS (" + builder + ") ==> Path: " + httpServletRequest.getServletPath());
        log.info("paramMap: {} ==> Path: {}", httpServletRequest.getParameterMap(), httpServletRequest.getServletPath());
    }

//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//        String path = request.getServletPath();
//        if(path.startsWith("/without-bearer/") ||
//                path.startsWith("/genotp/") ||
//                path.startsWith("/actuator/") ||
//                path.startsWith("/swagger/") ||
//                path.startsWith("/internal/") ||
//                path.startsWith("/v3/api-docs") ||
//                path.startsWith("/swagger-ui") ||
//                path.startsWith("/websocket") ||
//                path.startsWith("/banner/clearCache") ||
//                path.startsWith("/homepage/clearCache")) {
//            log.debug(path + " shouldNotFilter: true");
//            return true;
//        }
//        return false;
//    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        log.info("Checking shouldNotFilter for path: {}", path);
        if (path.startsWith("/v1/authen/") ||
//                path.equals("/v1/authen/login/email") ||
//                path.equals("/v1/authen/forgot") ||
//                path.equals("/v1/authen/login/request-otp") ||
//                path.equals("/v1/authen/login/otp") ||
                path.startsWith("/v1/") ||
                path.startsWith("/ws/") ||
                path.startsWith("/without-bearer/") ||
                path.startsWith("/genotp/") ||
                path.startsWith("/actuator/") ||
                path.startsWith("/swagger/") ||
                path.startsWith("/internal/") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/swagger-ui") ||
                path.startsWith("/websocket") ||
                path.startsWith("/banner/clearCache") ||
                path.startsWith("/homepage/clearCache")) {
            log.debug("{} shouldNotFilter: true", path);
            return true;
        }
        log.debug("{} shouldNotFilter: false", path);
        return false;
    }


}
