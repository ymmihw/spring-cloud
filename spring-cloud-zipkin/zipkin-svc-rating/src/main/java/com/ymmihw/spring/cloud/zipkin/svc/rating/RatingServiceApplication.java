package com.ymmihw.spring.cloud.zipkin.svc.rating;

import static java.util.Collections.emptyList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.sleuth.metric.SpanMetricReporter;
import org.springframework.cloud.sleuth.zipkin.HttpZipkinSpanReporter;
import org.springframework.cloud.sleuth.zipkin.ZipkinSpanReporter;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import zipkin.Span;

@SpringBootApplication
@EnableEurekaClient
@RestController
@RequestMapping("/ratings")
public class RatingServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(RatingServiceApplication.class, args);
  }

  private List<Rating> ratingList = Arrays.asList(new Rating(1L, 1L, 2), new Rating(2L, 1L, 3),
      new Rating(3L, 2L, 4), new Rating(4L, 2L, 5));

  @GetMapping("/{bookId}")
  public List<Rating> findRatingsByBookId(@PathVariable Long bookId) {
    return bookId == null || bookId.equals(0L) ? emptyList()
        : ratingList.stream().filter(r -> r.getBookId().equals(bookId))
            .collect(Collectors.toList());
  }

  @GetMapping("")
  public List<Rating> findAllRatings() {
    return ratingList;
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
