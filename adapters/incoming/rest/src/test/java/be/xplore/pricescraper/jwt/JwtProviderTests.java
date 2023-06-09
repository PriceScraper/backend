package be.xplore.pricescraper.jwt;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.xplore.pricescraper.config.JwtConfig;
import be.xplore.pricescraper.utils.security.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JwtProviderTests {
  private final JwtProvider jwtProvider;
  private String token;

  public JwtProviderTests() {
    var conf = new JwtConfig();
    conf.setSecret("mySecret");
    this.jwtProvider = new JwtProvider(conf);
  }

  @BeforeEach
  void prepare() {
    token = jwtProvider.generate(1L, "username", "myAvatar");
  }

  @Test
  void generate() {
    assertNotNull(token);
    assertTrue(token.length() > 10);
    assertTrue(token.startsWith("ey"));
  }

  @Test
  void validate() {
    assertTrue(jwtProvider.validateToken(token));
  }

  @Test
  void getUserId() {
    var response = jwtProvider.getUserIdFromToken(token);
    assertTrue(response > 0);
  }
}
