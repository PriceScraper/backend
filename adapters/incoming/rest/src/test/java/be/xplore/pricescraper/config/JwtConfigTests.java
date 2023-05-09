package be.xplore.pricescraper.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JwtConfigTests {
  private JwtConfig jwtConfig;

  @BeforeEach
  void prepare() {
    jwtConfig = new JwtConfig();
    jwtConfig.setSecret("mySecret");
  }

  @Test
  void constructed() {
    assertNotNull(jwtConfig);
  }

  @Test
  void get() {
    assertNotNull(jwtConfig.getSecret());
    assertTrue(jwtConfig.getSecret().length() > 0);
  }

  @Test
  void set() {
    assertNotNull(jwtConfig.getSecret());
    jwtConfig.setSecret(null);
    assertNull(jwtConfig.getSecret());
  }
}
