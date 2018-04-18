package com.ymmihw.spring.cloud.security.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.RedisFlushMode;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Configuration
@EnableRedisHttpSession(redisFlushMode = RedisFlushMode.IMMEDIATE)
public class SessionConfig extends AbstractHttpSessionApplicationInitializer {
  @Bean
  DefaultCookieSerializer defaultCookieSerializer() {
    DefaultCookieSerializer o = new DefaultCookieSerializer();
    o.setUseBase64Encoding(false);
    return o;
  }
}
