package be.xplore.pricescraper.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.domain.shops.ItemPrice;
import be.xplore.pricescraper.dtos.ItemSearchDto;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@Sql({"/sql/item_test_data.sql"})
@DataJpaTest
class ItemRepositoryTests {

  @Autowired
  ItemRepository itemRepository;

  @Test
  void itemShouldHave2TrackedItems() {
    Item item =
        itemRepository.findItemWithTrackedItemsById(1);
    assertThat(item.getTrackedItems()).hasSize(2);
  }

  @Test
  void trackedItemsShouldHaveLatestPrices() {
    Item item =
        itemRepository.findItemWithTrackedItemsById(1);
    List<ItemPrice> itemPrices =
        itemRepository.findLatestPricesForTrackedItems(item.getTrackedItems());
    List<String> timestampsAsStrings =
        itemPrices.stream().map(itemPrice -> itemPrice.getTimestamp().toString()).toList();
    List<String> expectedTimestamps = List.of("2023-04-22 12:00:00.0", "2023-04-21 12:00:00.0");
    assertThat(timestampsAsStrings).usingRecursiveComparison().ignoringCollectionOrder()
        .isEqualTo(expectedTimestamps);
    assertThat(itemPrices).hasSize(2);
  }

  @Test
  void resultShouldHave2Items() {
    List<ItemSearchDto> items =
        itemRepository.findItemsByNameLike("PIzZa");
    assertThat(items).hasSize(2);
  }

}
