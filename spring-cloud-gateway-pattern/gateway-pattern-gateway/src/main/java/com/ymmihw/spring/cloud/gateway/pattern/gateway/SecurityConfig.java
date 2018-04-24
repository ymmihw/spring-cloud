package com.ymmihw.spring.cloud.gateway.pattern.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  @Bean
  public static PasswordEncoder passwordEncoder() {
    return NoOpPasswordEncoder.getInstance();
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication().withUser("user").password("password").roles("USER").and()
        .withUser("admin").password("admin").roles("ADMIN");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // @formatter:off
    http.formLogin().defaultSuccessUrl("/home/index.html", true).and()
        .authorizeRequests()
            .antMatchers("/book-service/**", "/rating-service/**", "/login*", "/").permitAll()
            .antMatchers("/eureka/**").hasRole("ADMIN")
            .anyRequest().authenticated().and()
        .logout().and()
        .csrf().disable();
    // @formatter:on
  }
}
