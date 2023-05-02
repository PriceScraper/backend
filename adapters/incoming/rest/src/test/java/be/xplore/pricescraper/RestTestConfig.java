package be.xplore.pricescraper;

import be.xplore.pricescraper.config.FrontendConfig;
import be.xplore.pricescraper.config.JwtConfig;
import be.xplore.pricescraper.repositories.UserRepository;
import be.xplore.pricescraper.services.ShoppingListService;
import be.xplore.pricescraper.utils.security.JwtProvider;
import be.xplore.pricescraper.utils.security.JwtRequestFilter;
import be.xplore.pricescraper.utils.security.JwtSuccessHandler;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@TestConfiguration
@SpringBootConfiguration
@EnableWebMvc
public class RestTestConfig {
  @MockBean
  private UserRepository userRepository;
  @MockBean
  private JwtSuccessHandler jwtSuccessHandler;
  @MockBean
  private JwtProvider jwtProvider;
  @MockBean
  private JwtRequestFilter jwtRequestFilter;
  @MockBean
  private JwtConfig jwtConfig;
  @MockBean
  private FrontendConfig frontendConfig;
  @MockBean
  private ShoppingListService shoppingListService;
}
