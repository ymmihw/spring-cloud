package com.ymmihw.spring.cloud.gateway.pattern.gateway.client.book;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "book-service")
public interface BooksClient {

  @RequestMapping(value = "/books/{bookId}", method = {RequestMethod.GET})
  Book getBookById(@PathVariable("bookId") Long bookId, @RequestHeader("Cookie") String session);
}
