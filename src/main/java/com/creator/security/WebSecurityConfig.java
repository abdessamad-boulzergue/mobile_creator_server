package com.creator.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



  @Configuration
  @EnableWebSecurity
  public class WebSecurityConfig  {

      @Autowired
      private RestAuthenticationEntryPoint unauthorizedHandler;

      @Bean
      public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
          httpSecurity
                  // we don't need CSRF because our token is invulnerable
                  .csrf().disable()
                  .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                  // don't create session
                  .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                  .authorizeRequests()
                  .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                  .antMatchers("/users/**").hasRole("ADMIN")
                  .antMatchers("/creator/api/v1/auth/**").permitAll()
                  .antMatchers("/actuator/**").permitAll()
                  .anyRequest().authenticated();

          // Custom JWT based security filter
          httpSecurity
                  .addFilterBefore(new JwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);

          // disable page caching
          httpSecurity
                  .headers()
                  .frameOptions().sameOrigin()
                  .cacheControl();

          return httpSecurity.build();
  }



}
