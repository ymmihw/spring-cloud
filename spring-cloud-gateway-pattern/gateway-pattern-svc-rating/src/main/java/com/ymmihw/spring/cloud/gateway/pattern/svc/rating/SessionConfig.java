package com.ymmihw.spring.cloud.gateway.pattern.svc.rating;

import org.springframework.context.annotation.Bean;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@EnableRedisHttpSession
public class SessionConfig extends AbstractHttpSessionApplicationInitializer {
  @Bean
  DefaultCookieSerializer defaultCookieSerializer() {
    DefaultCookieSerializer o = new DefaultCookieSerializer();
    o.setUseBase64Encoding(false);
    return o;
  }
}
