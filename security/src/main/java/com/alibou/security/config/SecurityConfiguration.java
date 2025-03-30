package com.alibou.security.config;

import com.alibou.security.feature.AuthenticationService;
import com.alibou.security.feature.JWTFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private AuthenticationService userService;
    @Autowired
    @Qualifier("restAuthenticationEntryPoint")
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    private JWTFilter jwtFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }


    //    @Override
    //    protected void configure(HttpSecurity http) throws Exception {
    //        http = http.csrf().and().cors().disable();
    //
    //        http = http.sessionManagement()
    //                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and();
    //
    //        http = http.exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint).and();
    //
    //        http.authorizeRequests()
    //                .antMatchers("/swagger/**", "/demo/**", "/actuator/**").hasIpAddress("192.168.1.0/24")
    //                .antMatchers("/user/authenticate",  "/s2s/**","/anonymous/**","/demo/**", "/user/get").permitAll()
    //                .anyRequest().authenticated();
    //
    //        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    //    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()//allow CORS option calls
                .antMatchers("/anonymous/**").permitAll()
                .antMatchers("/v1/**").permitAll()
                .antMatchers("/ws/**").permitAll() // Cho phép tất cả yêu cầu đến /ws/**
                .antMatchers("/internal/**",
                        "/without-bearer/**",
                        "/actuator/**").hasIpAddress("10.225.10.0/24")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.headers().frameOptions().disable();
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}