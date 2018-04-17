package com.ymmihw.spring.cloud.security.svc.book;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

@EnableRedisHttpSession
public class SessionConfig extends AbstractHttpSessionApplicationInitializer {
}