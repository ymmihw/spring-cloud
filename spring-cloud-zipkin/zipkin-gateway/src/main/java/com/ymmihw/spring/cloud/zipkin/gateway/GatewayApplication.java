package com.ymmihw.spring.cloud.zipkin.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.sleuth.metric.SpanMetricReporter;
import org.springframework.cloud.sleuth.zipkin.HttpZipkinSpanReporter;
import org.springframework.cloud.sleuth.zipkin.ZipkinSpanReporter;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import zipkin.Span;

@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
public class GatewayApplication {
  public static void main(String[] args) {
    SpringApplication.run(GatewayApplication.class, args);
  }

  @Autowired
  private EurekaClient eurekaClient;

  @Autowired
  private SpanMetricReporter spanMetricReporter;

  @Value("${spring.sleuth.web.skipPattern}")
  private String skipPattern;

  @Bean
  public ZipkinSpanReporter makeZipkinSpanReporter() {
    return new ZipkinSpanReporter() {
      private HttpZipkinSpanReporter delegate;
      private String baseUrl;

      @Override
      public void report(Span span) {
        InstanceInfo instance = eurekaClient.getNextServerFromEureka("zipkin", false);
        if (!(baseUrl != null && instance.getHomePageUrl().equals(baseUrl))) {
          baseUrl = instance.getHomePageUrl();
          delegate = new HttpZipkinSpanReporter(new RestTemplate(), baseUrl, 1, spanMetricReporter);
        }
        if (!span.name.matches(skipPattern))
          delegate.report(span);
      }
    };
  }
}
