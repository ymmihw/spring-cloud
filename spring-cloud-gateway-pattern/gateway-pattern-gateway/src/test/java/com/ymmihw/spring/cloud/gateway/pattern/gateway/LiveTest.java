package com.ymmihw.spring.cloud.gateway.pattern.gateway;

import static io.restassured.RestAssured.config;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import com.ymmihw.spring.cloud.gateway.pattern.gateway.client.book.Book;
import io.restassured.RestAssured;
import io.restassured.authentication.FormAuthConfig;
import io.restassured.config.RedirectConfig;
import io.restassured.response.Response;

public class LiveTest {

  private final String ROOT_URI = "http://localhost:8080";
  private final FormAuthConfig formConfig = new FormAuthConfig("/login", "username", "password");

  @Before
  public void setup() {
    RestAssured.config = config().redirect(RedirectConfig.redirectConfig().followRedirects(false));
  }

  @Test
  public void whenGetAllBooks_thenSuccess() {
    final Response response = RestAssured.get(ROOT_URI + "/book-service/books");
    Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    Assert.assertNotNull(response.getBody());
  }

  @Test
  public void whenAccessProtectedResourceWithoutLogin_thenRedirectToLogin() {
    final Response response = RestAssured.get(ROOT_URI + "/home/index.html");
    Assert.assertEquals(HttpStatus.FOUND.value(), response.getStatusCode());
    Assert.assertEquals("http://localhost:8080/login", response.getHeader("Location"));
  }

  @Test
  public void whenAccessProtectedResourceAfterLogin_thenSuccess() {
    final Response response = RestAssured.given().auth().form("user", "password", formConfig)
        .get(ROOT_URI + "/book-service/books/1");
    Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    Assert.assertNotNull(response.getBody());
  }

  @Test
  public void whenAccessAdminProtectedResource_thenForbidden() {
    final Response response = RestAssured.given().auth().form("user", "password", formConfig)
        .get(ROOT_URI + "/rating-service/ratings/1");
    Assert.assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCode());

  }

  @Test
  public void whenAdminAccessProtectedResource_thenSuccess() {
    final Response response = RestAssured.given().auth().form("admin", "admin", formConfig)
        .get(ROOT_URI + "/rating-service/ratings/1");
    Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    Assert.assertNotNull(response.getBody());
  }

  @Test
  public void whenAdminAccessDiscoveryResource_thenSuccess() {
    final Response response =
        RestAssured.given().auth().form("admin", "admin", formConfig).get(ROOT_URI + "/discovery");
    Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
  }

  @Test
  public void accessCombinedEndpoint() {
    final Response response = RestAssured.given().auth().form("user", "password", formConfig)
        .get(ROOT_URI + "/combined?bookId=1");
    Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    Assert.assertNotNull(response.getBody());
    final Book result = response.as(Book.class);
    Assert.assertEquals(new Long(1), result.getId());
    Assert.assertNotNull(result.getRatings());
    Assert.assertTrue(result.getRatings().size() > 0);
  }
}
