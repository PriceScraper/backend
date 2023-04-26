package be.xplore.pricescraper.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import be.xplore.pricescraper.domain.shops.ItemPrice;
import be.xplore.pricescraper.dtos.TrackItem;
import be.xplore.pricescraper.repositories.ItemPriceRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatusCode;

@SpringBootTest
@Slf4j
public class ItemControllerTests {
  @Autowired
  private ItemsController itemsController;
  @MockBean
  private ItemPriceRepository itemPriceRepository;

  @BeforeEach
  void prepare() {
    // to be mocked due to SQL queries being ran during other tests.
    given(itemPriceRepository.save(any())).willReturn(new ItemPrice());
  }

  @Test
  void findItemsByNameLike() {
    var response = itemsController.findItemsByNameLike("someName");
    assertNotNull(response);
  }

  @Test
  void findItemByIdWithLatestPrices() {
    var response = itemsController.findItemByIdWithLatestPrices(1);
    assertNotNull(response);
  }

  @Test
  void trackItemSuccess() {
    var body = new TrackItem(
        "https://drive.carrefour.be/nl/"
            +
            "Baby/Babyvoeding/Snacks-%26-desserts/Desserts-met-fruit/Vanaf-6-maand/Carrefour-Baby%7CBio-Appel%2C-Aardbei-vanaf-6-Maanden-4-x-100-g/p/06358717");
    var response = itemsController.trackItem(body);
    assertNotNull(response);
    log.info(response.getBody().toString());
    assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
  }

  @Test
  void trackItemFailure() {
    var body = new TrackItem("https://drive.carrefour.be/nl/Dranken/itemdoesnotexist");
    var response = itemsController.trackItem(body);
    assertNotNull(response);
    assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
  }
}
