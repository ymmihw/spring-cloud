package com.ymmihw.spring.cloud.zipkin.svc.book;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.sleuth.metric.SpanMetricReporter;
import org.springframework.cloud.sleuth.zipkin.HttpZipkinSpanReporter;
import org.springframework.cloud.sleuth.zipkin.ZipkinProperties;
import org.springframework.cloud.sleuth.zipkin.ZipkinSpanReporter;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import zipkin.Span;

@SpringBootApplication
@EnableEurekaClient
@RestController
@RequestMapping("/books")
public class BookServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(BookServiceApplication.class, args);
  }

  private List<Book> bookList =
      Arrays.asList(new Book(1L, "Baeldung goes to the market", "Tim Schimandle"),
          new Book(2L, "Baeldung goes to the park", "Slavisa"));

  @GetMapping("")
  public List<Book> findAllBooks() {
    return bookList;
  }

  @GetMapping("/{bookId}")
  public Book findBook(@PathVariable Long bookId) {
    return bookList.stream().filter(b -> b.getId().equals(bookId)).findFirst().orElse(null);
  }


  @Autowired
  private EurekaClient eurekaClient;

  @Autowired
  private SpanMetricReporter spanMetricReporter;

  @Autowired
  private ZipkinProperties zipkinProperties;

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
          delegate = new HttpZipkinSpanReporter(baseUrl, zipkinProperties.getFlushInterval(),
              zipkinProperties.getCompression().isEnabled(), spanMetricReporter);
          if (!span.name.matches(skipPattern))
            delegate.report(span);
        }
      }
    };
  }
}
