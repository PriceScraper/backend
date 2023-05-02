package be.xplore.pricescraper.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import be.xplore.pricescraper.domain.shops.ItemPrice;
import be.xplore.pricescraper.repositories.ItemPriceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class ItemServiceIntegrationTests {
  @Autowired
  private ItemService itemService;
  @MockBean
  private ItemPriceRepository itemPriceRepository;

  @BeforeEach
  void prepare() {
    // to be mocked due to SQL queries being ran during other tests.
    given(itemPriceRepository.save(any())).willReturn(new ItemPrice());
  }

  @Test
  void trackItemSuccess() {
    var response = itemService.addTrackedItem(
        "https://drive.carrefour.be/nl/Dranken/Softdrinks/Sport--%26-gezonde-dranken/Aquarius%7CLemon-500-ml/p/06093663");
    assertTrue(response.hasSucceeded());
    assertNotNull(response.object());
    assertNull(response.message());
  }

  @Test
  void trackItemFailure() {
    var response = itemService.addTrackedItem("https://drive.carrefour.be/nl/itemdoesnotexist");
    assertFalse(response.hasSucceeded());
    assertNull(response.object());
    assertNotNull(response.message());
  }
}
