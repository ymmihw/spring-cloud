package com.ymmihw.spring.cloud.zipkin.gateway.filter;

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
    String sessionId = context.getRequest().getSession().getId();
    Session session = repository.getSession(sessionId);
    context.addZuulRequestHeader("Cookie", "SESSION=" + session.getId());
    log.info("ZuulPreFilter session proxy: {}", session.getId());
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
