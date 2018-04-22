/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package com.ymmihw.spring.cloud.zookeeper.service.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
  @Autowired
  private ProviderClient providerClient;

  @GetMapping("/get-greeting")
  public String greeting() {
    return providerClient.HelloWorld();
  }
}
