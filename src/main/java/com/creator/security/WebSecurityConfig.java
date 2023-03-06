package com.creator.security;

import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.management.HttpSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;


@KeycloakConfiguration
public class WebSecurityConfig extends KeycloakWebSecurityConfigurerAdapter {


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {

        KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper( new SimpleAuthorityMapper());
        auth.authenticationProvider(keycloakAuthenticationProvider);
    }

    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    @Override
    @ConditionalOnMissingBean(HttpSessionManager.class)
    protected HttpSessionManager httpSessionManager() {
        return new HttpSessionManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        super.configure(http);
            http. csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                    .authorizeRequests()
                    .antMatchers("/books").hasAnyRole("Member", "Librarian")
                    .antMatchers("/creator/**").hasRole("user")
                    .anyRequest().permitAll();
        http.addFilterBefore(new JwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        http.cors();
    }
    /*
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
          httpSecurity
                  // we don't need CSRF because our token is invulnerable
                  .csrf().disable()
                  .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                  // don't create session
                 // .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                  .authorizeRequests()
                  .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                  .antMatchers("/users/**").hasRole("ADMIN")
                  .antMatchers("/creator/*").hasRole("user")
                  .antMatchers("/creator/api/v1/auth/**").permitAll()
                  .antMatchers("/actuator/**").permitAll()
                  .anyRequest().authenticated();

          // Custom JWT based security filter
          //httpSecurity
            //      .addFilterBefore(new JwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);

          // disable page caching
          httpSecurity
                  .headers()
                  .frameOptions().sameOrigin()
                  .cacheControl();

          return httpSecurity.build();
  }
     */
}