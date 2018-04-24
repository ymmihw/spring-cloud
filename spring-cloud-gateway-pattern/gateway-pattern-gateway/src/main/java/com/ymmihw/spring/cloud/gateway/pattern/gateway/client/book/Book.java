package com.ymmihw.spring.cloud.gateway.pattern.gateway.client.book;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ymmihw.spring.cloud.gateway.pattern.gateway.client.rating.Rating;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Book {
  private Long id;
  private String author;
  private String title;
  private List<Rating> ratings;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public List<Rating> getRatings() {
    return ratings;
  }

  public void setRatings(List<Rating> ratings) {
    this.ratings = ratings;
  }
}
