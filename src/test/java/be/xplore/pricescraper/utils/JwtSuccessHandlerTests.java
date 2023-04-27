package be.xplore.pricescraper.utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import be.xplore.pricescraper.config.FrontendConfig;
import be.xplore.pricescraper.config.JwtConfig;
import be.xplore.pricescraper.util.JwtProvider;
import be.xplore.pricescraper.util.JwtSuccessHandler;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JwtSuccessHandlerTests {
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
