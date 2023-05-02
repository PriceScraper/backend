package be.xplore.pricescraper;

import be.xplore.pricescraper.services.ItemServiceImpl;
import be.xplore.pricescraper.services.ShoppingListServiceImpl;
import be.xplore.pricescraper.services.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class PriceScraperApplicationTests {
  @MockBean
  private ItemServiceImpl itemService;
  @MockBean
  private ShoppingListServiceImpl shoppingListService;
  @MockBean
  private UserServiceImpl userService;

  @Test
  void contextLoads() {
  }

}
