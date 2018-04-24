package com.ymmihw.spring.cloud.gateway.pattern.svc.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // @formatter:off
    http.httpBasic().disable()
        .authorizeRequests()
            .antMatchers("/books").permitAll()
            .antMatchers("/books/*").hasAnyRole("USER", "ADMIN")
            .anyRequest().authenticated().and()
        .csrf().disable();
    // @formatter:on
  }
}
