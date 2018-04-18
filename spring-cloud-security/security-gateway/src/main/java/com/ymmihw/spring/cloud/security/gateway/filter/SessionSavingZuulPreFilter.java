package com.ymmihw.spring.cloud.security.gateway.filter;

import javax.servlet.http.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Component;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class SessionSavingZuulPreFilter extends ZuulFilter {

  private Logger log = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private SessionRepository<?> repository;

  @Override
  public boolean shouldFilter() {
    return true;
  }

  @Override
  public Object run() {
    RequestContext context = RequestContext.getCurrentContext();
    Cookie[] cookies = context.getRequest().getCookies();

    String sessionId = null;
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equalsIgnoreCase("SESSION")) {
          sessionId = cookie.getValue();
          break;
        }
      }
    }
    Session session = repository.findById(context.getRequest().getSession().getId());
    context.addZuulRequestHeader("Cookie", "SESSION=" + sessionId);
    log.info("ZuulPreFilter session proxy: {}", sessionId);
    return null;
  }

  @Override
  public String filterType() {
    return "pre";
  }

  @Override
  public int filterOrder() {
    return 0;
  }
}
