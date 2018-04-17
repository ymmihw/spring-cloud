package com.ymmihw.spring.cloud.security.svc.rating;

public class Rating {
  private Long id;
  private Long bookId;
  private int stars;

  public Rating(Long id, Long bookId, int stars) {
    super();
    this.id = id;
    this.bookId = bookId;
    this.stars = stars;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getBookId() {
    return bookId;
  }

  public void setBookId(Long bookId) {
    this.bookId = bookId;
  }

  public int getStars() {
    return stars;
  }

  public void setStars(int stars) {
    this.stars = stars;
  }

  // standard getters and setters


}
