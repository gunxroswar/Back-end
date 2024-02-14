package com.Deadline.BackEnd.Backend.config;


import com.Deadline.BackEnd.Backend.filter.JwtAuthenticationFilter;
import com.Deadline.BackEnd.Backend.service.UserDetailsServiceImp;
import com.Deadline.BackEnd.Backend.service.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
//@ConfigurationProperties(prefix = "security.jwt.token")
public class SecurityConfig {
    private final UserDetailsServiceImp userServiceimpl;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig( UserDetailsServiceImp userServiceimpl1, JwtAuthenticationFilter authenticationFilter) {
        this.userServiceimpl = userServiceimpl1;
        this.jwtAuthenticationFilter = authenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        req->req.requestMatchers("/guests/signup","/guests/login")
                                .permitAll()
                                .requestMatchers("/admin_mode/**")
                                .hasAuthority("ADMIN")
                                .anyRequest()
                                .authenticated()
                ).userDetailsService(userServiceimpl)
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter , UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
//    @Bean
//    public AuthenticationManager getAuthenticationManager() throws Exception {
//        return authenticationManager();
//    }
//
//    private AuthenticationManager authenticationManager() {
//    }
}
