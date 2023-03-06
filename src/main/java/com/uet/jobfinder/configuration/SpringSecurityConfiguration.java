package com.uet.jobfinder.configuration;

import com.uet.jobfinder.security.CustomUserDetailsService;
import com.uet.jobfinder.security.CustomUsernamePasswordFilter;
import com.uet.jobfinder.security.JWTAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfiguration {

    private CustomUserDetailsService userDetailsService;

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().disable()
                // .and()
                .authorizeRequests()
                .antMatchers("/api/login").permitAll();
        // .antMatchers("/api/order/list").authenticated()
        //// .antMatchers("/api/login", "/api/account/create").permitAll()
        //// .antMatchers("/users/get").authenticated();
        // .anyRequest().authenticated();
        http.addFilterBefore(jwtAuthenticationFilter(), CustomUsernamePasswordFilter.class);
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Autowired
    public void setUserDetailService(CustomUserDetailsService userDetailService) {
        this.userDetailsService = userDetailService;
    }

}
