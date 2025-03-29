package com.alibou.security.feature;

import com.alibou.security.config.MovieConfiguration;
import com.alibou.security.utils.SecurityUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Log4j2
@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    MovieConfiguration movieConfiguration;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public String getUserNameFromHttpRequest(HttpServletRequest httpServletRequest) {
        return SecurityUtil.getUserNameFromHttpRequest(httpServletRequest, movieConfiguration.getJwtSecretKey());
    }

    public String getTokenFromHttpRequest(HttpServletRequest httpServletRequest) {
        return SecurityUtil.getTokenFromHttpRequest(httpServletRequest, movieConfiguration.getJwtSecretKey());
    }

}
