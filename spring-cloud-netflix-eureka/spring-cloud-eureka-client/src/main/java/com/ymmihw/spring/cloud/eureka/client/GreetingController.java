package com.ymmihw.spring.cloud.eureka.client;

import org.springframework.web.bind.annotation.RequestMapping;

public interface GreetingController {
  @RequestMapping("/greeting")
  String greeting();
}
