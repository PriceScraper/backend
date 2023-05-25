package be.xplore.pricescraper.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;

import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.domain.shops.TrackedItem;
import be.xplore.pricescraper.dtos.TrackItem;
import be.xplore.pricescraper.services.BarcodeItemService;
import be.xplore.pricescraper.services.ItemService;
import be.xplore.pricescraper.services.ItemServiceImpl;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = {ItemsController.class, ItemServiceImpl.class})
@Slf4j
class ItemControllerTests {
  @Autowired
  private ItemsController itemsController;
  @MockBean
  private ItemService itemService;
  @MockBean
  private BarcodeItemService barcodeItemService;

  @BeforeEach
  void prepare() {
    var item = new Item();
    item.setTrackedItems(List.of());
    given(itemService.findItemWithTrackedItemsAndLatestPricesById(1)).willReturn(item);

    given(itemService.addTrackedItem("https://drive.carrefour.be/nl/Dranken/itemexists"))
        .willReturn(new TrackedItem());
  }

  @Test
  void trackItemSuccess() {
    var body = new TrackItem(
        "https://drive.carrefour.be/nl/Dranken/itemexists");
    var response = itemsController.trackItem(body);
    assertNotNull(response);
    assertThat(response).isNotNull();
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
