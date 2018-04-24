package com.ymmihw.spring.cloud.gateway.pattern.gateway.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ymmihw.spring.cloud.gateway.pattern.gateway.client.book.Book;
import com.ymmihw.spring.cloud.gateway.pattern.gateway.client.book.BooksClient;
import com.ymmihw.spring.cloud.gateway.pattern.gateway.client.rating.Rating;
import com.ymmihw.spring.cloud.gateway.pattern.gateway.client.rating.RatingsClient;

@RestController
@RequestMapping("/combined")
public class CombinedController {

  private final BooksClient booksClient;
  private final RatingsClient ratingsClient;

  @Autowired
  public CombinedController(BooksClient booksClient, RatingsClient ratingsClient) {
    this.booksClient = booksClient;
    this.ratingsClient = ratingsClient;
  }

  @GetMapping
  public Book getCombinedResponse(@RequestParam Long bookId,
      @CookieValue("SESSION") String session) {
    Book book = booksClient.getBookById(bookId, "SESSION=" + session);
    List<Rating> ratings = ratingsClient.getRatingsByBookId(bookId, "SESSION=" + session);
    book.setRatings(ratings);
    return book;
  }
}
