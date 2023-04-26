package be.xplore.pricescraper.utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import be.xplore.pricescraper.utils.security.JwtRequestFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JwtRequestFilterTests {
  private JwtRequestFilter jwtRequestFilter;

  @BeforeEach
  void prepare() {
    jwtRequestFilter = new JwtRequestFilter();
  }

  @Test
  void constructed() {
    assertNotNull(jwtRequestFilter);
  }
}
