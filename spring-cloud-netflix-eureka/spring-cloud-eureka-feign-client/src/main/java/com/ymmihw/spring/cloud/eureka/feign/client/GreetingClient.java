package com.ymmihw.spring.cloud.eureka.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import com.ymmihw.spring.cloud.eureka.client.GreetingController;

@FeignClient("spring-cloud-eureka-client")
public interface GreetingClient extends GreetingController {
}
