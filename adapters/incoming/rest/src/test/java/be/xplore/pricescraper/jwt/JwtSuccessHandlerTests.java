package be.xplore.pricescraper.jwt;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import be.xplore.pricescraper.config.FrontendConfig;
import be.xplore.pricescraper.config.JwtConfig;
import be.xplore.pricescraper.utils.security.JwtProvider;
import be.xplore.pricescraper.utils.security.JwtSuccessHandler;
import org.junit.jupiter.api.Test;

class JwtSuccessHandlerTests {
  private final JwtSuccessHandler jwtSuccessHandler;

  public JwtSuccessHandlerTests() {
    this.jwtSuccessHandler =
        new JwtSuccessHandler(new JwtProvider(new JwtConfig()), null, new FrontendConfig());
  }

  @Test
  void initialized() {
    assertNotNull(jwtSuccessHandler);
  }
}
