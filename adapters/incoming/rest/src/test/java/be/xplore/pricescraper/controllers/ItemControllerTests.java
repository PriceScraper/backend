package be.xplore.pricescraper.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;

import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.domain.shops.TrackedItem;
import be.xplore.pricescraper.dtos.ServiceResponse;
import be.xplore.pricescraper.dtos.TrackItem;
import be.xplore.pricescraper.services.ItemService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatusCode;

@SpringBootTest(classes = ItemsController.class)
@Slf4j
public class ItemControllerTests {
  @Autowired
  private ItemsController itemsController;
  @MockBean
  private ItemService itemService;

  @BeforeEach
  void prepare() {
    var item = new Item();
    item.setTrackedItems(List.of());
    given(itemService.findItemWithTrackedItemsAndLatestPricesById(1)).willReturn(item);

    given(itemService.addTrackedItem("https://drive.carrefour.be/nl/Dranken/itemexists"))
        .willReturn(new ServiceResponse<>(true, new TrackedItem(), null));
    given(itemService.addTrackedItem("https://drive.carrefour.be/nl/Dranken/itemdoesnotexist"))
        .willReturn(new ServiceResponse<>(false, null, "Some error"));
  }

  @Test
  void trackItemSuccess() {
    var body = new TrackItem(
        "https://drive.carrefour.be/nl/Dranken/itemexists");
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
}
